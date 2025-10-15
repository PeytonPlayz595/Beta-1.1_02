package net.minecraft.src;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.minecraft.client.Minecraft;

public class ColorizerFoliage {
	private static final int[] field_6529_a = new int[65536];
	private static Logger LOGGER = LogManager.getLogger();
	
	public static void init() {
		Arrays.fill(field_6529_a, 0);
		
		try {
			TexturePackBase pack = Minecraft.getMinecraft().renderEngine.field_6527_k.selectedTexturePack;
			String tex = "/misc/foliagecolor.png";
			ImageData img = ImageData.loadImageFile(pack != null ? pack.func_6481_a(tex) : EagRuntime.getRequiredResourceStream(tex)).swapRB();
			img.getRGB(0, 0, 256, 256, field_6529_a, 0, 256);
		} catch (Exception var1) {
			LOGGER.error(var1);
		}
	}

	public static int func_4146_a(double var0, double var2) {
		var2 *= var0;
		int var4 = (int)((1.0D - var0) * 255.0D);
		int var5 = (int)((1.0D - var2) * 255.0D);
		return field_6529_a[var5 << 8 | var4];
	}
}
