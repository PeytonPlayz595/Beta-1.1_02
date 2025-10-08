package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class EntityPainting extends Entity {
	private int field_695_c;
	public int field_691_a;
	private int field_9322_d;
	private int field_9321_e;
	private int field_9320_f;
	public EnumArt field_690_b;

	public EntityPainting(World var1) {
		super(var1);
		this.field_695_c = 0;
		this.field_691_a = 0;
		this.yOffset = 0.0F;
		this.setSize(0.5F, 0.5F);
	}

	public EntityPainting(World var1, int var2, int var3, int var4, int var5) {
		this(var1);
		this.field_9322_d = var2;
		this.field_9321_e = var3;
		this.field_9320_f = var4;
		ArrayList<EnumArt> var6 = new ArrayList<>();
		EnumArt[] var7 = EnumArt._VALUES;
		int var8 = var7.length;

		for(int var9 = 0; var9 < var8; ++var9) {
			EnumArt var10 = var7[var9];
			this.field_690_b = var10;
			this.func_412_b(var5);
			if(this.func_410_i()) {
				var6.add(var10);
			}
		}

		if(var6.size() > 0) {
			this.field_690_b = (EnumArt)var6.get(this.rand.nextInt(var6.size()));
		}

		this.func_412_b(var5);
	}

	public void func_412_b(int var1) {
		this.field_691_a = var1;
		this.prevRotationYaw = this.rotationYaw = (float)(var1 * 90);
		float var2 = (float)this.field_690_b.field_1623_z;
		float var3 = (float)this.field_690_b.field_1636_A;
		float var4 = (float)this.field_690_b.field_1623_z;
		if(var1 != 0 && var1 != 2) {
			var2 = 0.5F;
		} else {
			var4 = 0.5F;
		}

		var2 /= 32.0F;
		var3 /= 32.0F;
		var4 /= 32.0F;
		float var5 = (float)this.field_9322_d + 0.5F;
		float var6 = (float)this.field_9321_e + 0.5F;
		float var7 = (float)this.field_9320_f + 0.5F;
		float var8 = 9.0F / 16.0F;
		if(var1 == 0) {
			var7 -= var8;
		}

		if(var1 == 1) {
			var5 -= var8;
		}

		if(var1 == 2) {
			var7 += var8;
		}

		if(var1 == 3) {
			var5 += var8;
		}

		if(var1 == 0) {
			var5 -= this.func_411_c(this.field_690_b.field_1623_z);
		}

		if(var1 == 1) {
			var7 += this.func_411_c(this.field_690_b.field_1623_z);
		}

		if(var1 == 2) {
			var5 += this.func_411_c(this.field_690_b.field_1623_z);
		}

		if(var1 == 3) {
			var7 -= this.func_411_c(this.field_690_b.field_1623_z);
		}

		var6 += this.func_411_c(this.field_690_b.field_1636_A);
		this.setPosition((double)var5, (double)var6, (double)var7);
		float var9 = -(0.1F / 16.0F);
		this.boundingBox.setBounds((double)(var5 - var2 - var9), (double)(var6 - var3 - var9), (double)(var7 - var4 - var9), (double)(var5 + var2 + var9), (double)(var6 + var3 + var9), (double)(var7 + var4 + var9));
	}

	private float func_411_c(int var1) {
		return var1 == 32 ? 0.5F : (var1 == 64 ? 0.5F : 0.0F);
	}

	public void onUpdate() {
		if(this.field_695_c++ == 100 && !this.func_410_i()) {
			this.field_695_c = 0;
			this.setEntityDead();
			this.worldObj.entityJoinedWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.painting)));
		}

	}

	public boolean func_410_i() {
		if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0) {
			return false;
		} else {
			int var1 = this.field_690_b.field_1623_z / 16;
			int var2 = this.field_690_b.field_1636_A / 16;
			int var3 = this.field_9322_d;
			int var4 = this.field_9321_e;
			int var5 = this.field_9320_f;
			if(this.field_691_a == 0) {
				var3 = MathHelper.floor_double(this.posX - (double)((float)this.field_690_b.field_1623_z / 32.0F));
			}

			if(this.field_691_a == 1) {
				var5 = MathHelper.floor_double(this.posZ - (double)((float)this.field_690_b.field_1623_z / 32.0F));
			}

			if(this.field_691_a == 2) {
				var3 = MathHelper.floor_double(this.posX - (double)((float)this.field_690_b.field_1623_z / 32.0F));
			}

			if(this.field_691_a == 3) {
				var5 = MathHelper.floor_double(this.posZ - (double)((float)this.field_690_b.field_1623_z / 32.0F));
			}

			var4 = MathHelper.floor_double(this.posY - (double)((float)this.field_690_b.field_1636_A / 32.0F));

			int var7;
			for(int var6 = 0; var6 < var1; ++var6) {
				for(var7 = 0; var7 < var2; ++var7) {
					Material var8;
					if(this.field_691_a != 0 && this.field_691_a != 2) {
						var8 = this.worldObj.getBlockMaterial(this.field_9322_d, var4 + var7, var5 + var6);
					} else {
						var8 = this.worldObj.getBlockMaterial(var3 + var6, var4 + var7, this.field_9320_f);
					}

					if(!var8.func_878_a()) {
						return false;
					}
				}
			}

			List<Entity> var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);

			for(var7 = 0; var7 < var9.size(); ++var7) {
				if(var9.get(var7) instanceof EntityPainting) {
					return false;
				}
			}

			return true;
		}
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		this.setEntityDead();
		this.setBeenAttacked();
		this.worldObj.entityJoinedWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.painting)));
		return true;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setByte("Dir", (byte)this.field_691_a);
		var1.setString("Motive", this.field_690_b.field_1624_y);
		var1.setInteger("TileX", this.field_9322_d);
		var1.setInteger("TileY", this.field_9321_e);
		var1.setInteger("TileZ", this.field_9320_f);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.field_691_a = var1.getByte("Dir");
		this.field_9322_d = var1.getInteger("TileX");
		this.field_9321_e = var1.getInteger("TileY");
		this.field_9320_f = var1.getInteger("TileZ");
		String var2 = var1.getString("Motive");
		EnumArt[] var3 = EnumArt._VALUES;
		int var4 = var3.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			EnumArt var6 = var3[var5];
			if(var6.field_1624_y.equals(var2)) {
				this.field_690_b = var6;
			}
		}

		if(this.field_690_b == null) {
			this.field_690_b = EnumArt.Kebab;
		}

		this.func_412_b(this.field_691_a);
	}
}
