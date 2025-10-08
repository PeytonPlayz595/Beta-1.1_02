package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.opengl.GL11;

import net.peyton.eagler.minecraft.FontRenderer;

public class RenderManager {
	private Map<Class<? extends Entity>, Render> entityRenderMap = new HashMap<>();
	public static RenderManager instance = new RenderManager();
	private FontRenderer field_1218_p;
	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	public RenderEngine renderEngine;
	public ItemRenderer field_4236_f;
	public World worldObj;
	public EntityPlayer field_1226_h;
	public float field_1225_i;
	public float field_1224_j;
	public GameSettings options;
	public double field_1222_l;
	public double field_1221_m;
	public double field_1220_n;

	private RenderManager() {
		this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
		this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
		this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
		this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
		this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		this.entityRenderMap.put(EntitySkeleton.class, new RenderBiped(new ModelSkeleton(), 0.5F));
		this.entityRenderMap.put(EntityZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
		this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		this.entityRenderMap.put(EntityZombieSimple.class, new RenderZombieSimple(new ModelZombie(), 0.5F, 6.0F));
		this.entityRenderMap.put(EntityGhast.class, new RenderGhast());
		this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.snowball.getIconIndex((ItemStack)null)));
		this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.egg.getIconIndex((ItemStack)null)));
		this.entityRenderMap.put(EntityFireball.class, new RenderFireball());
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
		this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
		this.entityRenderMap.put(EntityFish.class, new RenderFish());
		Iterator<Render> var1 = this.entityRenderMap.values().iterator();

		while(var1.hasNext()) {
			var1.next().setRenderManager(this);
		}

	}

	@SuppressWarnings("unchecked")
	public Render getEntityClassRenderObject(Class<? extends Entity> var1) {
		Render var2 = (Render)this.entityRenderMap.get(var1);
		if(var2 == null && var1 != Entity.class) {
			var2 = this.getEntityClassRenderObject((Class<? extends Entity>)var1.getSuperclass());
			this.entityRenderMap.put(var1, var2);
		}

		return var2;
	}

	public Render getEntityRenderObject(Entity var1) {
		return this.getEntityClassRenderObject(var1.getClass());
	}

	public void func_857_a(World var1, RenderEngine var2, FontRenderer var3, EntityPlayer var4, GameSettings var5, float var6) {
		this.worldObj = var1;
		this.renderEngine = var2;
		this.options = var5;
		this.field_1226_h = var4;
		this.field_1218_p = var3;
		this.field_1225_i = var4.prevRotationYaw + (var4.rotationYaw - var4.prevRotationYaw) * var6;
		this.field_1224_j = var4.prevRotationPitch + (var4.rotationPitch - var4.prevRotationPitch) * var6;
		this.field_1222_l = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var6;
		this.field_1221_m = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var6;
		this.field_1220_n = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var6;
	}

	public void renderEntity(Entity var1, float var2) {
		double var3 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
		double var5 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
		double var7 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;
		float var9 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var2;
		float var10 = var1.getEntityBrightness(var2);
		GL11.glColor3f(var10, var10, var10);
		this.renderEntityWithPosYaw(var1, var3 - renderPosX, var5 - renderPosY, var7 - renderPosZ, var9, var2);
	}

	public void renderEntityWithPosYaw(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		Render var10 = this.getEntityRenderObject(var1);
		if(var10 != null) {
			var10.doRender(var1, var2, var4, var6, var8, var9);
			var10.doRenderShadowAndFire(var1, var2, var4, var6, var8, var9);
		}

	}

	public void func_852_a(World var1) {
		this.worldObj = var1;
	}

	public double func_851_a(double var1, double var3, double var5) {
		double var7 = var1 - this.field_1222_l;
		double var9 = var3 - this.field_1221_m;
		double var11 = var5 - this.field_1220_n;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public FontRenderer getFontRenderer() {
		return this.field_1218_p;
	}
}
