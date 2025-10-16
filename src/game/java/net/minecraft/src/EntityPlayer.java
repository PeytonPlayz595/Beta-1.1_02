package net.minecraft.src;

import java.util.List;

import net.peyton.eagler.minecraft.TextureLocation;

public abstract class EntityPlayer extends EntityLiving {
	public InventoryPlayer inventory = new InventoryPlayer(this);
	public CraftingInventoryCB field_20069_g;
	public CraftingInventoryCB field_20068_h;
	public byte field_9371_f = 0;
	public int score = 0;
	public float field_775_e;
	public float field_774_f;
	public boolean isSwinging = false;
	public int swingProgressInt = 0;
	public String field_771_i;
	public int dimension;
	public String field_20067_q;
	public double field_20066_r;
	public double field_20065_s;
	public double field_20064_t;
	public double field_20063_u;
	public double field_20062_v;
	public double field_20061_w;
	private int damageRemainder = 0;
	public EntityFish fishEntity = null;
	
	private static final TextureLocation player = new TextureLocation("/mob/char.png");

	public EntityPlayer(World var1) {
		super(var1);
		this.field_20069_g = new CraftingInventoryPlayerCB(this.inventory, !var1.multiplayerWorld);
		this.field_20068_h = this.field_20069_g;
		this.yOffset = 1.62F;
		this.setLocationAndAngles((double)var1.spawnX + 0.5D, (double)(var1.spawnY + 1), (double)var1.spawnZ + 0.5D, 0.0F, 0.0F);
		this.health = 20;
		this.field_9351_C = "humanoid";
		this.field_9353_B = 180.0F;
		this.fireResistance = 20;
		this.texture = player;
	}

	public void onUpdate() {
		super.onUpdate();
		if(!this.worldObj.multiplayerWorld && this.field_20068_h != null && !this.field_20068_h.func_20120_b(this)) {
			this.func_20059_m();
			this.field_20068_h = this.field_20069_g;
		}

		this.field_20066_r = this.field_20063_u;
		this.field_20065_s = this.field_20062_v;
		this.field_20064_t = this.field_20061_w;
		double var1 = this.posX - this.field_20063_u;
		double var3 = this.posY - this.field_20062_v;
		double var5 = this.posZ - this.field_20061_w;
		double var7 = 10.0D;
		if(var1 > var7) {
			this.field_20066_r = this.field_20063_u = this.posX;
		}

		if(var5 > var7) {
			this.field_20064_t = this.field_20061_w = this.posZ;
		}

		if(var3 > var7) {
			this.field_20065_s = this.field_20062_v = this.posY;
		}

		if(var1 < -var7) {
			this.field_20066_r = this.field_20063_u = this.posX;
		}

		if(var5 < -var7) {
			this.field_20064_t = this.field_20061_w = this.posZ;
		}

		if(var3 < -var7) {
			this.field_20065_s = this.field_20062_v = this.posY;
		}

		this.field_20063_u += var1 * 0.25D;
		this.field_20061_w += var5 * 0.25D;
		this.field_20062_v += var3 * 0.25D;
	}

	protected void func_20059_m() {
		this.field_20068_h = this.field_20069_g;
	}

	public void func_20046_s() {
		this.field_20067_q = "http://www.minecraft.net/cloak/get.jsp?user=" + this.field_771_i;
		this.skinUrl = this.field_20067_q;
	}

	public void func_350_p() {
		super.func_350_p();
		this.field_775_e = this.field_774_f;
		this.field_774_f = 0.0F;
	}

	public void preparePlayerToSpawn() {
		this.yOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		this.health = 20;
		this.deathTime = 0;
	}

	protected void updatePlayerActionState() {
		if(this.isSwinging) {
			++this.swingProgressInt;
			if(this.swingProgressInt == 8) {
				this.swingProgressInt = 0;
				this.isSwinging = false;
			}
		} else {
			this.swingProgressInt = 0;
		}

		this.swingProgress = (float)this.swingProgressInt / 8.0F;
	}

	public void onLivingUpdate() {
		if(this.worldObj.difficultySetting == 0 && this.health < 20 && this.ticksExisted % 20 * 12 == 0) {
			this.heal(1);
		}

		this.inventory.decrementAnimations();
		this.field_775_e = this.field_774_f;
		super.onLivingUpdate();
		float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float var2 = (float)Math.atan(-this.motionY * (double)0.2F) * 15.0F;
		if(var1 > 0.1F) {
			var1 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			var1 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			var2 = 0.0F;
		}

		this.field_774_f += (var1 - this.field_774_f) * 0.4F;
		this.field_9328_R += (var2 - this.field_9328_R) * 0.8F;
		if(this.health > 0) {
			List<Entity> var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.0D, 1.0D));
			if(var3 != null) {
				for(int var4 = 0; var4 < var3.size(); ++var4) {
					Entity var5 = (Entity)var3.get(var4);
					if(!var5.isDead) {
						this.func_451_h(var5);
					}
				}
			}
		}

	}

	private void func_451_h(Entity var1) {
		var1.onCollideWithPlayer(this);
	}

	public int getScore() {
		return this.score;
	}

	public void onDeath(Entity var1) {
		super.onDeath(var1);
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = (double)0.1F;
		if(this.field_771_i.equals("Notch")) {
			this.dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
		}

		this.inventory.dropAllItems();
		if(var1 != null) {
			this.motionX = (double)(-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
			this.motionZ = (double)(-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
		} else {
			this.motionX = this.motionZ = 0.0D;
		}

		this.yOffset = 0.1F;
	}

	public void addToPlayerScore(Entity var1, int var2) {
		this.score += var2;
	}

	public void func_20060_w() {
		this.dropPlayerItemWithRandomChoice(this.inventory.decrStackSize(this.inventory.currentItem, 1), false);
	}

	public void dropPlayerItem(ItemStack var1) {
		this.dropPlayerItemWithRandomChoice(var1, false);
	}

	public void dropPlayerItemWithRandomChoice(ItemStack var1, boolean var2) {
		if(var1 != null) {
			EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY - (double)0.3F + (double)this.func_373_s(), this.posZ, var1);
			var3.delayBeforeCanPickup = 40;
			float var4 = 0.1F;
			float var5;
			if(var2) {
				var5 = this.rand.nextFloat() * 0.5F;
				float var6 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				var3.motionX = (double)(-MathHelper.sin(var6) * var5);
				var3.motionZ = (double)(MathHelper.cos(var6) * var5);
				var3.motionY = (double)0.2F;
			} else {
				var4 = 0.3F;
				var3.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var4);
				var3.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var4);
				var3.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * var4 + 0.1F);
				var4 = 0.02F;
				var5 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				var4 *= this.rand.nextFloat();
				var3.motionX += Math.cos((double)var5) * (double)var4;
				var3.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				var3.motionZ += Math.sin((double)var5) * (double)var4;
			}

			this.joinEntityItemWithWorld(var3);
		}
	}

	protected void joinEntityItemWithWorld(EntityItem var1) {
		this.worldObj.entityJoinedWorld(var1);
	}

	public float getCurrentPlayerStrVsBlock(Block var1) {
		float var2 = this.inventory.getStrVsBlock(var1);
		if(this.isInsideOfMaterial(Material.water)) {
			var2 /= 5.0F;
		}

		if(!this.onGround) {
			var2 /= 5.0F;
		}

		return var2;
	}

	public boolean canHarvestBlock(Block var1) {
		return this.inventory.canHarvestBlock(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		NBTTagList var2 = var1.getTagList("Inventory");
		this.inventory.readFromNBT(var2);
		this.dimension = var1.getInteger("Dimension");
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
		var1.setInteger("Dimension", this.dimension);
	}

	public void displayGUIChest(IInventory var1) {
	}

	public void displayWorkbenchGUI(int var1, int var2, int var3) {
	}

	public void onItemPickup(Entity var1, int var2) {
	}

	public float func_373_s() {
		return 0.12F;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		this.field_9344_ag = 0;
		if(this.health <= 0) {
			return false;
		} else {
			if(var1 instanceof EntityMobs || var1 instanceof EntityArrow) {
				if(this.worldObj.difficultySetting == 0) {
					var2 = 0;
				}

				if(this.worldObj.difficultySetting == 1) {
					var2 = var2 / 3 + 1;
				}

				if(this.worldObj.difficultySetting == 3) {
					var2 = var2 * 3 / 2;
				}
			}

			return var2 == 0 ? false : super.attackEntityFrom(var1, var2);
		}
	}

	protected void damageEntity(int var1) {
		int var2 = 25 - this.inventory.getTotalArmorValue();
		int var3 = var1 * var2 + this.damageRemainder;
		this.inventory.damageArmor(var1);
		var1 = var3 / 25;
		this.damageRemainder = var3 % 25;
		super.damageEntity(var1);
	}

	public void displayGUIFurnace(TileEntityFurnace var1) {
	}

	public void displayGUIEditSign(TileEntitySign var1) {
	}

	public void useCurrentItemOnEntity(Entity var1) {
		var1.interact(this);
	}

	public ItemStack getCurrentEquippedItem() {
		return this.inventory.getCurrentItem();
	}

	public void destroyCurrentEquippedItem() {
		this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
	}

	public double getYOffset() {
		return (double)(this.yOffset - 0.5F);
	}

	public void swingItem() {
		this.swingProgressInt = -1;
		this.isSwinging = true;
	}

	public void attackTargetEntityWithCurrentItem(Entity var1) {
		int var2 = this.inventory.getDamageVsEntity(var1);
		if(var2 > 0) {
			var1.attackEntityFrom(this, var2);
			ItemStack var3 = this.getCurrentEquippedItem();
			if(var3 != null && var1 instanceof EntityLiving) {
				var3.hitEntity((EntityLiving)var1);
				if(var3.stackSize <= 0) {
					var3.func_1097_a(this);
					this.destroyCurrentEquippedItem();
				}
			}
		}

	}

	public void respawnPlayer() {
	}

	public void func_20058_b(ItemStack var1) {
	}

	public void setEntityDead() {
		super.setEntityDead();
		this.field_20069_g.onCraftGuiClosed(this);
		if(this.field_20068_h != null) {
			this.field_20068_h.onCraftGuiClosed(this);
		}

	}
}
