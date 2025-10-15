package net.minecraft.src;

import java.io.IOException;
import java.util.List;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EagUtils;
import net.lax1dude.eaglercraft.internal.FileChooserResult;
import net.peyton.eagler.minecraft.ResourcePack;
import net.peyton.eagler.minecraft.Tessellator;

public class GuiTexturePacks extends GuiScreen {
	protected GuiScreen field_6461_a;
	private int field_6460_h = 0;
	private int field_6459_i = 32;
	private int field_6458_j = this.height - 55 + 4;
	private int field_6457_l = 0;
	private int field_6456_m = this.width;
	private int field_6455_n = -2;
	private int field_6454_o = -1;

	public GuiTexturePacks(GuiScreen var1) {
		this.field_6461_a = var1;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.func_20162_a();
		this.controlList.add(new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, var1.func_20163_a("Upload texture pack")));
		this.controlList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, var1.func_20163_a("gui.done")));
		this.mc.texturePackList.func_6532_a();
		this.field_6459_i = 32;
		this.field_6458_j = this.height - 58 + 4;
		this.field_6457_l = 0;
		this.field_6456_m = this.width;
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 5) {
				EagRuntime.displayFileChooser("application/zip", "zip");
			}

			if(var1.id == 6) {
				this.mc.renderEngine.refreshTextures();
				this.mc.displayGuiScreen(this.field_6461_a);
			}

		}
	}

	protected void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
	}

	protected void mouseMovedOrUp(int var1, int var2, int var3) {
		super.mouseMovedOrUp(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		if(this.field_6454_o <= 0) {
			this.mc.texturePackList.func_6532_a();
			this.field_6454_o += 20;
		}

		List<TexturePackBase> var4 = this.mc.texturePackList.availableTexturePacks();
		int var5;
		if(Mouse.isButtonDown(0)) {
			if(this.field_6455_n == -1) {
				if(var2 >= this.field_6459_i && var2 <= this.field_6458_j) {
					var5 = this.width / 2 - 110;
					int var6 = this.width / 2 + 110;
					int var7 = (var2 - this.field_6459_i + this.field_6460_h - 2) / 36;
					if(var1 >= var5 && var1 <= var6 && var7 >= 0 && var7 < var4.size() && this.mc.texturePackList.setTexturePack((TexturePackBase)var4.get(var7))) {
						this.mc.renderEngine.refreshTextures();
						
						ColorizerFoliage.init();
						ColorizerGrass.init();
					}

					this.field_6455_n = var2;
				} else {
					this.field_6455_n = -2;
				}
			} else if(this.field_6455_n >= 0) {
				this.field_6460_h -= var2 - this.field_6455_n;
				this.field_6455_n = var2;
			}
		} else {
			if(this.field_6455_n >= 0 && this.field_6455_n == var2) {
			}

			this.field_6455_n = -1;
		}

		var5 = var4.size() * 36 - (this.field_6458_j - this.field_6459_i - 4);
		if(var5 < 0) {
			var5 /= 2;
		}

		if(this.field_6460_h < 0) {
			this.field_6460_h = 0;
		}

		if(this.field_6460_h > var5) {
			this.field_6460_h = var5;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator var16 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var17 = 32.0F;
		var16.startDrawingQuads();
		var16.setColorOpaque_I(2105376);
		var16.addVertexWithUV((double)this.field_6457_l, (double)this.field_6458_j, 0.0D, (double)((float)this.field_6457_l / var17), (double)((float)(this.field_6458_j + this.field_6460_h) / var17));
		var16.addVertexWithUV((double)this.field_6456_m, (double)this.field_6458_j, 0.0D, (double)((float)this.field_6456_m / var17), (double)((float)(this.field_6458_j + this.field_6460_h) / var17));
		var16.addVertexWithUV((double)this.field_6456_m, (double)this.field_6459_i, 0.0D, (double)((float)this.field_6456_m / var17), (double)((float)(this.field_6459_i + this.field_6460_h) / var17));
		var16.addVertexWithUV((double)this.field_6457_l, (double)this.field_6459_i, 0.0D, (double)((float)this.field_6457_l / var17), (double)((float)(this.field_6459_i + this.field_6460_h) / var17));
		var16.draw();

		for(int var8 = 0; var8 < var4.size(); ++var8) {
			TexturePackBase var9 = (TexturePackBase)var4.get(var8);
			int var10 = this.width / 2 - 92 - 16;
			int var11 = 36 + var8 * 36 - this.field_6460_h;
			byte var12 = 32;
			byte var13 = 32;
			if(var9 == this.mc.texturePackList.selectedTexturePack) {
				int var14 = this.width / 2 - 110;
				int var15 = this.width / 2 + 110;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				var16.startDrawingQuads();
				var16.setColorOpaque_I(8421504);
				var16.addVertexWithUV((double)var14, (double)(var11 + var12 + 2), 0.0D, 0.0D, 1.0D);
				var16.addVertexWithUV((double)var15, (double)(var11 + var12 + 2), 0.0D, 1.0D, 1.0D);
				var16.addVertexWithUV((double)var15, (double)(var11 - 2), 0.0D, 1.0D, 0.0D);
				var16.addVertexWithUV((double)var14, (double)(var11 - 2), 0.0D, 0.0D, 0.0D);
				var16.setColorOpaque_I(0);
				var16.addVertexWithUV((double)(var14 + 1), (double)(var11 + var12 + 1), 0.0D, 0.0D, 1.0D);
				var16.addVertexWithUV((double)(var15 - 1), (double)(var11 + var12 + 1), 0.0D, 1.0D, 1.0D);
				var16.addVertexWithUV((double)(var15 - 1), (double)(var11 - 1), 0.0D, 1.0D, 0.0D);
				var16.addVertexWithUV((double)(var14 + 1), (double)(var11 - 1), 0.0D, 0.0D, 0.0D);
				var16.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			var9.func_6483_c(this.mc);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			var16.startDrawingQuads();
			var16.setColorOpaque_I(16777215);
			var16.addVertexWithUV((double)var10, (double)(var11 + var12), 0.0D, 0.0D, 1.0D);
			var16.addVertexWithUV((double)(var10 + var13), (double)(var11 + var12), 0.0D, 1.0D, 1.0D);
			var16.addVertexWithUV((double)(var10 + var13), (double)var11, 0.0D, 1.0D, 0.0D);
			var16.addVertexWithUV((double)var10, (double)var11, 0.0D, 0.0D, 0.0D);
			var16.draw();
			this.drawString(this.fontRenderer, var9.texturePackFileName, var10 + var13 + 2, var11 + 1, 16777215);
			this.drawString(this.fontRenderer, var9.firstDescriptionLine, var10 + var13 + 2, var11 + 12, 8421504);
			this.drawString(this.fontRenderer, var9.secondDescriptionLine, var10 + var13 + 2, var11 + 12 + 10, 8421504);
		}

		byte var18 = 4;
		this.func_6452_a(0, this.field_6459_i, 255, 255);
		this.func_6452_a(this.field_6458_j, this.height, 255, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV((double)this.field_6457_l, (double)(this.field_6459_i + var18), 0.0D, 0.0D, 1.0D);
		var16.addVertexWithUV((double)this.field_6456_m, (double)(this.field_6459_i + var18), 0.0D, 1.0D, 1.0D);
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV((double)this.field_6456_m, (double)this.field_6459_i, 0.0D, 1.0D, 0.0D);
		var16.addVertexWithUV((double)this.field_6457_l, (double)this.field_6459_i, 0.0D, 0.0D, 0.0D);
		var16.draw();
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV((double)this.field_6457_l, (double)this.field_6458_j, 0.0D, 0.0D, 1.0D);
		var16.addVertexWithUV((double)this.field_6456_m, (double)this.field_6458_j, 0.0D, 1.0D, 1.0D);
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV((double)this.field_6456_m, (double)(this.field_6458_j - var18), 0.0D, 1.0D, 0.0D);
		var16.addVertexWithUV((double)this.field_6457_l, (double)(this.field_6458_j - var18), 0.0D, 0.0D, 0.0D);
		var16.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		StringTranslate var19 = StringTranslate.func_20162_a();
		this.drawCenteredString(this.fontRenderer, var19.func_20163_a("texturePack.title"), this.width / 2, 16, 16777215);
		this.drawCenteredString(this.fontRenderer, var19.func_20163_a("texturePack.folderInfo"), this.width / 2 - 77, this.height - 26, 8421504);
		super.drawScreen(var1, var2, var3);
	}

	public void updateScreen() {
		super.updateScreen();
		--this.field_6454_o;
		
		if(EagRuntime.fileChooserHasResult()) {
			FileChooserResult result = EagRuntime.getFileChooserResult();
			this.mc.loadingScreen.displayLoadingString("Importing texture pack");
			EagUtils.sleep(1000l);
			try {
				ResourcePack.uploadResourcePack(result.fileName, result.fileData, this.mc.loadingScreen);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void func_6452_a(int var1, int var2, int var3, int var4) {
		Tessellator var5 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var6 = 32.0F;
		var5.startDrawingQuads();
		var5.setColorRGBA_I(4210752, var4);
		var5.addVertexWithUV(0.0D, (double)var2, 0.0D, 0.0D, (double)((float)var2 / var6));
		var5.addVertexWithUV((double)this.width, (double)var2, 0.0D, (double)((float)this.width / var6), (double)((float)var2 / var6));
		var5.setColorRGBA_I(4210752, var3);
		var5.addVertexWithUV((double)this.width, (double)var1, 0.0D, (double)((float)this.width / var6), (double)((float)var1 / var6));
		var5.addVertexWithUV(0.0D, (double)var1, 0.0D, 0.0D, (double)((float)var1 / var6));
		var5.draw();
	}
}