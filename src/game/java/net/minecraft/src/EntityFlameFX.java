package net.minecraft.src;

import net.peyton.eagler.minecraft.Tessellator;

public class EntityFlameFX extends EntityFX {
	private float field_672_a;

	public EntityFlameFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6, var8, var10, var12);
		this.motionX = this.motionX * (double)0.01F + var8;
		this.motionY = this.motionY * (double)0.01F + var10;
		this.motionZ = this.motionZ * (double)0.01F + var12;
		this.field_672_a = this.field_665_g;
		this.particleRed = this.particleBlue = this.particleGreen = 1.0F;
		this.field_666_f = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.noClip = true;
		this.field_670_b = 48;
	}

	public void func_406_a(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)this.e + var2) / (float)this.field_666_f;
		this.field_665_g = this.field_672_a * (1.0F - var8 * var8 * 0.5F);
		super.func_406_a(var1, var2, var3, var4, var5, var6, var7);
	}

	public float getEntityBrightness(float var1) {
		float var2 = ((float)this.e + var1) / (float)this.field_666_f;
		if(var2 < 0.0F) {
			var2 = 0.0F;
		}

		if(var2 > 1.0F) {
			var2 = 1.0F;
		}

		float var3 = super.getEntityBrightness(var1);
		return var3 * var2 + (1.0F - var2);
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.e++ >= this.field_666_f) {
			this.setEntityDead();
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.96F;
		this.motionY *= (double)0.96F;
		this.motionZ *= (double)0.96F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}
}
