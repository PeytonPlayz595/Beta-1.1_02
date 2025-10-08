package net.minecraft.src;

import net.lax1dude.eaglercraft.Random;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.peyton.eagler.minecraft.Tessellator;
import net.peyton.eagler.minecraft.TextureLocation;

public class EffectRenderer {
	protected World worldObj;
	private ObjectArrayList<ObjectArrayList<EntityFX>> field_1728_b = new ObjectArrayList<>(4);
	private Random rand = new Random();

	public EffectRenderer(World var1, RenderEngine var2) {
		if(var1 != null) {
			this.worldObj = var1;
		}

		for(int var3 = 0; var3 < 4; ++var3) {
			this.field_1728_b.add(var3, new ObjectArrayList<>());
		}

	}

	public void func_1192_a(EntityFX var1) {
		int var2 = var1.func_404_c();
		this.field_1728_b.get(var2).add(var1);
	}

	public void func_1193_a() {
		for(int var1 = 0; var1 < 4; ++var1) {
			ObjectArrayList<EntityFX> list = this.field_1728_b.get(var1);
			for(int var2 = 0; var2 < list.size(); ++var2) {
				EntityFX var3 = list.get(var2);
				var3.onUpdate();
				if(var3.isDead) {
					list.remove(var2--);
				}
			}
		}

	}

	public void func_1189_a(Entity var1, float var2) {
		float var3 = MathHelper.cos(var1.rotationYaw * (float)Math.PI / 180.0F);
		float var4 = MathHelper.sin(var1.rotationYaw * (float)Math.PI / 180.0F);
		float var5 = -var4 * MathHelper.sin(var1.rotationPitch * (float)Math.PI / 180.0F);
		float var6 = var3 * MathHelper.sin(var1.rotationPitch * (float)Math.PI / 180.0F);
		float var7 = MathHelper.cos(var1.rotationPitch * (float)Math.PI / 180.0F);
		EntityFX.field_660_l = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
		EntityFX.field_659_m = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
		EntityFX.field_658_n = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;

		for(int var8 = 0; var8 < 3; ++var8) {
			ObjectArrayList<EntityFX> list = this.field_1728_b.get(var8);
			int size = list.size();
			if(size != 0) {
				TextureLocation var9 = null;
				if(var8 == 0) {
					var9 = TextureLocation.particles;
				}

				if(var8 == 1) {
					var9 = TextureLocation.terrain;
				}

				if(var8 == 2) {
					var9 = TextureLocation.items;
				}

				if(var9 != null) {
					var9.bindTexture();
				}
				Tessellator var10 = Tessellator.instance;
				var10.startDrawingQuads();

				for(int var11 = 0; var11 < size; ++var11) {
					EntityFX var12 = list.get(var11);
					var12.func_406_a(var10, var2, var3, var7, var4, var5, var6);
				}

				var10.draw();
			}
		}
	}

	public void func_1187_b(Entity var1, float var2) {
		byte var3 = 3;
		ObjectArrayList<EntityFX> list = this.field_1728_b.get(var3);
		int size = list.size();
		if(size != 0) {
			Tessellator var4 = Tessellator.instance;

			for(int var5 = 0; var5 < size; ++var5) {
				EntityFX var6 = list.get(var5);
				var6.func_406_a(var4, var2, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	public void func_1188_a(World var1) {
		this.worldObj = var1;

		for(int var2 = 0; var2 < 4; ++var2) {
			this.field_1728_b.get(var2).clear();
		}

	}

	public void func_1186_a(int var1, int var2, int var3) {
		int var4 = this.worldObj.getBlockId(var1, var2, var3);
		if(var4 != 0) {
			Block var5 = Block.blocksList[var4];
			byte var6 = 4;

			for(int var7 = 0; var7 < var6; ++var7) {
				for(int var8 = 0; var8 < var6; ++var8) {
					for(int var9 = 0; var9 < var6; ++var9) {
						double var10 = (double)var1 + ((double)var7 + 0.5D) / (double)var6;
						double var12 = (double)var2 + ((double)var8 + 0.5D) / (double)var6;
						double var14 = (double)var3 + ((double)var9 + 0.5D) / (double)var6;
						this.func_1192_a((new EntityDiggingFX(this.worldObj, var10, var12, var14, var10 - (double)var1 - 0.5D, var12 - (double)var2 - 0.5D, var14 - (double)var3 - 0.5D, var5)).func_4041_a(var1, var2, var3));
					}
				}
			}

		}
	}

	public void func_1191_a(int var1, int var2, int var3, int var4) {
		int var5 = this.worldObj.getBlockId(var1, var2, var3);
		if(var5 != 0) {
			Block var6 = Block.blocksList[var5];
			float var7 = 0.1F;
			double var8 = (double)var1 + this.rand.nextDouble() * (var6.maxX - var6.minX - (double)(var7 * 2.0F)) + (double)var7 + var6.minX;
			double var10 = (double)var2 + this.rand.nextDouble() * (var6.maxY - var6.minY - (double)(var7 * 2.0F)) + (double)var7 + var6.minY;
			double var12 = (double)var3 + this.rand.nextDouble() * (var6.maxZ - var6.minZ - (double)(var7 * 2.0F)) + (double)var7 + var6.minZ;
			if(var4 == 0) {
				var10 = (double)var2 + var6.minY - (double)var7;
			}

			if(var4 == 1) {
				var10 = (double)var2 + var6.maxY + (double)var7;
			}

			if(var4 == 2) {
				var12 = (double)var3 + var6.minZ - (double)var7;
			}

			if(var4 == 3) {
				var12 = (double)var3 + var6.maxZ + (double)var7;
			}

			if(var4 == 4) {
				var8 = (double)var1 + var6.minX - (double)var7;
			}

			if(var4 == 5) {
				var8 = (double)var1 + var6.maxX + (double)var7;
			}

			this.func_1192_a((new EntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6)).func_4041_a(var1, var2, var3).func_407_b(0.2F).func_405_d(0.6F));
		}
	}

	public String func_1190_b() {
		return "" + (this.field_1728_b.get(0).size() + this.field_1728_b.get(1).size() + this.field_1728_b.get(2).size());
	}
}
