package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.peyton.eagler.minecraft.TextureLocation;

public class GuiCrafting extends GuiContainer {
	public GuiCrafting(InventoryPlayer var1, World var2, int var3, int var4, int var5) {
		super(new CraftingInventoryWorkbenchCB(var1, var2, var3, var4, var5));
	}

	public void onGuiClosed() {
		super.onGuiClosed();
		this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 28, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	private TextureLocation crafting = new TextureLocation("/gui/crafting.png");
	protected void drawGuiContainerBackgroundLayer(float var1) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		crafting.bindTexture();
		int var3 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.ySize);
	}
}
