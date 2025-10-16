package net.minecraft.src;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntitySlime extends EntityLiving implements IMobs {
	public float field_768_a;
	public float field_767_b;
	private int field_769_d = 0;
	public int field_770_c = 1;
	
	private static final TextureLocation slime = new TextureLocation("/mob/slime.png");

	public EntitySlime(World var1) {
		super(var1);
		this.texture = slime;
		this.field_770_c = 1 << this.rand.nextInt(3);
		this.yOffset = 0.0F;
		this.field_769_d = this.rand.nextInt(20) + 10;
		this.func_441_c(this.field_770_c);
	}

	public void func_441_c(int var1) {
		this.field_770_c = var1;
		this.setSize(0.6F * (float)var1, 0.6F * (float)var1);
		this.health = var1 * var1;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("Size", this.field_770_c - 1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.field_770_c = var1.getInteger("Size") + 1;
	}

	public void onUpdate() {
		this.field_767_b = this.field_768_a;
		boolean var1 = this.onGround;
		super.onUpdate();
		if(this.onGround && !var1) {
			for(int var2 = 0; var2 < this.field_770_c * 8; ++var2) {
				float var3 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float var4 = this.rand.nextFloat() * 0.5F + 0.5F;
				float var5 = MathHelper.sin(var3) * (float)this.field_770_c * 0.5F * var4;
				float var6 = MathHelper.cos(var3) * (float)this.field_770_c * 0.5F * var4;
				this.worldObj.spawnParticle("slime", this.posX + (double)var5, this.boundingBox.minY, this.posZ + (double)var6, 0.0D, 0.0D, 0.0D);
			}

			if(this.field_770_c > 2) {
				this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}

			this.field_768_a = -0.5F;
		}

		this.field_768_a *= 0.6F;
	}

	protected void updatePlayerActionState() {
		EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		if(var1 != null) {
			this.faceEntity(var1, 10.0F);
		}

		if(this.onGround && this.field_769_d-- <= 0) {
			this.field_769_d = this.rand.nextInt(20) + 10;
			if(var1 != null) {
				this.field_769_d /= 3;
			}

			this.isJumping = true;
			if(this.field_770_c > 1) {
				this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}

			this.field_768_a = 1.0F;
			this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
			this.moveForward = (float)(1 * this.field_770_c);
		} else {
			this.isJumping = false;
			if(this.onGround) {
				this.moveStrafing = this.moveForward = 0.0F;
			}
		}

	}

	public void setEntityDead() {
		if(this.field_770_c > 1 && this.health == 0) {
			for(int var1 = 0; var1 < 4; ++var1) {
				float var2 = ((float)(var1 % 2) - 0.5F) * (float)this.field_770_c / 4.0F;
				float var3 = ((float)(var1 / 2) - 0.5F) * (float)this.field_770_c / 4.0F;
				EntitySlime var4 = new EntitySlime(this.worldObj);
				var4.func_441_c(this.field_770_c / 2);
				var4.setLocationAndAngles(this.posX + (double)var2, this.posY + 0.5D, this.posZ + (double)var3, this.rand.nextFloat() * 360.0F, 0.0F);
				this.worldObj.entityJoinedWorld(var4);
			}
		}

		super.setEntityDead();
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
		if(this.field_770_c > 1 && this.canEntityBeSeen(var1) && (double)this.getDistanceToEntity(var1) < 0.6D * (double)this.field_770_c && var1.attackEntityFrom(this, this.field_770_c)) {
			this.worldObj.playSoundAtEntity(this, "mob.slimeattack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		}

	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	protected int getDropItemId() {
		return this.field_770_c == 1 ? Item.slimeBall.shiftedIndex : 0;
	}

	public boolean getCanSpawnHere() {
		Chunk var1 = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
		return (this.field_770_c == 1 || this.worldObj.difficultySetting > 0) && this.rand.nextInt(10) == 0 && var1.func_997_a(987234911L).nextInt(10) == 0 && this.posY < 16.0D;
	}

	protected float getSoundVolume() {
		return 0.6F;
	}
}
