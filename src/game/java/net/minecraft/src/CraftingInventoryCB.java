package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public abstract class CraftingInventoryCB {
	public List<ItemStack> field_20123_d = new ArrayList<>();
	public List<Slot> field_20122_e = new ArrayList<>();
	public int unusedList = 0;
	private short craftMatrix = 0;
	protected List<ICrafting> field_20121_g = new ArrayList<>();

	protected void func_20117_a(Slot var1) {
		var1.field_20007_a = this.field_20122_e.size();
		this.field_20122_e.add(var1);
		this.field_20123_d.add((ItemStack)null);
	}

	public void func_20114_a() {
		for(int var1 = 0; var1 < this.field_20122_e.size(); ++var1) {
			ItemStack var2 = this.field_20122_e.get(var1).getStack();
			ItemStack var3 = this.field_20123_d.get(var1);
			if(!ItemStack.func_20107_a(var3, var2)) {
				var3 = var2 == null ? null : var2.copy();
				this.field_20123_d.set(var1, var3);

				for(int var4 = 0; var4 < this.field_20121_g.size(); ++var4) {
					this.field_20121_g.get(var4).func_20159_a(this, var1, var3);
				}
			}
		}

	}

	public Slot func_20118_a(int var1) {
		return this.field_20122_e.get(var1);
	}

	public ItemStack func_20116_a(int var1, int var2, EntityPlayer var3) {
		ItemStack var4 = null;
		if(var2 == 0 || var2 == 1) {
			InventoryPlayer var5 = var3.inventory;
			if(var1 == -999) {
				if(var5.func_20075_i() != null && var1 == -999) {
					if(var2 == 0) {
						var3.dropPlayerItem(var5.func_20075_i());
						var5.func_20076_b((ItemStack)null);
					}

					if(var2 == 1) {
						var3.dropPlayerItem(var5.func_20075_i().splitStack(1));
						if(var5.func_20075_i().stackSize == 0) {
							var5.func_20076_b((ItemStack)null);
						}
					}
				}
			} else {
				Slot var6 = (Slot)this.field_20122_e.get(var1);
				if(var6 != null) {
					var6.onSlotChanged();
					ItemStack var7 = var6.getStack();
					if(var7 != null) {
						var4 = var7.copy();
					}

					if(var7 != null || var5.func_20075_i() != null) {
						int var8;
						if(var7 != null && var5.func_20075_i() == null) {
							var8 = var2 == 0 ? var7.stackSize : (var7.stackSize + 1) / 2;
							var5.func_20076_b(var6.func_20004_a(var8));
							if(var7.stackSize == 0) {
								var6.putStack((ItemStack)null);
							}

							var6.onPickupFromSlot();
						} else if(var7 == null && var5.func_20075_i() != null && var6.isItemValid(var5.func_20075_i())) {
							var8 = var2 == 0 ? var5.func_20075_i().stackSize : 1;
							if(var8 > var6.getSlotStackLimit()) {
								var8 = var6.getSlotStackLimit();
							}

							var6.putStack(var5.func_20075_i().splitStack(var8));
							if(var5.func_20075_i().stackSize == 0) {
								var5.func_20076_b((ItemStack)null);
							}
						} else if(var7 != null && var5.func_20075_i() != null) {
							if(var6.isItemValid(var5.func_20075_i())) {
								if(var7.itemID != var5.func_20075_i().itemID) {
									if(var5.func_20075_i().stackSize <= var6.getSlotStackLimit()) {
										var6.putStack(var5.func_20075_i());
										var5.func_20076_b(var7);
									}
								} else if(var7.itemID == var5.func_20075_i().itemID) {
									if(var2 == 0) {
										var8 = var5.func_20075_i().stackSize;
										if(var8 > var6.getSlotStackLimit() - var7.stackSize) {
											var8 = var6.getSlotStackLimit() - var7.stackSize;
										}

										if(var8 > var5.func_20075_i().getMaxStackSize() - var7.stackSize) {
											var8 = var5.func_20075_i().getMaxStackSize() - var7.stackSize;
										}

										var5.func_20075_i().splitStack(var8);
										if(var5.func_20075_i().stackSize == 0) {
											var5.func_20076_b((ItemStack)null);
										}

										var7.stackSize += var8;
									} else if(var2 == 1) {
										var8 = 1;
										if(var8 > var6.getSlotStackLimit() - var7.stackSize) {
											var8 = var6.getSlotStackLimit() - var7.stackSize;
										}

										if(var8 > var5.func_20075_i().getMaxStackSize() - var7.stackSize) {
											var8 = var5.func_20075_i().getMaxStackSize() - var7.stackSize;
										}

										var5.func_20075_i().splitStack(var8);
										if(var5.func_20075_i().stackSize == 0) {
											var5.func_20076_b((ItemStack)null);
										}

										var7.stackSize += var8;
									}
								}
							} else if(var7.itemID == var5.func_20075_i().itemID && var5.func_20075_i().getMaxStackSize() > 1) {
								var8 = var7.stackSize;
								if(var8 > 0 && var8 + var5.func_20075_i().stackSize <= var5.func_20075_i().getMaxStackSize()) {
									ItemStack var10000 = var5.func_20075_i();
									var10000.stackSize += var8;
									var7.splitStack(var8);
									if(var7.stackSize == 0) {
										var6.putStack((ItemStack)null);
									}

									var6.onPickupFromSlot();
								}
							}
						}
					}
				}
			}
		}

		return var4;
	}

	public void onCraftGuiClosed(EntityPlayer var1) {
		InventoryPlayer var2 = var1.inventory;
		if(var2.func_20075_i() != null) {
			var1.dropPlayerItem(var2.func_20075_i());
			var2.func_20076_b((ItemStack)null);
		}

	}

	public void onCraftMatrixChanged(IInventory var1) {
		this.func_20114_a();
	}

	public void func_20119_a(int var1, ItemStack var2) {
		this.func_20118_a(var1).putStack(var2);
	}

	public void func_20115_a(ItemStack[] var1) {
		for(int var2 = 0; var2 < var1.length; ++var2) {
			this.func_20118_a(var2).putStack(var1[var2]);
		}

	}

	public void func_20112_a(int var1, int var2) {
	}

	public short func_20111_a(InventoryPlayer var1) {
		++this.craftMatrix;
		return this.craftMatrix;
	}

	public void func_20113_a(short var1) {
	}

	public void func_20110_b(short var1) {
	}

	public abstract boolean func_20120_b(EntityPlayer var1);
}
