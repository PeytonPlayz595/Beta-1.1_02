package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.Random;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.minecraft.EaglerCloudRenderer;
import net.minecraft.client.Minecraft;
import net.peyton.eagler.minecraft.LegacyMergeSort;
import net.peyton.eagler.minecraft.Tessellator;
import net.peyton.eagler.minecraft.TextureLocation;

import org.lwjgl.opengl.GL11;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class RenderGlobal implements IWorldAccess {
	public List<TileEntity> field_1458_a = new ArrayList<>();
	private World worldObj;
	private RenderEngine renderEngine;
	private ObjectArrayList<WorldRenderer> worldRenderersToUpdate = new ObjectArrayList<>();
	private WorldRenderer[] sortedWorldRenderers;
	private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int field_1440_s;
	private Minecraft mc;
	private RenderBlocks field_1438_u;
	private int field_1435_x = 0;
	private int field_1434_y;
	private int field_1433_z;
	private int field_1432_A;
	private int field_1431_B;
	private int field_1430_C;
	private int field_1429_D;
	private int field_1428_E;
	private int field_1427_F;
	private int field_1426_G;
	private int renderDistance = -1;
	private int field_1424_I = 2;
	private int field_1423_J;
	private int field_1422_K;
	private int field_1421_L;
	int[] field_1457_b = new int['\uc350'];
	private int field_1420_M;
	private int field_1419_N;
	private int field_1417_P;
	private int field_1416_Q;
	private int worldRenderersCheckIndex;
	private IntBuffer glListBuffer = GLAllocation.createDirectIntBuffer(65536);
	int dummyInt0;
	int glDummyList;
	int field_1455_d = 0;
	int field_1454_e = GLAllocation.generateDisplayLists(1);
	double field_1453_f = -9999.0D;
	double field_1452_g = -9999.0D;
	double field_1451_h = -9999.0D;
	public float field_1450_i;
	int field_1449_j = 0;
	
	double prevReposX;
	double prevReposY;
	double prevReposZ;
	
	public final EaglerCloudRenderer cloudRenderer;

	public RenderGlobal(Minecraft var1, RenderEngine var2) {
		this.mc = var1;
		this.renderEngine = var2;
		byte var3 = 64;
		this.field_1440_s = GLAllocation.generateDisplayLists(var3 * var3 * var3 * 3);
		this.field_1434_y = GLAllocation.generateDisplayLists(3);
		GL11.glPushMatrix();
		GL11.glNewList(this.field_1434_y, GL11.GL_COMPILE);
		this.func_950_f();
		GL11.glEndList();
		GL11.glPopMatrix();
		Tessellator var4 = Tessellator.instance;
		this.field_1433_z = this.field_1434_y + 1;
		GL11.glNewList(this.field_1433_z, GL11.GL_COMPILE);
		byte var6 = 64;
		int var7 = 256 / var6 + 2;
		float var5 = 16.0F;

		int var8;
		int var9;
		
		//TODO Made this a single draw call, does this break anything?
		var4.startDrawingQuads();
		for(var8 = -var6 * var7; var8 <= var6 * var7; var8 += var6) {
			for(var9 = -var6 * var7; var9 <= var6 * var7; var9 += var6) {
				var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + 0));
				var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + 0));
				var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + var6));
				var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + var6));
			}
		}
		var4.draw();

		GL11.glEndList();
		this.field_1432_A = this.field_1434_y + 2;
		GL11.glNewList(this.field_1432_A, GL11.GL_COMPILE);
		var5 = -16.0F;
		var4.startDrawingQuads();

		for(var8 = -var6 * var7; var8 <= var6 * var7; var8 += var6) {
			for(var9 = -var6 * var7; var9 <= var6 * var7; var9 += var6) {
				var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + 0));
				var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + 0));
				var4.addVertex((double)(var8 + 0), (double)var5, (double)(var9 + var6));
				var4.addVertex((double)(var8 + var6), (double)var5, (double)(var9 + var6));
			}
		}

		var4.draw();
		GL11.glEndList();
		this.cloudRenderer = new EaglerCloudRenderer(mc);
	}

	private void func_950_f() {
		Random var1 = new Random(10842L);
		Tessellator var2 = Tessellator.instance;
		var2.startDrawingQuads();

		for(int var3 = 0; var3 < 1500; ++var3) {
			double var4 = (double)(var1.nextFloat() * 2.0F - 1.0F);
			double var6 = (double)(var1.nextFloat() * 2.0F - 1.0F);
			double var8 = (double)(var1.nextFloat() * 2.0F - 1.0F);
			double var10 = (double)(0.25F + var1.nextFloat() * 0.25F);
			double var12 = var4 * var4 + var6 * var6 + var8 * var8;
			if(var12 < 1.0D && var12 > 0.01D) {
				var12 = 1.0D / Math.sqrt(var12);
				var4 *= var12;
				var6 *= var12;
				var8 *= var12;
				double var14 = var4 * 100.0D;
				double var16 = var6 * 100.0D;
				double var18 = var8 * 100.0D;
				double var20 = Math.atan2(var4, var8);
				double var22 = Math.sin(var20);
				double var24 = Math.cos(var20);
				double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
				double var28 = Math.sin(var26);
				double var30 = Math.cos(var26);
				double var32 = var1.nextDouble() * Math.PI * 2.0D;
				double var34 = Math.sin(var32);
				double var36 = Math.cos(var32);

				for(int var38 = 0; var38 < 4; ++var38) {
					double var39 = 0.0D;
					double var41 = (double)((var38 & 2) - 1) * var10;
					double var43 = (double)((var38 + 1 & 2) - 1) * var10;
					double var47 = var41 * var36 - var43 * var34;
					double var49 = var43 * var36 + var41 * var34;
					double var53 = var47 * var28 + var39 * var30;
					double var55 = var39 * var28 - var47 * var30;
					double var57 = var55 * var22 - var49 * var24;
					double var61 = var49 * var22 + var55 * var24;
					var2.addVertex(var14 + var57, var16 + var53, var18 + var61);
				}
			}
		}

		var2.draw();
	}

	public void func_946_a(World var1) {
		if(this.worldObj != null) {
			this.worldObj.removeWorldAccess(this);
		}

		this.field_1453_f = -9999.0D;
		this.field_1452_g = -9999.0D;
		this.field_1451_h = -9999.0D;
		RenderManager.instance.func_852_a(var1);
		this.worldObj = var1;
		this.field_1438_u = new RenderBlocks(var1);
		if(var1 != null) {
			var1.addWorldAccess(this);
			this.loadRenderers();
		}

	}

	public void loadRenderers() {
		Block.leaves.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
		this.renderDistance = this.mc.gameSettings.renderDistance;
		int var1;
		if(this.worldRenderers != null) {
			for(var1 = 0; var1 < this.worldRenderers.length; ++var1) {
				this.worldRenderers[var1].func_1204_c();
			}
		}

		var1 = 64 << 3 - this.renderDistance;
		if(var1 > 400) {
			var1 = 400;
		}

		this.prevReposX = -9999.0D;
		this.prevReposY = -9999.0D;
		this.prevReposZ = -9999.0D;
		this.renderChunksWide = var1 / 16 + 1;
		this.renderChunksTall = 8;
		this.renderChunksDeep = var1 / 16 + 1;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		int var2 = 0;
		int var3 = 0;
		this.field_1431_B = 0;
		this.field_1430_C = 0;
		this.field_1429_D = 0;
		this.field_1428_E = this.renderChunksWide;
		this.field_1427_F = this.renderChunksTall;
		this.field_1426_G = this.renderChunksDeep;

		int var4;
		int var8;
		for(var4 = 0, var8 = this.worldRenderersToUpdate.size(); var4 < var8; ++var4) {
			WorldRenderer wr = ((WorldRenderer)this.worldRenderersToUpdate.get(var4));
			if(wr != null) {
				wr.needsUpdate = false;
			}
		}

		this.worldRenderersToUpdate.clear();
		this.field_1458_a.clear();

		for(var4 = 0; var4 < this.renderChunksWide; ++var4) {
			for(int var5 = 0; var5 < this.renderChunksTall; ++var5) {
				for(int var6 = 0; var6 < this.renderChunksDeep; ++var6) {
					int index = (var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4;
					WorldRenderer wr = (this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4] = new WorldRenderer(this.worldObj, this.field_1458_a, var4 * 16, var5 * 16, var6 * 16, 16, this.field_1440_s + var2));
					wr.isWaitingOnOcclusionQuery = false;
					wr.isVisible = true;
					wr.isInFrustrum = true;
					wr.field_1735_w = var3++;
					wr.markDirty();
					this.sortedWorldRenderers[index] = wr;
					this.worldRenderersToUpdate.add(wr);
					var2 += 3;
				}
			}
		}

		if(this.worldObj != null) {
			EntityPlayerSP var7 = this.mc.thePlayer;
			if(var7 != null) {
				this.func_956_b(MathHelper.floor_double(var7.posX), MathHelper.floor_double(var7.posY), MathHelper.floor_double(var7.posZ));
				LegacyMergeSort.sort(this.sortedWorldRenderers, EntitySorter.instance.setEntity(var7));
			}
		}

		this.field_1424_I = 2;
	}

	public void func_951_a(Vec3D var1, ICamera var2, float var3) {
		if(this.field_1424_I > 0) {
			--this.field_1424_I;
		} else {
			TileEntityRenderer.instance.setRenderingContext(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, var3);
			RenderManager.instance.func_857_a(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.thePlayer, this.mc.gameSettings, var3);
			this.field_1423_J = 0;
			this.field_1422_K = 0;
			this.field_1421_L = 0;
			EntityPlayerSP var4 = this.mc.thePlayer;
			RenderManager.renderPosX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var3;
			RenderManager.renderPosY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var3;
			RenderManager.renderPosZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var3;
			TileEntityRenderer.staticPlayerX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var3;
			TileEntityRenderer.staticPlayerY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var3;
			TileEntityRenderer.staticPlayerZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var3;
			List<Entity> var5 = this.worldObj.func_658_i();
			this.field_1423_J = var5.size();

			int var6;
			for(var6 = 0; var6 < var5.size(); ++var6) {
				Entity var7 = (Entity)var5.get(var6);
				if(var7.isInRangeToRenderVec3D(var1) && var2.isBoundingBoxInFrustum(var7.boundingBox) && (var7 != this.mc.thePlayer || this.mc.gameSettings.thirdPersonView)) {
					++this.field_1422_K;
					RenderManager.instance.renderEntity(var7, var3);
				}
			}

			for(var6 = 0; var6 < this.field_1458_a.size(); ++var6) {
				TileEntityRenderer.instance.renderTileEntity((TileEntity)this.field_1458_a.get(var6), var3);
			}

		}
	}

	public String func_953_b() {
		return "C: " + this.field_1417_P + "/" + this.field_1420_M + ". F: " + this.field_1419_N + ", E: " + this.field_1416_Q;
	}

	public String func_957_c() {
		return "E: " + this.field_1422_K + "/" + this.field_1423_J + ". B: " + this.field_1421_L + ", I: " + (this.field_1423_J - this.field_1421_L - this.field_1422_K);
	}

	private void func_956_b(int var1, int var2, int var3) {
		var1 -= 8;
		var2 -= 8;
		var3 -= 8;
		this.field_1431_B = Integer.MAX_VALUE;
		this.field_1430_C = Integer.MAX_VALUE;
		this.field_1429_D = Integer.MAX_VALUE;
		this.field_1428_E = Integer.MIN_VALUE;
		this.field_1427_F = Integer.MIN_VALUE;
		this.field_1426_G = Integer.MIN_VALUE;
		int var4 = this.renderChunksWide * 16;
		int var5 = var4 / 2;

		for(int var6 = 0; var6 < this.renderChunksWide; ++var6) {
			int var7 = var6 * 16;
			int var8 = var7 + var5 - var1;
			if(var8 < 0) {
				var8 -= var4 - 1;
			}

			var8 /= var4;
			var7 -= var8 * var4;
			if(var7 < this.field_1431_B) {
				this.field_1431_B = var7;
			}

			if(var7 > this.field_1428_E) {
				this.field_1428_E = var7;
			}

			for(int var9 = 0; var9 < this.renderChunksDeep; ++var9) {
				int var10 = var9 * 16;
				int var11 = var10 + var5 - var3;
				if(var11 < 0) {
					var11 -= var4 - 1;
				}

				var11 /= var4;
				var10 -= var11 * var4;
				if(var10 < this.field_1429_D) {
					this.field_1429_D = var10;
				}

				if(var10 > this.field_1426_G) {
					this.field_1426_G = var10;
				}

				for(int var12 = 0; var12 < this.renderChunksTall; ++var12) {
					int var13 = var12 * 16;
					if(var13 < this.field_1430_C) {
						this.field_1430_C = var13;
					}

					if(var13 > this.field_1427_F) {
						this.field_1427_F = var13;
					}

					WorldRenderer var14 = this.worldRenderers[(var9 * this.renderChunksTall + var12) * this.renderChunksWide + var6];
					boolean var15 = var14.needsUpdate;
					var14.func_1197_a(var7, var13, var10);
					if(!var15 && var14.needsUpdate) {
						this.worldRenderersToUpdate.add(var14);
					}
				}
			}
		}

	}

	public int func_943_a(EntityPlayer var1, int var2, double var3) {
		if(this.worldRenderersToUpdate.size() < 10) {
			byte partialX = 10;

			for(int i = 0; i < partialX; ++i) {
				this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
				WorldRenderer partialY = this.worldRenderers[this.worldRenderersCheckIndex];
				if(partialY.needsUpdate && !this.worldRenderersToUpdate.contains(partialY)) {
					this.worldRenderersToUpdate.add(partialY);
				}
			}
		}
		
		if(this.mc.gameSettings.renderDistance != this.renderDistance) {
			this.loadRenderers();
		}

		if(var2 == 0) {
			this.field_1420_M = 0;
			this.field_1419_N = 0;
			this.field_1417_P = 0;
			this.field_1416_Q = 0;
		}

		double var11 = var1.posX - this.field_1453_f;
		double var13 = var1.posY - this.field_1452_g;
		double var15 = var1.posZ - this.field_1451_h;
		if(var11 * var11 + var13 * var13 + var15 * var15 > 16.0D) {
			this.field_1453_f = var1.posX;
			this.field_1452_g = var1.posY;
			this.field_1451_h = var1.posZ;
			double ocReq = var1.posX - this.prevReposX;
			double lastIndex = var1.posY - this.prevReposY;
			double stepNum = var1.posZ - this.prevReposZ;
			double switchStep = ocReq * ocReq + lastIndex * lastIndex + stepNum * stepNum;
			int num = 0;
			if(switchStep > (double)(num * num) + 16.0D) {
				this.prevReposX = var1.posX;
				this.prevReposY = var1.posY;
				this.prevReposZ = var1.posZ;
				this.func_956_b(MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ));
			}
			
			LegacyMergeSort.sort(this.sortedWorldRenderers, EntitySorter.instance.setEntity(var1));
		}

		return this.func_952_a(0, this.sortedWorldRenderers.length, var2, var3);
	}

	private int func_952_a(int var1, int var2, int var3, double var4) {
		this.glListBuffer.clear();
		int var6 = 0;

		for(int var7 = var1; var7 < var2; ++var7) {
			if(var3 == 0) {
				++this.field_1420_M;
				if(this.sortedWorldRenderers[var7].skipRenderPass[var3]) {
					++this.field_1416_Q;
				} else if(!this.sortedWorldRenderers[var7].isInFrustrum) {
					++this.field_1419_N;
				} else {
					++this.field_1417_P;
				}
			}

			if(!this.sortedWorldRenderers[var7].skipRenderPass[var3] && this.sortedWorldRenderers[var7].isInFrustrum && this.sortedWorldRenderers[var7].isVisible) {
				int var8 = this.sortedWorldRenderers[var7].getGLCallListForPass(var3);
				if(var8 >= 0) {
					int partialX = this.sortedWorldRenderers[var7].getGLCallListForPass(var3);
					if(partialX >= 0) {
						this.glListBuffer.put(partialX);
						++var6;
					}
				}
			}
		}

		this.glListBuffer.flip();
		EntityPlayerSP var19 = this.mc.thePlayer;
		double var20 = var19.lastTickPosX + (var19.posX - var19.lastTickPosX) * var4;
		double var10 = var19.lastTickPosY + (var19.posY - var19.lastTickPosY) * var4;
		double var12 = var19.lastTickPosZ + (var19.posZ - var19.lastTickPosZ) * var4;
		//this.mc.entityRenderer.enableLightmap(var4);
		
		//TODO: Idk what this is for
		GL11.glTranslatef((float)(-var20), (float)(-var10), (float)(-var12));
		GL11.glCallLists(this.glListBuffer);
		GL11.glTranslatef((float)var20, (float)var10, (float)var12);
		
		//this.mc.entityRenderer.disableLightmap(var4);

		return var6;
	}

	public void func_945_d() {
		++this.field_1435_x;
	}

	public void func_4142_a(float var1) {
		if(!this.mc.theWorld.worldProvider.field_4220_c) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			Vec3D var2 = this.worldObj.func_4079_a(this.mc.thePlayer, var1);
			float var3 = (float)var2.xCoord;
			float var4 = (float)var2.yCoord;
			float var5 = (float)var2.zCoord;
			float var7;
			float var8;
			if(this.mc.gameSettings.anaglyph) {
				float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
				var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
				var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
				var3 = var6;
				var4 = var7;
				var5 = var8;
			}

			GL11.glColor3f(var3, var4, var5);
			Tessellator var14 = Tessellator.instance;
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glColor3f(var3, var4, var5);
			GL11.glCallList(this.field_1433_z);
			GL11.glDisable(GL11.GL_FOG);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			float[] var15 = this.worldObj.worldProvider.func_4097_b(this.worldObj.getCelestialAngle(var1), var1);
			float var11;
			if(var15 != null) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glPushMatrix();
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				var8 = this.worldObj.getCelestialAngle(var1);
				GL11.glRotatef(var8 > 0.5F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
				var14.startDrawing(6);
				var14.setColorRGBA_F(var15[0], var15[1], var15[2], var15[3]);
				var14.addVertex(0.0D, 100.0D, 0.0D);
				byte var9 = 16;
				var14.setColorRGBA_F(var15[0], var15[1], var15[2], 0.0F);

				for(int var10 = 0; var10 <= var9; ++var10) {
					var11 = (float)var10 * (float)Math.PI * 2.0F / (float)var9;
					float var12 = MathHelper.sin(var11);
					float var13 = MathHelper.cos(var11);
					var14.addVertex((double)(var12 * 120.0F), (double)(var13 * 120.0F), (double)(-var13 * 40.0F * var15[3]));
				}

				var14.draw();
				GL11.glPopMatrix();
				GL11.glShadeModel(GL11.GL_FLAT);
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glPushMatrix();
			var7 = 0.0F;
			var8 = 0.0F;
			float var16 = 0.0F;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(var7, var8, var16);
			GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(this.worldObj.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
			float var17 = 30.0F;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
			var14.startDrawingQuads();
			var14.addVertexWithUV((double)(-var17), 100.0D, (double)(-var17), 0.0D, 0.0D);
			var14.addVertexWithUV((double)var17, 100.0D, (double)(-var17), 1.0D, 0.0D);
			var14.addVertexWithUV((double)var17, 100.0D, (double)var17, 1.0D, 1.0D);
			var14.addVertexWithUV((double)(-var17), 100.0D, (double)var17, 0.0D, 1.0D);
			var14.draw();
			var17 = 20.0F;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
			var14.startDrawingQuads();
			var14.addVertexWithUV((double)(-var17), -100.0D, (double)var17, 1.0D, 1.0D);
			var14.addVertexWithUV((double)var17, -100.0D, (double)var17, 0.0D, 1.0D);
			var14.addVertexWithUV((double)var17, -100.0D, (double)(-var17), 0.0D, 0.0D);
			var14.addVertexWithUV((double)(-var17), -100.0D, (double)(-var17), 1.0D, 0.0D);
			var14.draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			var11 = this.worldObj.func_679_f(var1);
			if(var11 > 0.0F) {
				GL11.glColor4f(var11, var11, var11, var11);
				GL11.glCallList(this.field_1434_y);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glPopMatrix();
			GL11.glColor3f(var3 * 0.2F + 0.04F, var4 * 0.2F + 0.04F, var5 * 0.6F + 0.1F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glCallList(this.field_1432_A);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
		}
	}

	public boolean updateRenderers(EntityPlayer var1, boolean var2) {
		RenderSorter var4 = RenderSorter.instance.setPlayer(var1);
		ArrayList<WorldRenderer> var6 = null;
		int var7 = this.worldRenderersToUpdate.size();
		int var9;
		WorldRenderer var10;
		ObjectArrayList<WorldRenderer> laterUpdateList = new ObjectArrayList<>(var7);
		for (var9 = 0; var9 < var7; ++var9) {
			var10 = this.worldRenderersToUpdate.get(var9);
			
			if (var10 != null) {
				if (!var10.isInFrustrum || !var10.isVisible) {
					laterUpdateList.add(var10);
				} else {
					if (var6 == null) {
						var6 = new ArrayList<>();
					}
					
					var6.add(var10);
				}
			}
		}
		
		this.worldRenderersToUpdate = laterUpdateList;
		
		int updates = 0;
		if (var6 != null) {
			int size = var6.size();
			if (size > 1) {
				var6.sort(var4);
			}
			
			for (var9 = size - 1; var9 >= 0; --var9) {
				var10 = var6.get(var9);
				if(updates >= 1) {
					this.worldRenderersToUpdate.add(var10);
				} else {
					var10.updateRenderer();
					var10.needsUpdate = false;
					if(!var10.canRender()) {
						++updates;
					}
				}
			}
		}
		
		return true;
	}
	
	public void func_959_a(EntityPlayer var1, MovingObjectPosition var2, int var3, ItemStack var4, float var5) {
		Tessellator var6 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)EagRuntime.steadyTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
		int var8;
		if(var3 == 0) {
			if(this.field_1450_i > 0.0F) {
				GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
				TextureLocation.terrain.bindTexture();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
				GL11.glPushMatrix();
				var8 = this.worldObj.getBlockId(var2.blockX, var2.blockY, var2.blockZ);
				Block var9 = var8 > 0 ? Block.blocksList[var8] : null;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glPolygonOffset(-3.0F, -3.0F);
				GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
				var6.startDrawingQuads();
				double var10 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var5;
				double var12 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var5;
				double var14 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var5;
				var6.setTranslationD(-var10, -var12, -var14);
				var6.disableColor();
				if(var9 == null) {
					var9 = Block.stone;
				}

				this.field_1438_u.renderBlockUsingTexture(var9, var2.blockX, var2.blockY, var2.blockZ, 240 + (int)(this.field_1450_i * 10.0F));
				var6.draw();
				var6.setTranslationD(0.0D, 0.0D, 0.0D);
				GL11.glPolygonOffset(0.0F, 0.0F);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glDepthMask(true);
				GL11.glPopMatrix();
			}
		} else if(var4 != null) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float var16 = MathHelper.sin((float)EagRuntime.steadyTimeMillis() / 100.0F) * 0.2F + 0.8F;
			GL11.glColor4f(var16, var16, var16, MathHelper.sin((float)EagRuntime.steadyTimeMillis() / 200.0F) * 0.2F + 0.5F);
			TextureLocation.terrain.bindTexture();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public void drawSelectionBox(EntityPlayer var1, MovingObjectPosition var2, int var3, ItemStack var4, float var5) {
		if(var3 == 0 && var2.typeOfHit == 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
			GL11.glLineWidth(2.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);
			float var6 = 0.002F;
			int var7 = this.worldObj.getBlockId(var2.blockX, var2.blockY, var2.blockZ);
			if(var7 > 0) {
				Block.blocksList[var7].setBlockBoundsBasedOnState(this.worldObj, var2.blockX, var2.blockY, var2.blockZ);
				double var8 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var5;
				double var10 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var5;
				double var12 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var5;
				this.drawOutlinedBoundingBox(Block.blocksList[var7].getSelectedBoundingBoxFromPool(this.worldObj, var2.blockX, var2.blockY, var2.blockZ).expand((double)var6, (double)var6, (double)var6).getOffsetBoundingBox(-var8, -var10, -var12));
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}

	}

	private void drawOutlinedBoundingBox(AxisAlignedBB var1) {
		Tessellator var2 = Tessellator.instance;
		var2.startDrawing(3);
		var2.addVertex(var1.minX, var1.minY, var1.minZ);
		var2.addVertex(var1.maxX, var1.minY, var1.minZ);
		var2.addVertex(var1.maxX, var1.minY, var1.maxZ);
		var2.addVertex(var1.minX, var1.minY, var1.maxZ);
		var2.addVertex(var1.minX, var1.minY, var1.minZ);
		var2.draw();
		var2.startDrawing(3);
		var2.addVertex(var1.minX, var1.maxY, var1.minZ);
		var2.addVertex(var1.maxX, var1.maxY, var1.minZ);
		var2.addVertex(var1.maxX, var1.maxY, var1.maxZ);
		var2.addVertex(var1.minX, var1.maxY, var1.maxZ);
		var2.addVertex(var1.minX, var1.maxY, var1.minZ);
		var2.draw();
		var2.startDrawing(1);
		var2.addVertex(var1.minX, var1.minY, var1.minZ);
		var2.addVertex(var1.minX, var1.maxY, var1.minZ);
		var2.addVertex(var1.maxX, var1.minY, var1.minZ);
		var2.addVertex(var1.maxX, var1.maxY, var1.minZ);
		var2.addVertex(var1.maxX, var1.minY, var1.maxZ);
		var2.addVertex(var1.maxX, var1.maxY, var1.maxZ);
		var2.addVertex(var1.minX, var1.minY, var1.maxZ);
		var2.addVertex(var1.minX, var1.maxY, var1.maxZ);
		var2.draw();
	}

	public void func_949_a(int var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = MathHelper.bucketInt(var1, 16);
		int var8 = MathHelper.bucketInt(var2, 16);
		int var9 = MathHelper.bucketInt(var3, 16);
		int var10 = MathHelper.bucketInt(var4, 16);
		int var11 = MathHelper.bucketInt(var5, 16);
		int var12 = MathHelper.bucketInt(var6, 16);

		for(int var13 = var7; var13 <= var10; ++var13) {
			int var14 = var13 % this.renderChunksWide;
			if(var14 < 0) {
				var14 += this.renderChunksWide;
			}

			for(int var15 = var8; var15 <= var11; ++var15) {
				int var16 = var15 % this.renderChunksTall;
				if(var16 < 0) {
					var16 += this.renderChunksTall;
				}

				for(int var17 = var9; var17 <= var12; ++var17) {
					int var18 = var17 % this.renderChunksDeep;
					if(var18 < 0) {
						var18 += this.renderChunksDeep;
					}

					int var19 = (var18 * this.renderChunksTall + var16) * this.renderChunksWide + var14;
					WorldRenderer var20 = this.worldRenderers[var19];
					if(!var20.needsUpdate) {
						this.worldRenderersToUpdate.add(var20);
					}

					var20.markDirty();
				}
			}
		}

	}

	public void func_934_a(int var1, int var2, int var3) {
		this.func_949_a(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
	}

	public void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
		this.func_949_a(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
	}

	public void func_960_a(ICamera var1, float var2) {
		for(int var3 = 0; var3 < this.worldRenderers.length; ++var3) {
			if(!this.worldRenderers[var3].canRender() && (!this.worldRenderers[var3].isInFrustrum || (var3 + this.field_1449_j & 15) == 0)) {
				this.worldRenderers[var3].updateInFrustrum(var1);
			}
		}

		++this.field_1449_j;
	}

	public void playRecord(String var1, int var2, int var3, int var4) {
		if(var1 != null) {
			this.mc.ingameGUI.func_553_b("C418 - " + var1);
		}

		this.mc.sndManager.func_331_a(var1, (float)var2, (float)var3, (float)var4, 1.0F, 1.0F);
	}

	public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {
		float var10 = 16.0F;
		if(var8 > 1.0F) {
			var10 *= var8;
		}

		if(this.mc.thePlayer.getDistanceSq(var2, var4, var6) < (double)(var10 * var10)) {
			this.mc.sndManager.playSound(var1, (float)var2, (float)var4, (float)var6, var8, var9);
		}

	}

	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		double var14 = this.mc.thePlayer.posX - var2;
		double var16 = this.mc.thePlayer.posY - var4;
		double var18 = this.mc.thePlayer.posZ - var6;
		if(var14 * var14 + var16 * var16 + var18 * var18 <= 256.0D) {
			if(var1 == "bubble") {
				this.mc.effectRenderer.func_1192_a(new EntityBubbleFX(this.worldObj, var2, var4, var6, var8, var10, var12));
			} else if(var1 == "smoke") {
				this.mc.effectRenderer.func_1192_a(new EntitySmokeFX(this.worldObj, var2, var4, var6));
			} else if(var1 == "portal") {
				this.mc.effectRenderer.func_1192_a(new EntityPortalFX(this.worldObj, var2, var4, var6, var8, var10, var12));
			} else if(var1 == "explode") {
				this.mc.effectRenderer.func_1192_a(new EntityExplodeFX(this.worldObj, var2, var4, var6, var8, var10, var12));
			} else if(var1 == "flame") {
				this.mc.effectRenderer.func_1192_a(new EntityFlameFX(this.worldObj, var2, var4, var6, var8, var10, var12));
			} else if(var1 == "lava") {
				this.mc.effectRenderer.func_1192_a(new EntityLavaFX(this.worldObj, var2, var4, var6));
			} else if(var1 == "splash") {
				this.mc.effectRenderer.func_1192_a(new EntitySplashFX(this.worldObj, var2, var4, var6, var8, var10, var12));
			} else if(var1 == "largesmoke") {
				this.mc.effectRenderer.func_1192_a(new EntitySmokeFX(this.worldObj, var2, var4, var6, 2.5F));
			} else if(var1 == "reddust") {
				this.mc.effectRenderer.func_1192_a(new EntityReddustFX(this.worldObj, var2, var4, var6));
			} else if(var1 == "snowballpoof") {
				this.mc.effectRenderer.func_1192_a(new EntitySlimeFX(this.worldObj, var2, var4, var6, Item.snowball));
			} else if(var1 == "slime") {
				this.mc.effectRenderer.func_1192_a(new EntitySlimeFX(this.worldObj, var2, var4, var6, Item.slimeBall));
			}

		}
	}

	public void obtainEntitySkin(Entity var1) {
	}

	public void releaseEntitySkin(Entity var1) {
	}

	public void updateAllRenderers() {
		for(int var1 = 0; var1 < this.worldRenderers.length; ++var1) {
			if(this.worldRenderers[var1].field_1747_A) {
				if(!this.worldRenderers[var1].needsUpdate) {
					this.worldRenderersToUpdate.add(this.worldRenderers[var1]);
				}

				this.worldRenderers[var1].markDirty();
			}
		}

	}

	public void func_935_a(int var1, int var2, int var3, TileEntity var4) {
	}
	
	public double getCloudCounter(float partialTicks) {
		return (double) this.field_1435_x + partialTicks;
	}
}
