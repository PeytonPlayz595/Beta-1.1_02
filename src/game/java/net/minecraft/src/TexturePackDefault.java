package net.minecraft.src;

import net.lax1dude.eaglercraft.opengl.ImageData;
import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class TexturePackDefault extends TexturePackBase {
	private int texturePackName = -1;
	private ImageData texturePackThumbnail;
	private static Logger LOGGER = LogManager.getLogger();

	public TexturePackDefault() {
		this.texturePackFileName = "Default";
		this.firstDescriptionLine = "The default look of Minecraft";

		try {
			this.texturePackThumbnail = ImageData.loadImageFile("/pack.png").swapRB();
		} catch (Exception var2) {
			LOGGER.error(var2);
		}

	}

	public void func_6484_b(Minecraft var1) {
		if(this.texturePackThumbnail != null) {
			var1.renderEngine.deleteTexture(this.texturePackName);
		}

	}

	public void func_6483_c(Minecraft var1) {
		if(this.texturePackThumbnail != null && this.texturePackName < 0) {
			this.texturePackName = var1.renderEngine.allocateAndSetupTexture(this.texturePackThumbnail);
		}

		if(this.texturePackThumbnail != null) {
			var1.renderEngine.bindTexture(this.texturePackName);
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.renderEngine.getTexture("/gui/unknown_pack.png"));
		}

	}
}
