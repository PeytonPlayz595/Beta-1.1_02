package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.peyton.eagler.minecraft.TextureLocation;

public class GuiChest extends GuiContainer {
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private int field_980_m = 0;

	public GuiChest(IInventory var1, IInventory var2) {
		super(new CraftingInventoryChestCB(var1, var2));
		this.upperChestInventory = var1;
		this.lowerChestInventory = var2;
		this.field_948_f = false;
		short var3 = 222;
		int var4 = var3 - 108;
		this.field_980_m = var2.getSizeInventory() / 9;
		this.ySize = var4 + this.field_980_m * 18;
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(this.lowerChestInventory.getInvName(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float var1) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		TextureLocation.guiContainer.bindTexture();
		int var3 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.field_980_m * 18 + 17);
		this.drawTexturedModalRect(var3, var4 + this.field_980_m * 18 + 17, 0, 126, this.xSize, 96);
	}
}
