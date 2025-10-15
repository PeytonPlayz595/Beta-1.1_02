package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import com.carrotsearch.hppc.IntArrayList;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;

public class GLAllocation {
	private static IntArrayList textureNames = new IntArrayList();

	public static int generateDisplayLists(int count) {
		return GL11.glGenLists(count);
	}
	
	public static void deleteDisplayLists(int list) {
		GL11.glDeleteLists(list);
	}
	
	public static int generateTextureName() {
		int i = EaglercraftGPU.generateTexture();
		textureNames.add(i);
		return i;
	}

	public static void deleteTexturesAndDisplayLists() {
		for (int i = 0, j = textureNames.size(); i < j; ++i) {
			GL11.glDeleteTexture(textureNames.get(i));
		}
		
		textureNames.clear();
	}

	public static ByteBuffer createDirectByteBuffer(int var0) {
		return EagRuntime.allocateByteBuffer(var0);
	}

	public static IntBuffer createDirectIntBuffer(int var0) {
		return EagRuntime.allocateIntBuffer(var0);
	}

	public static FloatBuffer createDirectFloatBuffer(int var0) {
		return EagRuntime.allocateFloatBuffer(var0);
	}
}
