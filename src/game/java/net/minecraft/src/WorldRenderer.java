package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.lwjgl.opengl.GL11;

import net.peyton.eagler.minecraft.Tessellator;

public class WorldRenderer {
	public World worldObj;
	private int glRenderList = -1;
	public static int chunksUpdated = 0;
	public int posX;
	public int posY;
	public int posZ;
	public int sizeWidth;
	public int sizeHeight;
	public int sizeDepth;
	public int field_1755_i;
	public int field_1754_j;
	public int field_1753_k;
	public int field_1752_l;
	public int field_1751_m;
	public int field_1750_n;
	public boolean isInFrustrum = false;
	public boolean[] skipRenderPass = new boolean[2];
	public int field_1746_q;
	public int field_1743_r;
	public int field_1741_s;
	public float field_1740_t;
	public boolean needsUpdate;
	public AxisAlignedBB field_1736_v;
	public int field_1735_w;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int field_1732_z;
	public boolean field_1747_A;
	private boolean isInitialized = false;
	public List<TileEntity> tileEntityRenderers = new ArrayList<>();
	private List<TileEntity> field_1737_F;
	public boolean isVisibleFromPosition = false;
	public double visibleFromX;
	public double visibleFromY;
	public double visibleFromZ;
	private boolean needsBoxUpdate = false;
	public boolean isInFrustrumFully = false;

	public WorldRenderer(World var1, List<TileEntity> var2, int var3, int var4, int var5, int var6, int var7) {
		this.worldObj = var1;
		this.field_1737_F = var2;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = var6;
		this.field_1740_t = MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth)) / 2.0F;
		this.glRenderList = var7;
		this.posX = -999;
		this.func_1197_a(var3, var4, var5);
		this.needsUpdate = false;
	}

	public void func_1197_a(int var1, int var2, int var3) {
		if(var1 != this.posX || var2 != this.posY || var3 != this.posZ) {
			this.func_1195_b();
			this.posX = var1;
			this.posY = var2;
			this.posZ = var3;
			this.field_1746_q = var1 + this.sizeWidth / 2;
			this.field_1743_r = var2 + this.sizeHeight / 2;
			this.field_1741_s = var3 + this.sizeDepth / 2;
			this.field_1752_l = var1 & 1023;
			this.field_1751_m = var2;
			this.field_1750_n = var3 & 1023;
			this.field_1755_i = var1 - this.field_1752_l;
			this.field_1754_j = var2 - this.field_1751_m;
			this.field_1753_k = var3 - this.field_1750_n;
			float var4 = 2.0F;
			this.field_1736_v = AxisAlignedBB.getBoundingBox((double)((float)var1 - var4), (double)((float)var2 - var4), (double)((float)var3 - var4), (double)((float)(var1 + this.sizeWidth) + var4), (double)((float)(var2 + this.sizeHeight) + var4), (double)((float)(var3 + this.sizeDepth) + var4));
			this.needsBoxUpdate = true;
			this.markDirty();
			this.isVisibleFromPosition = false;
		}
	}

	public void updateRenderer() {
		if(this.needsUpdate) {
			++chunksUpdated;
			if(this.needsBoxUpdate) {
				float xMin = 0.0F;
				GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
				RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.field_1752_l - xMin), (double)((float)this.field_1751_m - xMin), (double)((float)this.field_1750_n - xMin), (double)((float)(this.field_1752_l + this.sizeWidth) + xMin), (double)((float)(this.field_1751_m + this.sizeHeight) + xMin), (double)((float)(this.field_1750_n + this.sizeDepth) + xMin)));
				GL11.glEndList();
				this.needsBoxUpdate = false;
			}

			this.isVisible = true;
			this.isVisibleFromPosition = false;
			int var24 = this.posX;
			int yMin = this.posY;
			int zMin = this.posZ;
			int xMax = this.posX + this.sizeWidth;
			int yMax = this.posY + this.sizeHeight;
			int zMax = this.posZ + this.sizeDepth;

			for(int hashset = 0; hashset < 2; ++hashset) {
				this.skipRenderPass[hashset] = true;
			}

			HashSet<TileEntity> var26 = new HashSet<>();
			var26.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
			byte one = 1;
			RenderRegionCache chunkcache = new RenderRegionCache(this.worldObj, var24 - one, yMin - one, zMin - one, xMax + one, yMax + one, zMax + one, one);
			RenderBlocks renderblocks = new RenderBlocks(chunkcache);
			Tessellator tessellator = Tessellator.instance;

			for(int hashset1 = 0; hashset1 < 2; ++hashset1) {
				boolean renderNextPass = false;
				boolean hasRenderedBlocks = false;
				boolean hasGlList = false;

				for(int y = yMin; y < yMax; ++y) {
					for(int z = zMin; z < zMax; ++z) {
						for(int x = var24; x < xMax; ++x) {
							int i3 = chunkcache.getBlockId(x, y, z);
							if(i3 > 0) {
								if(!hasGlList) {
									hasGlList = true;
									GL11.glNewList(this.glRenderList + hashset1, GL11.GL_COMPILE);
									tessellator.setRenderingChunk(true);
									tessellator.startDrawingQuads();
								}

								if(hashset1 == 0 && Block.isBlockContainer[i3]) {
									TileEntity block = chunkcache.getBlockTileEntity(x, y, z);
									if(TileEntityRenderer.instance.hasSpecialRenderer(block)) {
										this.tileEntityRenderers.add(block);
									}
								}

								Block var28 = Block.blocksList[i3];
								int blockPass = var28.getRenderBlockPass();
								if(hashset1 == 0 && renderblocks.func_35927_a(x, y, z, hashset1)) {
									hasRenderedBlocks = true;
								}

								boolean canRender = true;
								if(blockPass != hashset1) {
									renderNextPass = true;
									canRender = false;
								}

								if(canRender) {
									hasRenderedBlocks |= renderblocks.renderBlockByRenderType(var28, x, y, z);
								}
							}
						}
					}
				}

				if(hasGlList) {
					tessellator.draw();
					GL11.glEndList();
					tessellator.setRenderingChunk(false);
				} else {
					hasRenderedBlocks = false;
				}

				if(hasRenderedBlocks) {
					this.skipRenderPass[hashset1] = false;
				}

				if(!renderNextPass) {
					break;
				}
			}
			
			if(skipRenderPass[0]) {
				GL11.glFlushList(glRenderList);
			}
			
			if(skipRenderPass[1]) {
				GL11.glFlushList(glRenderList + 1);
			}

			HashSet<TileEntity> var27 = new HashSet<>();
			var27.addAll(this.tileEntityRenderers);
			var27.removeAll(var26);
			this.field_1737_F.addAll(var27);
			var26.removeAll(this.tileEntityRenderers);
			this.field_1737_F.removeAll(var26);
			this.field_1747_A = Chunk.field_1540_a;
			this.isInitialized = true;
		}
	}

	public float distanceToEntity(Entity var1) {
		float var2 = (float)(var1.posX - (double)this.field_1746_q);
		float var3 = (float)(var1.posY - (double)this.field_1743_r);
		float var4 = (float)(var1.posZ - (double)this.field_1741_s);
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	public void func_1195_b() {
		for(int var1 = 0; var1 < 2; ++var1) {
			this.skipRenderPass[var1] = true;
			GL11.glFlushList(glRenderList);
			GL11.glFlushList(glRenderList + 1);
		}

		this.isInFrustrum = false;
		this.isInitialized = false;
	}

	public void func_1204_c() {
		this.func_1195_b();
		this.worldObj = null;
	}

	public int getGLCallListForPass(int var1) {
		return !this.isInFrustrum ? -1 : (!this.skipRenderPass[var1] ? this.glRenderList + var1 : -1);
	}

	public void updateInFrustrum(ICamera var1) {
		this.isInFrustrum = var1.isBoundingBoxInFrustum(this.field_1736_v);
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public boolean canRender() {
		return !this.isInitialized ? false : this.skipRenderPass[0] && this.skipRenderPass[1] && !this.needsUpdate;
	}

	public void markDirty() {
		this.needsUpdate = true;
	}
}
