package net.minecraft.src;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntityGhast extends EntityFlying implements IMobs {
	public int field_4121_a = 0;
	public double field_4120_b;
	public double field_4127_c;
	public double field_4126_d;
	private Entity field_4123_g = null;
	private int field_4122_h = 0;
	public int field_4125_e = 0;
	public int field_4124_f = 0;
	
	private static final TextureLocation ghast = new TextureLocation("/mob/ghast.png");
	private static final TextureLocation ghastFire = new TextureLocation("/mob/ghast_fire.png");

	public EntityGhast(World var1) {
		super(var1);
		this.texture = ghast;
		this.setSize(4.0F, 4.0F);
		this.isImmuneToFire = true;
	}

	protected void updatePlayerActionState() {
		if(this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

		this.field_4125_e = this.field_4124_f;
		double var1 = this.field_4120_b - this.posX;
		double var3 = this.field_4127_c - this.posY;
		double var5 = this.field_4126_d - this.posZ;
		double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		if(var7 < 1.0D || var7 > 60.0D) {
			this.field_4120_b = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.field_4127_c = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.field_4126_d = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if(this.field_4121_a-- <= 0) {
			this.field_4121_a += this.rand.nextInt(5) + 2;
			if(this.func_4050_a(this.field_4120_b, this.field_4127_c, this.field_4126_d, var7)) {
				this.motionX += var1 / var7 * 0.1D;
				this.motionY += var3 / var7 * 0.1D;
				this.motionZ += var5 / var7 * 0.1D;
			} else {
				this.field_4120_b = this.posX;
				this.field_4127_c = this.posY;
				this.field_4126_d = this.posZ;
			}
		}

		if(this.field_4123_g != null && this.field_4123_g.isDead) {
			this.field_4123_g = null;
		}

		if(this.field_4123_g == null || this.field_4122_h-- <= 0) {
			this.field_4123_g = this.worldObj.getClosestPlayerToEntity(this, 100.0D);
			if(this.field_4123_g != null) {
				this.field_4122_h = 20;
			}
		}

		double var9 = 64.0D;
		if(this.field_4123_g != null && this.field_4123_g.getDistanceSqToEntity(this) < var9 * var9) {
			double var11 = this.field_4123_g.posX - this.posX;
			double var13 = this.field_4123_g.boundingBox.minY + (double)(this.field_4123_g.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
			double var15 = this.field_4123_g.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(var11, var15)) * 180.0F / (float)Math.PI;
			if(this.canEntityBeSeen(this.field_4123_g)) {
				if(this.field_4124_f == 10) {
					this.worldObj.playSoundAtEntity(this, "mob.ghast.charge", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				}

				++this.field_4124_f;
				if(this.field_4124_f == 20) {
					this.worldObj.playSoundAtEntity(this, "mob.ghast.fireball", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					EntityFireball var17 = new EntityFireball(this.worldObj, this, var11, var13, var15);
					double var18 = 4.0D;
					Vec3D var20 = this.getLook(1.0F);
					var17.posX = this.posX + var20.xCoord * var18;
					var17.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
					var17.posZ = this.posZ + var20.zCoord * var18;
					this.worldObj.entityJoinedWorld(var17);
					this.field_4124_f = -40;
				}
			} else if(this.field_4124_f > 0) {
				--this.field_4124_f;
			}
		} else {
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI;
			if(this.field_4124_f > 0) {
				--this.field_4124_f;
			}
		}

		this.texture = this.field_4124_f > 10 ? ghastFire : ghast;
	}

	private boolean func_4050_a(double var1, double var3, double var5, double var7) {
		double var9 = (this.field_4120_b - this.posX) / var7;
		double var11 = (this.field_4127_c - this.posY) / var7;
		double var13 = (this.field_4126_d - this.posZ) / var7;
		AxisAlignedBB var15 = this.boundingBox.copy();

		for(int var16 = 1; (double)var16 < var7; ++var16) {
			var15.offset(var9, var11, var13);
			if(this.worldObj.getCollidingBoundingBoxes(this, var15).size() > 0) {
				return false;
			}
		}

		return true;
	}

	protected String getLivingSound() {
		return "mob.ghast.moan";
	}

	protected String getHurtSound() {
		return "mob.ghast.scream";
	}

	protected String getDeathSound() {
		return "mob.ghast.death";
	}

	protected int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}

	protected float getSoundVolume() {
		return 10.0F;
	}

	public boolean getCanSpawnHere() {
		return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.difficultySetting > 0;
	}

	public int getMaxSpawnedInChunk() {
		return 1;
	}
}
