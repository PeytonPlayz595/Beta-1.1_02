package net.minecraft.src;

import java.util.List;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntityLiving extends Entity {
	public int field_9366_o = 20;
	public float field_9365_p;
	public float field_9364_q;
	public float field_9363_r;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	protected float field_9362_u;
	protected float field_9361_v;
	protected float field_9360_w;
	protected float field_9359_x;
	protected boolean field_9358_y = true;
	private static final TextureLocation charTex = new TextureLocation("/mob/char.png");
	protected TextureLocation texture = charTex;
	protected boolean unusedEntityLivingBoolean = true;
	protected float field_9353_B = 0.0F;
	protected String field_9351_C = null;
	protected float field_9349_D = 1.0F;
	protected int field_9347_E = 0;
	protected float field_9345_F = 0.0F;
	public boolean field_9343_G = false;
	public float prevSwingProgress;
	public float swingProgress;
	public int health = 10;
	public int prevHealth;
	private int a;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float field_9329_Q;
	public float field_9328_R;
	protected boolean field_9327_S = false;
	public int field_9326_T = -1;
	public float field_9325_U = (float)(Math.random() * (double)0.9F + (double)0.1F);
	public float field_705_Q;
	public float field_704_R;
	public float field_703_S;
	protected int field_9324_Y;
	protected double field_9323_Z;
	protected double field_9356_aa;
	protected double field_9354_ab;
	protected double field_9352_ac;
	protected double field_9350_ad;
	float field_9348_ae = 0.0F;
	protected int field_9346_af = 0;
	protected int field_9344_ag = 0;
	protected float moveStrafing;
	protected float moveForward;
	protected float randomYawVelocity;
	protected boolean isJumping = false;
	protected float defaultPitch = 0.0F;
	protected float moveSpeed = 0.7F;
	private Entity b;
	private int c = 0;

	public EntityLiving(World var1) {
		super(var1);
		this.preventEntitySpawning = true;
		this.field_9363_r = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_9365_p = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.field_9364_q = 1.0F;
		this.stepHeight = 0.5F;
	}

	public boolean canEntityBeSeen(Entity var1) {
		return this.worldObj.rayTraceBlocks(Vec3D.createVector(this.posX, this.posY + (double)this.func_373_s(), this.posZ), Vec3D.createVector(var1.posX, var1.posY + (double)var1.func_373_s(), var1.posZ)) == null;
	}

	public TextureLocation getEntityTexture() {
		return this.texture;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public boolean canBePushed() {
		return !this.isDead;
	}

	public float func_373_s() {
		return this.height * 0.85F;
	}

	public int func_421_b() {
		return 80;
	}

	public void func_391_y() {
		this.prevSwingProgress = this.swingProgress;
		super.func_391_y();
		if(this.rand.nextInt(1000) < this.a++) {
			this.a = -this.func_421_b();
			String var1 = this.getLivingSound();
			if(var1 != null) {
				this.worldObj.playSoundAtEntity(this, var1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}

		if(this.isEntityAlive() && this.func_345_I()) {
			this.attackEntityFrom((Entity)null, 1);
		}

		if(this.isImmuneToFire || this.worldObj.multiplayerWorld) {
			this.fire = 0;
		}

		int var8;
		if(this.isEntityAlive() && this.isInsideOfMaterial(Material.water)) {
			--this.air;
			if(this.air == -20) {
				this.air = 0;

				for(var8 = 0; var8 < 8; ++var8) {
					float var2 = this.rand.nextFloat() - this.rand.nextFloat();
					float var3 = this.rand.nextFloat() - this.rand.nextFloat();
					float var4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)var2, this.posY + (double)var3, this.posZ + (double)var4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom((Entity)null, 2);
			}

			this.fire = 0;
		} else {
			this.air = this.maxAir;
		}

		this.field_9329_Q = this.field_9328_R;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.field_9306_bj > 0) {
			--this.field_9306_bj;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				this.func_6392_F();
				this.setEntityDead();

				for(var8 = 0; var8 < 20; ++var8) {
					double var9 = this.rand.nextGaussian() * 0.02D;
					double var10 = this.rand.nextGaussian() * 0.02D;
					double var6 = this.rand.nextGaussian() * 0.02D;
					this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var9, var10, var6);
				}
			}
		}

		this.field_9359_x = this.field_9360_w;
		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	public void spawnExplosionParticle() {
		for(int var1 = 0; var1 < 20; ++var1) {
			double var2 = this.rand.nextGaussian() * 0.02D;
			double var4 = this.rand.nextGaussian() * 0.02D;
			double var6 = this.rand.nextGaussian() * 0.02D;
			double var8 = 10.0D;
			this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var2 * var8, this.posY + (double)(this.rand.nextFloat() * this.height) - var4 * var8, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var6 * var8, var2, var4, var6);
		}

	}

	public void func_350_p() {
		super.func_350_p();
		this.field_9362_u = this.field_9361_v;
		this.field_9361_v = 0.0F;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.yOffset = 0.0F;
		this.field_9323_Z = var1;
		this.field_9356_aa = var3;
		this.field_9354_ab = var5;
		this.field_9352_ac = (double)var7;
		this.field_9350_ad = (double)var8;
		this.field_9324_Y = var9;
	}
	
	private boolean canSkipUpdate() {
        if (this.hurtTime > 0) {
            return false;
        } else if (this.ticksExisted < 20) {
            return false;
        } else {
            World world = this.worldObj;

            if (world == null) {
                return false;
            } else if (world.playerEntities.size() != 1) {
                return false;
            } else {
                Entity entity = (Entity)world.playerEntities.get(0);
                double d0 = Math.max(Math.abs(this.posX - entity.posX) - 16.0D, 0.0D);
                double d1 = Math.max(Math.abs(this.posZ - entity.posZ) - 16.0D, 0.0D);
                double d2 = d0 * d0 + d1 * d1;
                return !this.isInRangeToRenderDist(d2);
            }
        }
    }
	
	private void onUpdateMinimal() {
		++this.field_9344_ag;

		if (this instanceof EntityMobs) {
			float f = this.getEntityBrightness(1.0F);

			if (f > 0.5F) {
				this.field_9344_ag += 2;
			}
		}

		this.onLivingUpdate();
	}

	public void onUpdate() {
		if(this.canSkipUpdate()) {
			this.onUpdateMinimal();
		} else {
			super.onUpdate();
			this.onLivingUpdate();
		}
		double var1 = this.posX - this.prevPosX;
		double var3 = this.posZ - this.prevPosZ;
		float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
		float var6 = this.renderYawOffset;
		float var7 = 0.0F;
		this.field_9362_u = this.field_9361_v;
		float var8 = 0.0F;
		if(var5 > 0.05F) {
			var8 = 1.0F;
			var7 = var5 * 3.0F;
			var6 = (float)Math.atan2(var3, var1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(this.swingProgress > 0.0F) {
			var6 = this.rotationYaw;
		}

		if(!this.onGround) {
			var8 = 0.0F;
		}

		this.field_9361_v += (var8 - this.field_9361_v) * 0.3F;

		float var9;
		for(var9 = var6 - this.renderYawOffset; var9 < -180.0F; var9 += 360.0F) {
		}

		while(var9 >= 180.0F) {
			var9 -= 360.0F;
		}

		this.renderYawOffset += var9 * 0.3F;

		float var10;
		for(var10 = this.rotationYaw - this.renderYawOffset; var10 < -180.0F; var10 += 360.0F) {
		}

		while(var10 >= 180.0F) {
			var10 -= 360.0F;
		}

		boolean var11 = var10 < -90.0F || var10 >= 90.0F;
		if(var10 < -75.0F) {
			var10 = -75.0F;
		}

		if(var10 >= 75.0F) {
			var10 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - var10;
		if(var10 * var10 > 2500.0F) {
			this.renderYawOffset += var10 * 0.2F;
		}

		if(var11) {
			var7 *= -1.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.field_9360_w += var7;
	}

	protected void setSize(float var1, float var2) {
		super.setSize(var1, var2);
	}

	public void heal(int var1) {
		if(this.health > 0) {
			this.health += var1;
			if(this.health > 20) {
				this.health = 20;
			}

			this.field_9306_bj = this.field_9366_o / 2;
		}
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(this.worldObj.multiplayerWorld) {
			return false;
		} else {
			this.field_9344_ag = 0;
			if(this.health <= 0) {
				return false;
			} else {
				this.field_704_R = 1.5F;
				boolean var3 = true;
				if((float)this.field_9306_bj > (float)this.field_9366_o / 2.0F) {
					if(var2 <= this.field_9346_af) {
						return false;
					}

					this.damageEntity(var2 - this.field_9346_af);
					this.field_9346_af = var2;
					var3 = false;
				} else {
					this.field_9346_af = var2;
					this.prevHealth = this.health;
					this.field_9306_bj = this.field_9366_o;
					this.damageEntity(var2);
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				if(var3) {
					this.worldObj.func_9425_a(this, (byte)2);
					this.setBeenAttacked();
					if(var1 != null) {
						double var4 = var1.posX - this.posX;

						double var6;
						for(var6 = var1.posZ - this.posZ; var4 * var4 + var6 * var6 < 1.0E-4D; var6 = (Math.random() - Math.random()) * 0.01D) {
							var4 = (Math.random() - Math.random()) * 0.01D;
						}

						this.attackedAtYaw = (float)(Math.atan2(var6, var4) * 180.0D / (double)((float)Math.PI)) - this.rotationYaw;
						this.func_434_a(var1, var2, var4, var6);
					} else {
						this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
					}
				}

				if(this.health <= 0) {
					if(var3) {
						this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					}

					this.onDeath(var1);
				} else if(var3) {
					this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				}

				return true;
			}
		}
	}

	public void performHurtAnimation() {
		this.hurtTime = this.maxHurtTime = 10;
		this.attackedAtYaw = 0.0F;
	}

	protected void damageEntity(int var1) {
		this.health -= var1;
	}

	protected float getSoundVolume() {
		return 1.0F;
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void func_434_a(Entity var1, int var2, double var3, double var5) {
		float var7 = MathHelper.sqrt_double(var3 * var3 + var5 * var5);
		float var8 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= var3 / (double)var7 * (double)var8;
		this.motionY += (double)0.4F;
		this.motionZ -= var5 / (double)var7 * (double)var8;
		if(this.motionY > (double)0.4F) {
			this.motionY = (double)0.4F;
		}

	}

	public void onDeath(Entity var1) {
		if(this.field_9347_E > 0 && var1 != null) {
			var1.addToPlayerScore(this, this.field_9347_E);
		}

		this.field_9327_S = true;
		if(!this.worldObj.multiplayerWorld) {
			int var2 = this.getDropItemId();
			if(var2 > 0) {
				int var3 = this.rand.nextInt(3);

				for(int var4 = 0; var4 < var3; ++var4) {
					this.dropItem(var2, 1);
				}
			}
		}

		this.worldObj.func_9425_a(this, (byte)3);
	}

	protected int getDropItemId() {
		return 0;
	}

	protected void fall(float var1) {
		int var2 = (int)Math.ceil((double)(var1 - 3.0F));
		if(var2 > 0) {
			this.attackEntityFrom((Entity)null, var2);
			int var3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset), MathHelper.floor_double(this.posZ));
			if(var3 > 0) {
				StepSound var4 = Block.blocksList[var3].stepSound;
				this.worldObj.playSoundAtEntity(this, var4.func_1145_d(), var4.func_1147_b() * 0.5F, var4.func_1144_c() * (12.0F / 16.0F));
			}
		}

	}

	public void moveEntityWithHeading(float var1, float var2) {
		double var3;
		if(this.handleWaterMovement()) {
			var3 = this.posY;
			this.func_351_a(var1, var2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.8F;
			this.motionY *= (double)0.8F;
			this.motionZ *= (double)0.8F;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + var3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else if(this.handleLavaMovement()) {
			var3 = this.posY;
			this.func_351_a(var1, var2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + var3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else {
			float var8 = 0.91F;
			if(this.onGround) {
				var8 = 546.0F * 0.1F * 0.1F * 0.1F;
				int var4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(var4 > 0) {
					var8 = Block.blocksList[var4].slipperiness * 0.91F;
				}
			}

			float var9 = 0.16277136F / (var8 * var8 * var8);
			this.func_351_a(var1, var2, this.onGround ? 0.1F * var9 : 0.02F);
			var8 = 0.91F;
			if(this.onGround) {
				var8 = 546.0F * 0.1F * 0.1F * 0.1F;
				int var5 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(var5 > 0) {
					var8 = Block.blocksList[var5].slipperiness * 0.91F;
				}
			}

			if(this.isOnLadder()) {
				this.fallDistance = 0.0F;
				if(this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if(this.isCollidedHorizontally && this.isOnLadder()) {
				this.motionY = 0.2D;
			}

			this.motionY -= 0.08D;
			this.motionY *= (double)0.98F;
			this.motionX *= (double)var8;
			this.motionZ *= (double)var8;
		}

		this.field_705_Q = this.field_704_R;
		var3 = this.posX - this.prevPosX;
		double var10 = this.posZ - this.prevPosZ;
		float var7 = MathHelper.sqrt_double(var3 * var3 + var10 * var10) * 4.0F;
		if(var7 > 1.0F) {
			var7 = 1.0F;
		}

		this.field_704_R += (var7 - this.field_704_R) * 0.4F;
		this.field_703_S += this.field_704_R;
	}

	public boolean isOnLadder() {
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(var1, var2, var3) == Block.ladder.blockID || this.worldObj.getBlockId(var1, var2 + 1, var3) == Block.ladder.blockID;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("Health", (short)this.health);
		var1.setShort("HurtTime", (short)this.hurtTime);
		var1.setShort("DeathTime", (short)this.deathTime);
		var1.setShort("AttackTime", (short)this.attackTime);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.health = var1.getShort("Health");
		if(!var1.hasKey("Health")) {
			this.health = 10;
		}

		this.hurtTime = var1.getShort("HurtTime");
		this.deathTime = var1.getShort("DeathTime");
		this.attackTime = var1.getShort("AttackTime");
	}

	public boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public void onLivingUpdate() {
		if(this.field_9324_Y > 0) {
			double var1 = this.posX + (this.field_9323_Z - this.posX) / (double)this.field_9324_Y;
			double var3 = this.posY + (this.field_9356_aa - this.posY) / (double)this.field_9324_Y;
			double var5 = this.posZ + (this.field_9354_ab - this.posZ) / (double)this.field_9324_Y;

			double var7;
			for(var7 = this.field_9352_ac - (double)this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
			}

			while(var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.field_9324_Y);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.field_9350_ad - (double)this.rotationPitch) / (double)this.field_9324_Y);
			--this.field_9324_Y;
			this.setPosition(var1, var3, var5);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		if(this.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else if(!this.field_9343_G) {
			this.updatePlayerActionState();
		}

		boolean var9 = this.handleWaterMovement();
		boolean var2 = this.handleLavaMovement();
		if(this.isJumping) {
			if(var9) {
				this.motionY += (double)0.04F;
			} else if(var2) {
				this.motionY += (double)0.04F;
			} else if(this.onGround) {
				this.func_424_C();
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
		List<Entity> var10 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
		if(var10 != null && var10.size() > 0) {
			for(int var4 = 0; var4 < var10.size(); ++var4) {
				Entity var11 = (Entity)var10.get(var4);
				if(var11.canBePushed()) {
					var11.applyEntityCollision(this);
				}
			}
		}

	}

	protected void func_424_C() {
		this.motionY = (double)0.42F;
	}

	protected void updatePlayerActionState() {
		++this.field_9344_ag;
		EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
		if(var1 != null) {
			double var2 = var1.posX - this.posX;
			double var4 = var1.posY - this.posY;
			double var6 = var1.posZ - this.posZ;
			double var8 = var2 * var2 + var4 * var4 + var6 * var6;
			if(var8 > 16384.0D) {
				this.setEntityDead();
			}

			if(this.field_9344_ag > 600 && this.rand.nextInt(800) == 0) {
				if(var8 < 1024.0D) {
					this.field_9344_ag = 0;
				} else {
					this.setEntityDead();
				}
			}
		}

		this.moveStrafing = 0.0F;
		this.moveForward = 0.0F;
		float var10 = 8.0F;
		if(this.rand.nextFloat() < 0.02F) {
			var1 = this.worldObj.getClosestPlayerToEntity(this, (double)var10);
			if(var1 != null) {
				this.b = var1;
				this.c = 10 + this.rand.nextInt(20);
			} else {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if(this.b != null) {
			this.faceEntity(this.b, 10.0F);
			if(this.c-- <= 0 || this.b.isDead || this.b.getDistanceSqToEntity(this) > (double)(var10 * var10)) {
				this.b = null;
			}
		} else {
			if(this.rand.nextFloat() < 0.05F) {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}

			this.rotationYaw += this.randomYawVelocity;
			this.rotationPitch = this.defaultPitch;
		}

		boolean var3 = this.handleWaterMovement();
		boolean var11 = this.handleLavaMovement();
		if(var3 || var11) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	public void faceEntity(Entity var1, float var2) {
		double var3 = var1.posX - this.posX;
		double var7 = var1.posZ - this.posZ;
		double var5;
		if(var1 instanceof EntityLiving) {
			EntityLiving var9 = (EntityLiving)var1;
			var5 = var9.posY + (double)var9.func_373_s() - (this.posY + (double)this.func_373_s());
		} else {
			var5 = (var1.boundingBox.minY + var1.boundingBox.maxY) / 2.0D - (this.posY + (double)this.func_373_s());
		}

		double var13 = (double)MathHelper.sqrt_double(var3 * var3 + var7 * var7);
		float var11 = (float)(Math.atan2(var7, var3) * 180.0D / (double)((float)Math.PI)) - 90.0F;
		float var12 = (float)(Math.atan2(var5, var13) * 180.0D / (double)((float)Math.PI));
		this.rotationPitch = -this.updateRotation(this.rotationPitch, var12, var2);
		this.rotationYaw = this.updateRotation(this.rotationYaw, var11, var2);
	}

	private float updateRotation(float var1, float var2, float var3) {
		float var4;
		for(var4 = var2 - var1; var4 < -180.0F; var4 += 360.0F) {
		}

		while(var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		if(var4 > var3) {
			var4 = var3;
		}

		if(var4 < -var3) {
			var4 = -var3;
		}

		return var1 + var4;
	}

	public void func_6392_F() {
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}

	protected void func_4034_G() {
		this.attackEntityFrom((Entity)null, 4);
	}

	public float getSwingProgress(float var1) {
		float var2 = this.swingProgress - this.prevSwingProgress;
		if(var2 < 0.0F) {
			++var2;
		}

		return this.prevSwingProgress + var2 * var1;
	}

	public Vec3D getPosition(float var1) {
		if(var1 == 1.0F) {
			return Vec3D.createVector(this.posX, this.posY, this.posZ);
		} else {
			double var2 = this.prevPosX + (this.posX - this.prevPosX) * (double)var1;
			double var4 = this.prevPosY + (this.posY - this.prevPosY) * (double)var1;
			double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var1;
			return Vec3D.createVector(var2, var4, var6);
		}
	}

	public Vec3D func_4037_H() {
		return this.getLook(1.0F);
	}

	public Vec3D getLook(float var1) {
		float var2;
		float var3;
		float var4;
		float var5;
		if(var1 == 1.0F) {
			var2 = MathHelper.cos(-this.rotationYaw * ((float)Math.PI / 180.0F) - (float)Math.PI);
			var3 = MathHelper.sin(-this.rotationYaw * ((float)Math.PI / 180.0F) - (float)Math.PI);
			var4 = -MathHelper.cos(-this.rotationPitch * ((float)Math.PI / 180.0F));
			var5 = MathHelper.sin(-this.rotationPitch * ((float)Math.PI / 180.0F));
			return Vec3D.createVector((double)(var3 * var4), (double)var5, (double)(var2 * var4));
		} else {
			var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * var1;
			var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * var1;
			var4 = MathHelper.cos(-var3 * ((float)Math.PI / 180.0F) - (float)Math.PI);
			var5 = MathHelper.sin(-var3 * ((float)Math.PI / 180.0F) - (float)Math.PI);
			float var6 = -MathHelper.cos(-var2 * ((float)Math.PI / 180.0F));
			float var7 = MathHelper.sin(-var2 * ((float)Math.PI / 180.0F));
			return Vec3D.createVector((double)(var5 * var6), (double)var7, (double)(var4 * var6));
		}
	}

	public MovingObjectPosition rayTrace(double var1, float var3) {
		Vec3D var4 = this.getPosition(var3);
		Vec3D var5 = this.getLook(var3);
		Vec3D var6 = var4.addVector(var5.xCoord * var1, var5.yCoord * var1, var5.zCoord * var1);
		return this.worldObj.rayTraceBlocks(var4, var6);
	}

	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public ItemStack getHeldItem() {
		return null;
	}

	public void func_9282_a(byte var1) {
		if(var1 == 2) {
			this.field_704_R = 1.5F;
			this.field_9306_bj = this.field_9366_o;
			this.hurtTime = this.maxHurtTime = 10;
			this.attackedAtYaw = 0.0F;
			this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.attackEntityFrom((Entity)null, 0);
		} else if(var1 == 3) {
			this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.health = 0;
			this.onDeath((Entity)null);
		} else {
			super.func_9282_a(var1);
		}

	}
}
