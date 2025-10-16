package net.minecraft.src;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntityCreeper extends EntityMobs {
	int timeSinceIgnited;
	int lastActiveTime;
	int fuseTime = 30;
	int creeperState = -1;
	int field_12241_e = -1;
	
	private static final TextureLocation creeper = new TextureLocation("/mob/creeper.png");

	public EntityCreeper(World var1) {
		super(var1);
		this.texture = creeper;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	public void func_9282_a(byte var1) {
		super.func_9282_a(var1);
		if(var1 == 4) {
			if(this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.creeperState = 1;
		}

		if(var1 == 5) {
			this.creeperState = -1;
		}

	}

	public void onUpdate() {
		this.lastActiveTime = this.timeSinceIgnited;
		if(this.worldObj.multiplayerWorld) {
			this.timeSinceIgnited += this.creeperState;
			if(this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if(this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
			}
		}

		super.onUpdate();
	}

	protected void updatePlayerActionState() {
		if(this.field_12241_e != this.creeperState) {
			this.field_12241_e = this.creeperState;
			if(this.creeperState > 0) {
				this.worldObj.func_9425_a(this, (byte)4);
			} else {
				this.worldObj.func_9425_a(this, (byte)5);
			}
		}

		this.lastActiveTime = this.timeSinceIgnited;
		if(this.worldObj.multiplayerWorld) {
			super.updatePlayerActionState();
		} else {
			if(this.timeSinceIgnited > 0 && this.creeperState < 0) {
				--this.timeSinceIgnited;
			}

			if(this.creeperState >= 0) {
				this.creeperState = 2;
			}

			super.updatePlayerActionState();
			if(this.creeperState != 1) {
				this.creeperState = -1;
			}
		}

	}

	protected String getHurtSound() {
		return "mob.creeper";
	}

	protected String getDeathSound() {
		return "mob.creeperdeath";
	}

	public void onDeath(Entity var1) {
		super.onDeath(var1);
		if(var1 instanceof EntitySkeleton) {
			this.dropItem(Item.record13.shiftedIndex + this.rand.nextInt(2), 1);
		}

	}

	protected void attackEntity(Entity var1, float var2) {
		if(this.creeperState <= 0 && var2 < 3.0F || this.creeperState > 0 && var2 < 7.0F) {
			if(this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.creeperState = 1;
			++this.timeSinceIgnited;
			if(this.timeSinceIgnited == this.fuseTime) {
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F);
				this.setEntityDead();
			}

			this.hasAttacked = true;
		}

	}

	public float func_440_b(float var1) {
		return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * var1) / (float)(this.fuseTime - 2);
	}

	protected int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}
}
