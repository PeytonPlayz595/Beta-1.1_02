package net.minecraft.src;

import net.peyton.eagler.minecraft.TextureLocation;

public class EntityZombieSimple extends EntityMobs {
	
	private static final TextureLocation zombie = new TextureLocation("/mob/zombie.png");
	
	public EntityZombieSimple(World var1) {
		super(var1);
		this.texture = zombie;
		this.moveSpeed = 0.5F;
		this.attackStrength = 50;
		this.health *= 10;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return this.worldObj.getLightBrightness(var1, var2, var3) - 0.5F;
	}
}
