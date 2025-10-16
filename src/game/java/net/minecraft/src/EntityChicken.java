package net.minecraft.src;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntityChicken extends EntityAnimals {
	public boolean field_753_a = false;
	public float field_752_b = 0.0F;
	public float field_758_c = 0.0F;
	public float field_757_d;
	public float field_756_e;
	public float field_755_h = 1.0F;
	public int timeUntilNextEgg;
	
	private static final TextureLocation chicken = new TextureLocation("/mob/chicken.png");

	public EntityChicken(World var1) {
		super(var1);
		this.texture = chicken;
		this.setSize(0.3F, 0.4F);
		this.health = 4;
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.field_756_e = this.field_752_b;
		this.field_757_d = this.field_758_c;
		this.field_758_c = (float)((double)this.field_758_c + (double)(this.onGround ? -1 : 4) * 0.3D);
		if(this.field_758_c < 0.0F) {
			this.field_758_c = 0.0F;
		}

		if(this.field_758_c > 1.0F) {
			this.field_758_c = 1.0F;
		}

		if(!this.onGround && this.field_755_h < 1.0F) {
			this.field_755_h = 1.0F;
		}

		this.field_755_h = (float)((double)this.field_755_h * 0.9D);
		if(!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		this.field_752_b += this.field_755_h * 2.0F;
		if(!this.worldObj.multiplayerWorld && --this.timeUntilNextEgg <= 0) {
			this.worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.dropItem(Item.egg.shiftedIndex, 1);
			this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		}

	}

	protected void fall(float var1) {
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected String getLivingSound() {
		return "mob.chicken";
	}

	protected String getHurtSound() {
		return "mob.chickenhurt";
	}

	protected String getDeathSound() {
		return "mob.chickenhurt";
	}

	protected int getDropItemId() {
		return Item.feather.shiftedIndex;
	}
}
