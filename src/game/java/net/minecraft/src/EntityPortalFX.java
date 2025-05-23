package net.minecraft.src;

import net.peyton.eagler.minecraft.Tessellator;

public class EntityPortalFX extends EntityFX {
	private float field_4083_a;
	private double field_4086_p;
	private double field_4085_q;
	private double field_4084_r;

	public EntityPortalFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6, var8, var10, var12);
		this.motionX = var8;
		this.motionY = var10;
		this.motionZ = var12;
		this.field_4086_p = this.posX = var2;
		this.field_4085_q = this.posY = var4;
		this.field_4084_r = this.posZ = var6;
		float var14 = this.rand.nextFloat() * 0.6F + 0.4F;
		this.field_4083_a = this.field_665_g = this.rand.nextFloat() * 0.2F + 0.5F;
		this.particleRed = this.particleBlue = this.particleGreen = 1.0F * var14;
		this.particleBlue *= 0.3F;
		this.particleRed *= 0.9F;
		this.field_666_f = (int)(Math.random() * 10.0D) + 40;
		this.noClip = true;
		this.field_670_b = (int)(Math.random() * 8.0D);
	}

	public void func_406_a(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)this.e + var2) / (float)this.field_666_f;
		var8 = 1.0F - var8;
		var8 *= var8;
		var8 = 1.0F - var8;
		this.field_665_g = this.field_4083_a * var8;
		super.func_406_a(var1, var2, var3, var4, var5, var6, var7);
	}

	public float getEntityBrightness(float var1) {
		float var2 = super.getEntityBrightness(var1);
		float var3 = (float)this.e / (float)this.field_666_f;
		var3 *= var3;
		var3 *= var3;
		return var2 * (1.0F - var3) + var3;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		float var1 = (float)this.e / (float)this.field_666_f;
		float var2 = var1;
		var1 = -var1 + var1 * var1 * 2.0F;
		var1 = 1.0F - var1;
		this.posX = this.field_4086_p + this.motionX * (double)var1;
		this.posY = this.field_4085_q + this.motionY * (double)var1 + (double)(1.0F - var2);
		this.posZ = this.field_4084_r + this.motionZ * (double)var1;
		if(this.e++ >= this.field_666_f) {
			this.setEntityDead();
		}

	}
}
