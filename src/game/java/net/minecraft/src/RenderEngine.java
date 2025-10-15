package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;
import com.carrotsearch.hppc.cursors.ObjectIntCursor;

import net.lax1dude.eaglercraft.beta.SpriteSheetTexture;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.peyton.eagler.minecraft.TextureLocation;

public class RenderEngine {
	public static boolean useMipmaps = false;
	private ObjectIntMap<String> textureMap = new ObjectIntHashMap<>();
	private IntObjectMap<ImageData> textureNameToImageMap = new IntObjectHashMap<>();
	private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(1048576);
	private List<TextureFX> field_1604_f = new ArrayList<TextureFX>();
	private List<SpriteSheetTexture> textureSpriteList = new ArrayList<SpriteSheetTexture>();
	private GameSettings options;
	private boolean clampTexture = false;
	private boolean blurTexture = false;
	public TexturePackList field_6527_k;
	
	private Logger LOGGER = LogManager.getLogger();

	public RenderEngine(TexturePackList var1, GameSettings var2) {
		this.field_6527_k = var1;
		this.options = var2;
	}
	
	public int getTextureWidth(TextureLocation location) {
		ImageData image = textureNameToImageMap.get(location.getTextureID());
		if (image != null) {
			return image.width;
		}
		return 0;
	}
	
	public int getTextureHeight(TextureLocation location) {
		ImageData image = textureNameToImageMap.get(location.getTextureID());
		if (image != null) {
			return image.height;
		}
		return 0;
	}

	public int getTexture(String var1) {
		TexturePackBase var2 = this.field_6527_k.selectedTexturePack;
		int var3 = this.textureMap.getOrDefault(var1, -1);
		if (var3 >= 0) {
			return var3;
		} else {
			try {
				int var5 = GLAllocation.generateTextureName();
				if (var1.startsWith("##")) {
					this.setupTexture(
							this.unwrapImageByColumns(
									this.readTextureImage(var2.func_6481_a(var1.substring(2)))),
							var5);
				} else if (var1.startsWith("%clamp%")) {
					this.clampTexture = true;
					this.setupTexture(this.readTextureImage(var2.func_6481_a(var1.substring(7))),
							var5);
					this.clampTexture = false;
				} else if (var1.startsWith("%blur%")) {
					this.blurTexture = true;
					this.setupTexture(this.readTextureImage(var2.func_6481_a(var1.substring(6))),
							var5);
					this.blurTexture = false;
				} else {
					this.setupTexture(this.readTextureImage(var2.func_6481_a(var1)), var5);
				}

				this.textureMap.put(var1, var5);
				return var5;
			} catch (IOException var4) {
				throw new RuntimeException("!!");
			}
		}
	}

	private ImageData unwrapImageByColumns(ImageData var1) {
		int var2 = var1.getWidth() / 16;
		ImageData var3 = new ImageData(16, var1.getHeight(), var1.alpha);

		for (int var5 = 0; var5 < var2; ++var5) {
			var3.drawLayer(var1, 0, var5 * var1.getHeight(), 16, (var5 + 1) * var1.getHeight(), var5 * 16, 0,
					(var5 + 1) * 16, var1.getHeight());
		}

		return var3;
	}

	public int allocateAndSetupTexture(ImageData var1) {
		int var2 = GLAllocation.generateTextureName();
		this.setupTexture(var1, var2);
		this.textureNameToImageMap.put(var2, var1);
		return var2;
	}

	public void setupTexture(ImageData var1, int var2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
		if (useMipmaps) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if (this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if (this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		int var3 = var1.getWidth();
		int var4 = var1.getHeight();
		int[] var5 = new int[var3 * var4];
		byte[] var6 = new byte[var3 * var4 * 4];
		var1.getRGB(0, 0, var3, var4, var5, 0, var3);

		int var7;
		int var8;
		int var9;
		int var10;
		int var11;
		int var12;
		int var13;
		int var14;
		for (var7 = 0; var7 < var5.length; ++var7) {
			var8 = var5[var7] >> 24 & 255;
			var9 = var5[var7] >> 16 & 255;
			var10 = var5[var7] >> 8 & 255;
			var11 = var5[var7] & 255;
			if (this.options != null && this.options.anaglyph) {
				var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
				var13 = (var9 * 30 + var10 * 70) / 100;
				var14 = (var9 * 30 + var11 * 70) / 100;
				var9 = var12;
				var10 = var13;
				var11 = var14;
			}

			var6[var7 * 4 + 0] = (byte) var9;
			var6[var7 * 4 + 1] = (byte) var10;
			var6[var7 * 4 + 2] = (byte) var11;
			var6[var7 * 4 + 3] = (byte) var8;
		}

		this.imageData.clear();
		this.imageData.put(var6);
		this.imageData.position(0).limit(var6.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var3, var4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) this.imageData);
		if (useMipmaps) {
			for (var7 = 1; var7 <= 4; ++var7) {
				var8 = var3 >> var7 - 1;
				var9 = var3 >> var7;
				var10 = var4 >> var7;

				for (var11 = 0; var11 < var9; ++var11) {
					for (var12 = 0; var12 < var10; ++var12) {
						var13 = this.imageData.getInt((var11 * 2 + 0 + (var12 * 2 + 0) * var8) * 4);
						var14 = this.imageData.getInt((var11 * 2 + 1 + (var12 * 2 + 0) * var8) * 4);
						int var15 = this.imageData.getInt((var11 * 2 + 1 + (var12 * 2 + 1) * var8) * 4);
						int var16 = this.imageData.getInt((var11 * 2 + 0 + (var12 * 2 + 1) * var8) * 4);
						int var17 = this.weightedAverageColor(this.weightedAverageColor(var13, var14),
								this.weightedAverageColor(var15, var16));
						this.imageData.putInt((var11 + var12 * var9) * 4, var17);
					}
				}

				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, var7, GL11.GL_RGBA, var9, var10, 0, GL11.GL_RGBA,
						GL11.GL_UNSIGNED_BYTE, (ByteBuffer) this.imageData);
			}
		}

	}

	public void deleteTexture(int var1) {
		this.textureNameToImageMap.remove(var1);
		GL11.glDeleteTexture(var1);
	}

	public int getTextureForDownloadableImage(String var1, String var2) {
		return this.getTexture(var2);
	}

	public void releaseImageData(String var1) {
	}

	public void registerTextureFX(TextureFX var1) {
		this.field_1604_f.add(var1);
		var1.func_783_a();
	}

	public void updateTerrainTextures() {

		for (int i = 0; i < field_1604_f.size(); i++) {
			TextureFX texturefx = (TextureFX) field_1604_f.get(i);
			texturefx.field_1131_c = options.anaglyph;
			texturefx.func_783_a();
			texturefx.func_782_a(this);
			int tileSize = 16 * 16 * 4;
			imageData.clear();
			imageData.put(texturefx.field_1127_a);
			imageData.position(0).limit(tileSize);
			GL11.glTexSubImage2D(3553 /* GL_TEXTURE_2D */, 0, (texturefx.field_1126_b % 16) * 16,
					(texturefx.field_1126_b / 16) * 16, 16, 16, 6408, 5121, imageData);
		}

		TextureLocation.terrain.bindTexture();
		for (int i = 0, l = textureSpriteList.size(); i < l; ++i) {
			SpriteSheetTexture sp = textureSpriteList.get(i);
			sp.update();
			int w = 16;
			//for (int j = 0; j < 5; ++j) {
			//No mipmaps :(
				GL11.glTexSubImage2D(3553, 0, (sp.iconIndex % 16) * w, (sp.iconIndex / 16) * w, w * sp.iconTileSize,
						w * sp.iconTileSize, 6408, 5121, sp.grabFrame(0));
				//w /= 2;
			//}
		}

	}

//	public void func_1067_a() {
//		int var1;
//		TextureFX var2;
//		int var3;
//		int var4;
//		int var5;
//		int var6;
//		int var7;
//		int var8;
//		int var9;
//		int var10;
//		int var11;
//		int var12;
//		for(var1 = 0; var1 < this.field_1604_f.size(); ++var1) {
//			var2 = (TextureFX)this.field_1604_f.get(var1);
//			var2.field_1131_c = this.options.anaglyph;
//			var2.func_783_a();
//			this.imageData.clear();
//			this.imageData.put(var2.field_1127_a);
//			this.imageData.position(0).limit(var2.field_1127_a.length);
//			var2.func_782_a(this);
//
//			for(var3 = 0; var3 < var2.field_1129_e; ++var3) {
//				for(var4 = 0; var4 < var2.field_1129_e; ++var4) {
//					GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var2.field_1126_b % 16 * 16 + var3 * 16, var2.field_1126_b / 16 * 16 + var4 * 16, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
//					if(useMipmaps) {
//						for(var5 = 1; var5 <= 4; ++var5) {
//							var6 = 16 >> var5 - 1;
//							var7 = 16 >> var5;
//
//							for(var8 = 0; var8 < var7; ++var8) {
//								for(var9 = 0; var9 < var7; ++var9) {
//									var10 = this.imageData.getInt((var8 * 2 + 0 + (var9 * 2 + 0) * var6) * 4);
//									var11 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2 + 0) * var6) * 4);
//									var12 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2 + 1) * var6) * 4);
//									int var13 = this.imageData.getInt((var8 * 2 + 0 + (var9 * 2 + 1) * var6) * 4);
//									int var14 = this.averageColor(this.averageColor(var10, var11), this.averageColor(var12, var13));
//									this.imageData.putInt((var8 + var9 * var7) * 4, var14);
//								}
//							}
//
//							GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, var5, var2.field_1126_b % 16 * var7, var2.field_1126_b / 16 * var7, var7, var7, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
//						}
//					}
//				}
//			}
//		}
//
//		for(var1 = 0; var1 < this.field_1604_f.size(); ++var1) {
//			var2 = (TextureFX)this.field_1604_f.get(var1);
//			if(var2.field_1130_d > 0) {
//				this.imageData.clear();
//				this.imageData.put(var2.field_1127_a);
//				this.imageData.position(0).limit(var2.field_1127_a.length);
//				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2.field_1130_d);
//				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
//				if(useMipmaps) {
//					for(var3 = 1; var3 <= 4; ++var3) {
//						var4 = 16 >> var3 - 1;
//						var5 = 16 >> var3;
//
//						for(var6 = 0; var6 < var5; ++var6) {
//							for(var7 = 0; var7 < var5; ++var7) {
//								var8 = this.imageData.getInt((var6 * 2 + 0 + (var7 * 2 + 0) * var4) * 4);
//								var9 = this.imageData.getInt((var6 * 2 + 1 + (var7 * 2 + 0) * var4) * 4);
//								var10 = this.imageData.getInt((var6 * 2 + 1 + (var7 * 2 + 1) * var4) * 4);
//								var11 = this.imageData.getInt((var6 * 2 + 0 + (var7 * 2 + 1) * var4) * 4);
//								var12 = this.averageColor(this.averageColor(var8, var9), this.averageColor(var10, var11));
//								this.imageData.putInt((var6 + var7 * var5) * 4, var12);
//							}
//						}
//
//						GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, var3, 0, 0, var5, var5, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
//					}
//				}
//			}
//		}
//
//	}

//	private int averageColor(int var1, int var2) {
//		int var3 = (var1 & -16777216) >> 24 & 255;
//		int var4 = (var2 & -16777216) >> 24 & 255;
//		return (var3 + var4 >> 1 << 24) + ((var1 & 16711422) + (var2 & 16711422) >> 1);
//	}

	private int weightedAverageColor(int var1, int var2) {
		int var3 = (var1 & -16777216) >> 24 & 255;
		int var4 = (var2 & -16777216) >> 24 & 255;
		short var5 = 255;
		if (var3 + var4 == 0) {
			var3 = 1;
			var4 = 1;
			var5 = 0;
		}

		int var6 = (var1 >> 16 & 255) * var3;
		int var7 = (var1 >> 8 & 255) * var3;
		int var8 = (var1 & 255) * var3;
		int var9 = (var2 >> 16 & 255) * var4;
		int var10 = (var2 >> 8 & 255) * var4;
		int var11 = (var2 & 255) * var4;
		int var12 = (var6 + var9) / (var3 + var4);
		int var13 = (var7 + var10) / (var3 + var4);
		int var14 = (var8 + var11) / (var3 + var4);
		return var5 << 24 | var12 << 16 | var13 << 8 | var14;
	}

	public void refreshTextures() {
		TexturePackBase var1 = this.field_6527_k.selectedTexturePack;
		ImageData var4;
		for(IntObjectCursor<ImageData> cursor : textureNameToImageMap) {
			if(cursor != null) {
				int var3 = cursor.key;
				var4 = cursor.value;
				this.setupTexture(var4, var3);
			}
		}
		
		for(ObjectIntCursor<String> cursor : textureMap) {
			if(cursor != null) {
				String var8 = cursor.key;
				
				try {
					if (var8.startsWith("##")) {
						var4 = this.unwrapImageByColumns(
								this.readTextureImage(var1.func_6481_a(var8.substring(2))));
					} else if (var8.startsWith("%clamp%")) {
						this.clampTexture = true;
						var4 = this.readTextureImage(var1.func_6481_a(var8.substring(7)));
					} else if (var8.startsWith("%blur%")) {
						this.blurTexture = true;
						var4 = this.readTextureImage(var1.func_6481_a(var8.substring(6)));
					} else {
						var4 = this.readTextureImage(var1.func_6481_a(var8));
					}

					int var5 = ((Integer) this.textureMap.get(var8)).intValue();
					this.setupTexture(var4, var5);
					this.blurTexture = false;
					this.clampTexture = false;
				} catch (IOException var6) {
					LOGGER.error(var6);
				}
			}
		}

		for (int j = 0, l = textureSpriteList.size(); j < l; ++j) {
			textureSpriteList.get(j).reloadData();
		}

	}

	private ImageData readTextureImage(InputStream var1) throws IOException {
		ImageData var2 = ImageData.loadImageFile(var1).swapRB();
		var1.close();
		return var2;
	}

	public void bindTexture(int var1) {
		if (var1 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
		}
	}

	public void registerSpriteSheet(String name, int iconIndex, int iconTileSize) {
		textureSpriteList.add(new SpriteSheetTexture(name, iconIndex, iconTileSize));
	}
}
