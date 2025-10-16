package net.minecraft.src;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntitySheep extends EntityAnimals {
	public boolean sheared = false;
	
	private static final TextureLocation sheep = new TextureLocation("/mob/sheep.png");

	public EntitySheep(World var1) {
		super(var1);
		this.texture = sheep;
		this.setSize(0.9F, 1.3F);
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		if(!this.worldObj.multiplayerWorld && !this.sheared && var1 instanceof EntityLiving) {
			this.sheared = true;
			int var3 = 1 + this.rand.nextInt(3);

			for(int var4 = 0; var4 < var3; ++var4) {
				EntityItem var5 = this.dropItemWithOffset(Block.cloth.blockID, 1, 1.0F);
				var5.motionY += (double)(this.rand.nextFloat() * 0.05F);
				var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
			}
		}

		return super.attackEntityFrom(var1, var2);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setBoolean("Sheared", this.sheared);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.sheared = var1.getBoolean("Sheared");
	}

	protected String getLivingSound() {
		return "mob.sheep";
	}

	protected String getHurtSound() {
		return "mob.sheep";
	}

	protected String getDeathSound() {
		return "mob.sheep";
	}
}
