package net.minecraft.src;

import java.util.List;

public class InventoryBasic implements IInventory {
	private String field_20072_a;
	private int field_20071_b;
	private ItemStack[] field_20074_c;
	private List<IInvBasic> field_20073_d;

	public InventoryBasic(String var1, int var2) {
		this.field_20072_a = var1;
		this.field_20071_b = var2;
		this.field_20074_c = new ItemStack[var2];
	}

	public ItemStack getStackInSlot(int var1) {
		return this.field_20074_c[var1];
	}

	public ItemStack decrStackSize(int var1, int var2) {
		if(this.field_20074_c[var1] != null) {
			ItemStack var3;
			if(this.field_20074_c[var1].stackSize <= var2) {
				var3 = this.field_20074_c[var1];
				this.field_20074_c[var1] = null;
				this.onInventoryChanged();
				return var3;
			} else {
				var3 = this.field_20074_c[var1].splitStack(var2);
				if(this.field_20074_c[var1].stackSize == 0) {
					this.field_20074_c[var1] = null;
				}

				this.onInventoryChanged();
				return var3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int var1, ItemStack var2) {
		this.field_20074_c[var1] = var2;
		if(var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
			var2.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public int getSizeInventory() {
		return this.field_20071_b;
	}

	public String getInvName() {
		return this.field_20072_a;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
		if(this.field_20073_d != null) {
			for(int var1 = 0; var1 < this.field_20073_d.size(); ++var1) {
				this.field_20073_d.get(var1).func_20134_a(this);
			}
		}

	}

	public boolean func_20070_a_(EntityPlayer var1) {
		return true;
	}
}
