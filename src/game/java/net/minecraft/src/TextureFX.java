package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.peyton.eagler.minecraft.TextureLocation;

public class TextureFX {
	public byte[] field_1127_a = new byte[1024];
	public int field_1126_b;
	public boolean field_1131_c = false;
	public int field_1130_d = 0;
	public int field_1129_e = 1;
	public int field_1128_f = 0;

	public TextureFX(int var1) {
		this.field_1126_b = var1;
	}

	public void func_783_a() {
	}

	public void func_782_a(RenderEngine var1) {
		if(this.field_1128_f == 0) {
			TextureLocation.terrain.bindTexture();
		} else if(this.field_1128_f == 1) {
			TextureLocation.items.bindTexture();
		}

	}
}
