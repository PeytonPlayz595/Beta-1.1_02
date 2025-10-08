package net.minecraft.src;

import java.util.List;

public class EntityFish extends Entity {
	private int field_4095_d;
	private int field_4094_e;
	private int field_4093_f;
	private int field_4092_g;
	private boolean field_4091_h;
	public int field_4098_a;
	public EntityPlayer field_4097_b;
	private int field_4090_i;
	private int field_4089_j;
	private int field_4088_k;
	public Entity field_4096_c;
	private int field_6388_l;
	private double field_6387_m;
	private double field_6386_n;
	private double field_6385_o;
	private double field_6384_p;
	private double field_6383_q;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	public EntityFish(World var1) {
		super(var1);
		this.field_4095_d = -1;
		this.field_4094_e = -1;
		this.field_4093_f = -1;
		this.field_4092_g = 0;
		this.field_4091_h = false;
		this.field_4098_a = 0;
		this.field_4089_j = 0;
		this.field_4088_k = 0;
		this.field_4096_c = null;
		this.setSize(0.25F, 0.25F);
	}

	public boolean isInRangeToRenderDist(double var1) {
		double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		var3 *= 64.0D;
		return var1 < var3 * var3;
	}

	public EntityFish(World var1, double var2, double var4, double var6) {
		this(var1);
		this.setPosition(var2, var4, var6);
	}

	public EntityFish(World var1, EntityPlayer var2) {
		super(var1);
		this.field_4095_d = -1;
		this.field_4094_e = -1;
		this.field_4093_f = -1;
		this.field_4092_g = 0;
		this.field_4091_h = false;
		this.field_4098_a = 0;
		this.field_4089_j = 0;
		this.field_4088_k = 0;
		this.field_4096_c = null;
		this.field_4097_b = var2;
		this.field_4097_b.fishEntity = this;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(var2.posX, var2.posY + 1.62D - (double)var2.yOffset, var2.posZ, var2.rotationYaw, var2.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= (double)0.1F;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		float var3 = 0.4F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
		this.func_4042_a(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	public void func_4042_a(double var1, double var3, double var5, float var7, float var8) {
		float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		var1 /= (double)var9;
		var3 /= (double)var9;
		var5 /= (double)var9;
		var1 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var3 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var5 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var1 *= (double)var7;
		var3 *= (double)var7;
		var5 *= (double)var7;
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / (double)((float)Math.PI));
		this.field_4090_i = 0;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.field_6387_m = var1;
		this.field_6386_n = var3;
		this.field_6385_o = var5;
		this.field_6384_p = (double)var7;
		this.field_6383_q = (double)var8;
		this.field_6388_l = var9;
		this.motionX = this.velocityX;
		this.motionY = this.velocityY;
		this.motionZ = this.velocityZ;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.velocityX = this.motionX = var1;
		this.velocityY = this.motionY = var3;
		this.velocityZ = this.motionZ = var5;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.field_6388_l > 0) {
			double var21 = this.posX + (this.field_6387_m - this.posX) / (double)this.field_6388_l;
			double var22 = this.posY + (this.field_6386_n - this.posY) / (double)this.field_6388_l;
			double var23 = this.posZ + (this.field_6385_o - this.posZ) / (double)this.field_6388_l;

			double var7;
			for(var7 = this.field_6384_p - (double)this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
			}

			while(var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.field_6388_l);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.field_6383_q - (double)this.rotationPitch) / (double)this.field_6388_l);
			--this.field_6388_l;
			this.setPosition(var21, var22, var23);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		} else {
			if(!this.worldObj.multiplayerWorld) {
				ItemStack var1 = this.field_4097_b.getCurrentEquippedItem();
				if(this.field_4097_b.isDead || !this.field_4097_b.isEntityAlive() || var1 == null || var1.getItem() != Item.fishingRod || this.getDistanceSqToEntity(this.field_4097_b) > 1024.0D) {
					this.setEntityDead();
					this.field_4097_b.fishEntity = null;
					return;
				}

				if(this.field_4096_c != null) {
					if(!this.field_4096_c.isDead) {
						this.posX = this.field_4096_c.posX;
						this.posY = this.field_4096_c.boundingBox.minY + (double)this.field_4096_c.height * 0.8D;
						this.posZ = this.field_4096_c.posZ;
						return;
					}

					this.field_4096_c = null;
				}
			}

			if(this.field_4098_a > 0) {
				--this.field_4098_a;
			}

			if(this.field_4091_h) {
				int var19 = this.worldObj.getBlockId(this.field_4095_d, this.field_4094_e, this.field_4093_f);
				if(var19 == this.field_4092_g) {
					++this.field_4090_i;
					if(this.field_4090_i == 1200) {
						this.setEntityDead();
					}

					return;
				}

				this.field_4091_h = false;
				this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				this.field_4090_i = 0;
				this.field_4089_j = 0;
			} else {
				++this.field_4089_j;
			}

			Vec3D var20 = Vec3D.createVector(this.posX, this.posY, this.posZ);
			Vec3D var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var20, var2);
			var20 = Vec3D.createVector(this.posX, this.posY, this.posZ);
			var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if(var3 != null) {
				var2 = Vec3D.createVector(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
			}

			Entity var4 = null;
			List<Entity> var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;

			double var13;
			for(int var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);
				if(var9.canBeCollidedWith() && (var9 != this.field_4097_b || this.field_4089_j >= 5)) {
					float var10 = 0.3F;
					AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
					MovingObjectPosition var12 = var11.func_1169_a(var20, var2);
					if(var12 != null) {
						var13 = var20.distanceTo(var12.hitVec);
						if(var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}

			if(var4 != null) {
				var3 = new MovingObjectPosition(var4);
			}

			if(var3 != null) {
				if(var3.entityHit != null) {
					if(var3.entityHit.attackEntityFrom(this.field_4097_b, 0)) {
						this.field_4096_c = var3.entityHit;
					}
				} else {
					this.field_4091_h = true;
				}
			}

			if(!this.field_4091_h) {
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				float var24 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
				this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)((float)Math.PI));

				for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var24) * 180.0D / (double)((float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
				}

				while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
					this.prevRotationPitch += 360.0F;
				}

				while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
					this.prevRotationYaw -= 360.0F;
				}

				while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
					this.prevRotationYaw += 360.0F;
				}

				this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
				this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
				float var25 = 0.92F;
				if(this.onGround || this.isCollidedHorizontally) {
					var25 = 0.5F;
				}

				byte var26 = 5;
				double var27 = 0.0D;

				for(int var28 = 0; var28 < var26; ++var28) {
					double var14 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var28 + 0) / (double)var26 - 0.125D + 0.125D;
					double var16 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var28 + 1) / (double)var26 - 0.125D + 0.125D;
					AxisAlignedBB var18 = AxisAlignedBB.getBoundingBoxFromPool(this.boundingBox.minX, var14, this.boundingBox.minZ, this.boundingBox.maxX, var16, this.boundingBox.maxZ);
					if(this.worldObj.func_707_b(var18, Material.water)) {
						var27 += 1.0D / (double)var26;
					}
				}

				if(var27 > 0.0D) {
					if(this.field_4088_k > 0) {
						--this.field_4088_k;
					} else if(this.rand.nextInt(500) == 0) {
						this.field_4088_k = this.rand.nextInt(30) + 10;
						this.motionY -= (double)0.2F;
						this.worldObj.playSoundAtEntity(this, "random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
						float var29 = (float)MathHelper.floor_double(this.boundingBox.minY);

						float var15;
						int var30;
						float var31;
						for(var30 = 0; (float)var30 < 1.0F + this.width * 20.0F; ++var30) {
							var15 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							var31 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							this.worldObj.spawnParticle("bubble", this.posX + (double)var15, (double)(var29 + 1.0F), this.posZ + (double)var31, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
						}

						for(var30 = 0; (float)var30 < 1.0F + this.width * 20.0F; ++var30) {
							var15 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							var31 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
							this.worldObj.spawnParticle("splash", this.posX + (double)var15, (double)(var29 + 1.0F), this.posZ + (double)var31, this.motionX, this.motionY, this.motionZ);
						}
					}
				}

				if(this.field_4088_k > 0) {
					this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
				}

				var13 = var27 * 2.0D - 1.0D;
				this.motionY += (double)0.04F * var13;
				if(var27 > 0.0D) {
					var25 = (float)((double)var25 * 0.9D);
					this.motionY *= 0.8D;
				}

				this.motionX *= (double)var25;
				this.motionY *= (double)var25;
				this.motionZ *= (double)var25;
				this.setPosition(this.posX, this.posY, this.posZ);
			}
		}
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("xTile", (short)this.field_4095_d);
		var1.setShort("yTile", (short)this.field_4094_e);
		var1.setShort("zTile", (short)this.field_4093_f);
		var1.setByte("inTile", (byte)this.field_4092_g);
		var1.setByte("shake", (byte)this.field_4098_a);
		var1.setByte("inGround", (byte)(this.field_4091_h ? 1 : 0));
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.field_4095_d = var1.getShort("xTile");
		this.field_4094_e = var1.getShort("yTile");
		this.field_4093_f = var1.getShort("zTile");
		this.field_4092_g = var1.getByte("inTile") & 255;
		this.field_4098_a = var1.getByte("shake") & 255;
		this.field_4091_h = var1.getByte("inGround") == 1;
	}

	public float func_392_h_() {
		return 0.0F;
	}

	public int func_4043_i() {
		byte var1 = 0;
		if(this.field_4096_c != null) {
			double var2 = this.field_4097_b.posX - this.posX;
			double var4 = this.field_4097_b.posY - this.posY;
			double var6 = this.field_4097_b.posZ - this.posZ;
			double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
			double var10 = 0.1D;
			this.field_4096_c.motionX += var2 * var10;
			this.field_4096_c.motionY += var4 * var10 + (double)MathHelper.sqrt_double(var8) * 0.08D;
			this.field_4096_c.motionZ += var6 * var10;
			var1 = 3;
		} else if(this.field_4088_k > 0) {
			EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.fishRaw.shiftedIndex));
			double var3 = this.field_4097_b.posX - this.posX;
			double var5 = this.field_4097_b.posY - this.posY;
			double var7 = this.field_4097_b.posZ - this.posZ;
			double var9 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
			double var11 = 0.1D;
			var13.motionX = var3 * var11;
			var13.motionY = var5 * var11 + (double)MathHelper.sqrt_double(var9) * 0.08D;
			var13.motionZ = var7 * var11;
			this.worldObj.entityJoinedWorld(var13);
			var1 = 1;
		}

		if(this.field_4091_h) {
			var1 = 2;
		}

		this.setEntityDead();
		this.field_4097_b.fishEntity = null;
		return var1;
	}
}
