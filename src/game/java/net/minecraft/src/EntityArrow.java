package net.minecraft.src;

import java.util.List;

public class EntityArrow extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private boolean inGround = false;
	public int arrowShake = 0;
	public EntityLiving field_682_g;
	private int field_681_h;
	private int field_680_i = 0;

	public EntityArrow(World var1) {
		super(var1);
		this.setSize(0.5F, 0.5F);
	}

	public EntityArrow(World var1, double var2, double var4, double var6) {
		super(var1);
		this.setSize(0.5F, 0.5F);
		this.setPosition(var2, var4, var6);
		this.yOffset = 0.0F;
	}

	public EntityArrow(World var1, EntityLiving var2) {
		super(var1);
		this.field_682_g = var2;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(var2.posX, var2.posY + (double)var2.func_373_s(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= (double)0.1F;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	public void setArrowHeading(double var1, double var3, double var5, float var7, float var8) {
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
		this.field_681_h = 0;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var7) * 180.0D / (double)((float)Math.PI));
		}

	}

	public void onUpdate() {
		super.onUpdate();
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)((float)Math.PI));
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var1) * 180.0D / (double)((float)Math.PI));
		}

		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inGround) {
			int var15 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			if(var15 == this.inTile) {
				++this.field_681_h;
				if(this.field_681_h == 1200) {
					this.setEntityDead();
				}

				return;
			}

			this.inGround = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
			this.field_681_h = 0;
			this.field_680_i = 0;
		} else {
			++this.field_680_i;
		}

		Vec3D var16 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		Vec3D var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var16, var2);
		var16 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(var3 != null) {
			var2 = Vec3D.createVector(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
		}

		Entity var4 = null;
		List<Entity> var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double var6 = 0.0D;

		float var10;
		for(int var8 = 0; var8 < var5.size(); ++var8) {
			Entity var9 = (Entity)var5.get(var8);
			if(var9.canBeCollidedWith() && (var9 != this.field_682_g || this.field_680_i >= 5)) {
				var10 = 0.3F;
				AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
				MovingObjectPosition var12 = var11.func_1169_a(var16, var2);
				if(var12 != null) {
					double var13 = var16.distanceTo(var12.hitVec);
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

		float var17;
		if(var3 != null) {
			if(var3.entityHit != null) {
				if(var3.entityHit.attackEntityFrom(this.field_682_g, 4)) {
					this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.setEntityDead();
				} else {
					this.motionX *= (double)-0.1F;
					this.motionY *= (double)-0.1F;
					this.motionZ *= (double)-0.1F;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.field_680_i = 0;
				}
			} else {
				this.xTile = var3.blockX;
				this.yTile = var3.blockY;
				this.zTile = var3.blockZ;
				this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
				this.motionX = (double)((float)(var3.hitVec.xCoord - this.posX));
				this.motionY = (double)((float)(var3.hitVec.yCoord - this.posY));
				this.motionZ = (double)((float)(var3.hitVec.zCoord - this.posZ));
				var17 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / (double)var17 * (double)0.05F;
				this.posY -= this.motionY / (double)var17 * (double)0.05F;
				this.posZ -= this.motionZ / (double)var17 * (double)0.05F;
				this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				this.inGround = true;
				this.arrowShake = 7;
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		var17 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)((float)Math.PI));

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var17) * 180.0D / (double)((float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
		float var18 = 0.99F;
		var10 = 0.03F;
		if(this.handleWaterMovement()) {
			for(int var19 = 0; var19 < 4; ++var19) {
				float var20 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var20, this.posY - this.motionY * (double)var20, this.posZ - this.motionZ * (double)var20, this.motionX, this.motionY, this.motionZ);
			}

			var18 = 0.8F;
		}

		this.motionX *= (double)var18;
		this.motionY *= (double)var18;
		this.motionZ *= (double)var18;
		this.motionY -= (double)var10;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("xTile", (short)this.xTile);
		var1.setShort("yTile", (short)this.yTile);
		var1.setShort("zTile", (short)this.zTile);
		var1.setByte("inTile", (byte)this.inTile);
		var1.setByte("shake", (byte)this.arrowShake);
		var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.xTile = var1.getShort("xTile");
		this.yTile = var1.getShort("yTile");
		this.zTile = var1.getShort("zTile");
		this.inTile = var1.getByte("inTile") & 255;
		this.arrowShake = var1.getByte("shake") & 255;
		this.inGround = var1.getByte("inGround") == 1;
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
		if(!this.worldObj.multiplayerWorld) {
			if(this.inGround && this.field_682_g == var1 && this.arrowShake <= 0 && var1.inventory.addItemStackToInventory(new ItemStack(Item.arrow.shiftedIndex, 1))) {
				this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				var1.onItemPickup(this, 1);
				this.setEntityDead();
			}

		}
	}

	public float func_392_h_() {
		return 0.0F;
	}
}
