package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.peyton.eagler.minecraft.Tessellator;
import net.peyton.eagler.minecraft.TextureLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRenderer {
	private Minecraft mc;
	private ItemStack field_9451_b = null;
	private float field_9453_c = 0.0F;
	private float field_9452_d = 0.0F;
	private RenderBlocks field_1357_e = new RenderBlocks();
	private int field_20099_f = -1;

	public ItemRenderer(Minecraft var1) {
		this.mc = var1;
	}

	public void renderItem(ItemStack var1) {
		GL11.glPushMatrix();
		if(var1.itemID < 256 && RenderBlocks.func_1219_a(Block.blocksList[var1.itemID].getRenderType())) {
			TextureLocation.terrain.bindTexture();
			this.field_1357_e.func_1227_a(Block.blocksList[var1.itemID]);
		} else {
			if(var1.itemID < 256) {
				TextureLocation.terrain.bindTexture();
			} else {
				TextureLocation.items.bindTexture();
			}

			Tessellator var2 = Tessellator.instance;
			float var3 = ((float)(var1.getIconIndex() % 16 * 16) + 0.0F) / 256.0F;
			float var4 = ((float)(var1.getIconIndex() % 16 * 16) + 15.99F) / 256.0F;
			float var5 = ((float)(var1.getIconIndex() / 16 * 16) + 0.0F) / 256.0F;
			float var6 = ((float)(var1.getIconIndex() / 16 * 16) + 15.99F) / 256.0F;
			float var7 = 1.0F;
			float var8 = 0.0F;
			float var9 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-var8, -var9, 0.0F);
			float var10 = 1.5F;
			GL11.glScalef(var10, var10, var10);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-(15.0F / 16.0F), -(1.0F / 16.0F), 0.0F);
			float var11 = 1.0F / 16.0F;
			var2.startDrawingQuads();
			var2.setNormal(0.0F, 0.0F, 1.0F);
			var2.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)var4, (double)var6);
			var2.addVertexWithUV((double)var7, 0.0D, 0.0D, (double)var3, (double)var6);
			var2.addVertexWithUV((double)var7, 1.0D, 0.0D, (double)var3, (double)var5);
			var2.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)var4, (double)var5);
			var2.draw();
			var2.startDrawingQuads();
			var2.setNormal(0.0F, 0.0F, -1.0F);
			var2.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - var11), (double)var4, (double)var5);
			var2.addVertexWithUV((double)var7, 1.0D, (double)(0.0F - var11), (double)var3, (double)var5);
			var2.addVertexWithUV((double)var7, 0.0D, (double)(0.0F - var11), (double)var3, (double)var6);
			var2.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - var11), (double)var4, (double)var6);
			var2.draw();
			var2.startDrawingQuads();
			var2.setNormal(-1.0F, 0.0F, 0.0F);

			int var12;
			float var13;
			float var14;
			float var15;
			for(var12 = 0; var12 < 16; ++var12) {
				var13 = (float)var12 / 16.0F;
				var14 = var4 + (var3 - var4) * var13 - 0.001953125F;
				var15 = var7 * var13;
				var2.addVertexWithUV((double)var15, 0.0D, (double)(0.0F - var11), (double)var14, (double)var6);
				var2.addVertexWithUV((double)var15, 0.0D, 0.0D, (double)var14, (double)var6);
				var2.addVertexWithUV((double)var15, 1.0D, 0.0D, (double)var14, (double)var5);
				var2.addVertexWithUV((double)var15, 1.0D, (double)(0.0F - var11), (double)var14, (double)var5);
			}

			var2.draw();
			var2.startDrawingQuads();
			var2.setNormal(1.0F, 0.0F, 0.0F);

			for(var12 = 0; var12 < 16; ++var12) {
				var13 = (float)var12 / 16.0F;
				var14 = var4 + (var3 - var4) * var13 - 0.001953125F;
				var15 = var7 * var13 + 1.0F / 16.0F;
				var2.addVertexWithUV((double)var15, 1.0D, (double)(0.0F - var11), (double)var14, (double)var5);
				var2.addVertexWithUV((double)var15, 1.0D, 0.0D, (double)var14, (double)var5);
				var2.addVertexWithUV((double)var15, 0.0D, 0.0D, (double)var14, (double)var6);
				var2.addVertexWithUV((double)var15, 0.0D, (double)(0.0F - var11), (double)var14, (double)var6);
			}

			var2.draw();
			var2.startDrawingQuads();
			var2.setNormal(0.0F, 1.0F, 0.0F);

			for(var12 = 0; var12 < 16; ++var12) {
				var13 = (float)var12 / 16.0F;
				var14 = var6 + (var5 - var6) * var13 - 0.001953125F;
				var15 = var7 * var13 + 1.0F / 16.0F;
				var2.addVertexWithUV(0.0D, (double)var15, 0.0D, (double)var4, (double)var14);
				var2.addVertexWithUV((double)var7, (double)var15, 0.0D, (double)var3, (double)var14);
				var2.addVertexWithUV((double)var7, (double)var15, (double)(0.0F - var11), (double)var3, (double)var14);
				var2.addVertexWithUV(0.0D, (double)var15, (double)(0.0F - var11), (double)var4, (double)var14);
			}

			var2.draw();
			var2.startDrawingQuads();
			var2.setNormal(0.0F, -1.0F, 0.0F);

			for(var12 = 0; var12 < 16; ++var12) {
				var13 = (float)var12 / 16.0F;
				var14 = var6 + (var5 - var6) * var13 - 0.001953125F;
				var15 = var7 * var13;
				var2.addVertexWithUV((double)var7, (double)var15, 0.0D, (double)var3, (double)var14);
				var2.addVertexWithUV(0.0D, (double)var15, 0.0D, (double)var4, (double)var14);
				var2.addVertexWithUV(0.0D, (double)var15, (double)(0.0F - var11), (double)var4, (double)var14);
				var2.addVertexWithUV((double)var7, (double)var15, (double)(0.0F - var11), (double)var3, (double)var14);
			}

			var2.draw();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		GL11.glPopMatrix();
	}

	public void renderItemInFirstPerson(float var1) {
		float var2 = this.field_9452_d + (this.field_9453_c - this.field_9452_d) * var1;
		EntityPlayerSP var3 = this.mc.thePlayer;
		GL11.glPushMatrix();
		GL11.glRotatef(var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		float var4 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ));
		GL11.glColor4f(var4, var4, var4, 1.0F);
		ItemStack var5 = this.field_9451_b;
		if(var3.fishEntity != null) {
			var5 = new ItemStack(Item.stick.shiftedIndex);
		}

		float var6;
		float var7;
		float var8;
		float var9;
		if(var5 != null) {
			GL11.glPushMatrix();
			var6 = 0.8F;
			var7 = var3.getSwingProgress(var1);
			var8 = MathHelper.sin(var7 * (float)Math.PI);
			var9 = MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI);
			GL11.glTranslatef(-var9 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI * 2.0F) * 0.2F, -var8 * 0.2F);
			GL11.glTranslatef(0.7F * var6, -0.65F * var6 - (1.0F - var2) * 0.6F, -0.9F * var6);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var7 = var3.getSwingProgress(var1);
			var8 = MathHelper.sin(var7 * var7 * (float)Math.PI);
			var9 = MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI);
			GL11.glRotatef(-var8 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var9 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var9 * 80.0F, 1.0F, 0.0F, 0.0F);
			var7 = 0.4F;
			GL11.glScalef(var7, var7, var7);
			if(var5.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			this.renderItem(var5);
			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			var6 = 0.8F;
			var7 = var3.getSwingProgress(var1);
			var8 = MathHelper.sin(var7 * (float)Math.PI);
			var9 = MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI);
			GL11.glTranslatef(-var9 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI * 2.0F) * 0.4F, -var8 * 0.4F);
			GL11.glTranslatef(0.8F * var6, -(12.0F / 16.0F) * var6 - (1.0F - var2) * 0.6F, -0.9F * var6);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var7 = var3.getSwingProgress(var1);
			var8 = MathHelper.sin(var7 * var7 * (float)Math.PI);
			var9 = MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI);
			GL11.glRotatef(var9 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var8 * 20.0F, 0.0F, 0.0F, 1.0F);
			TextureLocation location = this.mc.thePlayer.getEntityTexture();
			if(location != null) {
				location.bindTexture();
			}
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			Render var10 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			RenderPlayer var11 = (RenderPlayer)var10;
			var9 = 1.0F;
			GL11.glScalef(var9, var9, var9);
			var11.drawFirstPersonHand();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}

	private static final TextureLocation water = new TextureLocation("/misc/water.png");
	public void renderOverlays(float var1) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		if(this.mc.thePlayer.fire > 0 || this.mc.thePlayer.field_9299_bv) {
			TextureLocation.terrain.bindTexture();
			this.renderFireInFirstPerson(var1);
		}

		if(this.mc.thePlayer.func_345_I()) {
			int var2 = MathHelper.floor_double(this.mc.thePlayer.posX);
			int var3 = MathHelper.floor_double(this.mc.thePlayer.posY);
			int var4 = MathHelper.floor_double(this.mc.thePlayer.posZ);
			TextureLocation.terrain.bindTexture();
			int var6 = this.mc.theWorld.getBlockId(var2, var3, var4);
			if(Block.blocksList[var6] != null) {
				this.renderInsideOfBlock(var1, Block.blocksList[var6].getBlockTextureFromSide(2));
			}
		}

		if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
			water.bindTexture();
			this.renderWarpedTextureOverlay(var1);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	private void renderInsideOfBlock(float var1, int var2) {
		Tessellator var3 = Tessellator.instance;
		this.mc.thePlayer.getEntityBrightness(var1);
		float var4 = 0.1F;
		GL11.glColor4f(var4, var4, var4, 0.5F);
		GL11.glPushMatrix();
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = 0.0078125F;
		float var11 = (float)(var2 % 16) / 256.0F - var10;
		float var12 = ((float)(var2 % 16) + 15.99F) / 256.0F + var10;
		float var13 = (float)(var2 / 16) / 256.0F - var10;
		float var14 = ((float)(var2 / 16) + 15.99F) / 256.0F + var10;
		var3.startDrawingQuads();
		var3.addVertexWithUV((double)var5, (double)var7, (double)var9, (double)var12, (double)var14);
		var3.addVertexWithUV((double)var6, (double)var7, (double)var9, (double)var11, (double)var14);
		var3.addVertexWithUV((double)var6, (double)var8, (double)var9, (double)var11, (double)var13);
		var3.addVertexWithUV((double)var5, (double)var8, (double)var9, (double)var12, (double)var13);
		var3.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderWarpedTextureOverlay(float var1) {
		Tessellator var2 = Tessellator.instance;
		float var3 = this.mc.thePlayer.getEntityBrightness(var1);
		GL11.glColor4f(var3, var3, var3, 0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		float var4 = 4.0F;
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = -this.mc.thePlayer.rotationYaw / 64.0F;
		float var11 = this.mc.thePlayer.rotationPitch / 64.0F;
		var2.startDrawingQuads();
		var2.addVertexWithUV((double)var5, (double)var7, (double)var9, (double)(var4 + var10), (double)(var4 + var11));
		var2.addVertexWithUV((double)var6, (double)var7, (double)var9, (double)(0.0F + var10), (double)(var4 + var11));
		var2.addVertexWithUV((double)var6, (double)var8, (double)var9, (double)(0.0F + var10), (double)(0.0F + var11));
		var2.addVertexWithUV((double)var5, (double)var8, (double)var9, (double)(var4 + var10), (double)(0.0F + var11));
		var2.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderFireInFirstPerson(float var1) {
		Tessellator var2 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float var3 = 1.0F;

		for(int var4 = 0; var4 < 2; ++var4) {
			GL11.glPushMatrix();
			int var5 = Block.fire.blockIndexInTexture + var4 * 16;
			int var6 = (var5 & 15) << 4;
			int var7 = var5 & 240;
			float var8 = (float)var6 / 256.0F;
			float var9 = ((float)var6 + 15.99F) / 256.0F;
			float var10 = (float)var7 / 256.0F;
			float var11 = ((float)var7 + 15.99F) / 256.0F;
			float var12 = (0.0F - var3) / 2.0F;
			float var13 = var12 + var3;
			float var14 = 0.0F - var3 / 2.0F;
			float var15 = var14 + var3;
			float var16 = -0.5F;
			GL11.glTranslatef((float)(-(var4 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((float)(var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			var2.startDrawingQuads();
			var2.addVertexWithUV((double)var12, (double)var14, (double)var16, (double)var9, (double)var11);
			var2.addVertexWithUV((double)var13, (double)var14, (double)var16, (double)var8, (double)var11);
			var2.addVertexWithUV((double)var13, (double)var15, (double)var16, (double)var8, (double)var10);
			var2.addVertexWithUV((double)var12, (double)var15, (double)var16, (double)var9, (double)var10);
			var2.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void func_895_a() {
		this.field_9452_d = this.field_9453_c;
		EntityPlayerSP var1 = this.mc.thePlayer;
		ItemStack var2 = var1.inventory.getCurrentItem();
		boolean var4 = this.field_20099_f == var1.inventory.currentItem && var2 == this.field_9451_b;
		if(this.field_9451_b == null && var2 == null) {
			var4 = true;
		}

		if(var2 != null && this.field_9451_b != null && var2 != this.field_9451_b && var2.itemID == this.field_9451_b.itemID) {
			this.field_9451_b = var2;
			var4 = true;
		}

		float var5 = 0.4F;
		float var6 = var4 ? 1.0F : 0.0F;
		float var7 = var6 - this.field_9453_c;
		if(var7 < -var5) {
			var7 = -var5;
		}

		if(var7 > var5) {
			var7 = var5;
		}

		this.field_9453_c += var7;
		if(this.field_9453_c < 0.1F) {
			this.field_9451_b = var2;
			this.field_20099_f = var1.inventory.currentItem;
		}

	}

	public void func_9449_b() {
		this.field_9453_c = 0.0F;
	}

	public void func_9450_c() {
		this.field_9453_c = 0.0F;
	}
}
