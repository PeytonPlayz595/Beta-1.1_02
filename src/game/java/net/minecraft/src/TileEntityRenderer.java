package net.minecraft.src;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.opengl.GL11;

import net.peyton.eagler.minecraft.FontRenderer;

public class TileEntityRenderer {
	private Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> specialRendererMap = new IdentityHashMap<>();
	public static TileEntityRenderer instance = new TileEntityRenderer();
	private FontRenderer fontRenderer;
	public static double staticPlayerX;
	public static double staticPlayerY;
	public static double staticPlayerZ;
	public RenderEngine renderEngine;
	public World worldObj;
	public EntityPlayer entityPlayer;
	public float playerYaw;
	public float playerPitch;
	public double playerX;
	public double playerY;
	public double playerZ;

	private TileEntityRenderer() {
		this.specialRendererMap.put(TileEntitySign.class, new TileEntitySignRenderer());
		this.specialRendererMap.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
		Iterator<TileEntitySpecialRenderer> var1 = this.specialRendererMap.values().iterator();

		while(var1.hasNext()) {
			var1.next().setTileEntityRenderer(this);
		}

	}

	@SuppressWarnings("unchecked")
	public TileEntitySpecialRenderer getSpecialRendererForClass(Class<? extends TileEntity> var1) {
		TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)this.specialRendererMap.get(var1);
		if(var2 == null && var1 != TileEntity.class) {
			var2 = this.getSpecialRendererForClass((Class<? extends TileEntity>)var1.getSuperclass());
			this.specialRendererMap.put(var1, var2);
		}

		return var2;
	}

	public boolean hasSpecialRenderer(TileEntity var1) {
		return this.getSpecialRendererForEntity(var1) != null;
	}

	public TileEntitySpecialRenderer getSpecialRendererForEntity(TileEntity var1) {
		return var1 == null ? null : this.getSpecialRendererForClass(var1.getClass());
	}

	public void setRenderingContext(World var1, RenderEngine var2, FontRenderer var3, EntityPlayer var4, float var5) {
		this.worldObj = var1;
		this.renderEngine = var2;
		this.entityPlayer = var4;
		this.fontRenderer = var3;
		this.playerYaw = var4.prevRotationYaw + (var4.rotationYaw - var4.prevRotationYaw) * var5;
		this.playerPitch = var4.prevRotationPitch + (var4.rotationPitch - var4.prevRotationPitch) * var5;
		this.playerX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var5;
		this.playerY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var5;
		this.playerZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var5;
	}

	public void renderTileEntity(TileEntity var1, float var2) {
		if(var1.getDistanceFrom(this.playerX, this.playerY, this.playerZ) < 4096.0D) {
			float var3 = this.worldObj.getLightBrightness(var1.xCoord, var1.yCoord, var1.zCoord);
			GL11.glColor3f(var3, var3, var3);
			this.renderTileEntityAt(var1, (double)var1.xCoord - staticPlayerX, (double)var1.yCoord - staticPlayerY, (double)var1.zCoord - staticPlayerZ, var2);
		}

	}

	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		TileEntitySpecialRenderer var9 = this.getSpecialRendererForEntity(var1);
		if(var9 != null) {
			var9.renderTileEntityAt(var1, var2, var4, var6, var8);
		}

	}

	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
}
