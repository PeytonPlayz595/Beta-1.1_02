package net.minecraft.src;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntityPig extends EntityAnimals {
	public boolean rideable = false;
	
	private static final TextureLocation pig = new TextureLocation("/mob/pig.png");

	public EntityPig(World var1) {
		super(var1);
		this.texture = pig;
		this.setSize(0.9F, 0.9F);
		this.rideable = false;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setBoolean("Saddle", this.rideable);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.rideable = var1.getBoolean("Saddle");
	}

	protected String getLivingSound() {
		return "mob.pig";
	}

	protected String getHurtSound() {
		return "mob.pig";
	}

	protected String getDeathSound() {
		return "mob.pigdeath";
	}

	public boolean interact(EntityPlayer var1) {
		if(this.rideable) {
			var1.mountEntity(this);
			return true;
		} else {
			return false;
		}
	}

	protected int getDropItemId() {
		return Item.porkRaw.shiftedIndex;
	}
}
