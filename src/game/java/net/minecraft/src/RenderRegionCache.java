package net.minecraft.src;

import java.util.Arrays;

public class RenderRegionCache extends ChunkCache {

	private final int positionX;
	private final int positionY;
	private final int positionZ;

	private static final int[] combinedLights = new int[8000];
	private static final int[] biomeColors = new int[1200];
	private static final int[] biomeColorsBlended = new int[768];

	public RenderRegionCache(World var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
		super(var1, var2, var3, var4, var5, var6, var7);
		this.positionX = var2 - var8;
		this.positionY = var3 - var8;
		this.positionZ = var4 - var8;

		Arrays.fill(combinedLights, -1);
		Arrays.fill(biomeColors, -1);
		Arrays.fill(biomeColorsBlended, -1);
	}

	private int getPositionIndex(int par1, int par2, int par3) {
		int i = par1 - positionX;
		int j = par2 - positionY;
		int k = par3 - positionZ;
		return i * 400 + k * 20 + j;
	}

	@Override
	public int func_35451_b(int var1, int var2, int var3, int var4) {
		int j = this.getPositionIndex(var1, var2, var3);
		int k = combinedLights[j];
		if (k == -1) {
			combinedLights[j] = k = calcLightBrightnessForSkyBlocks(var1, var2, var3);
		}
		return (k & 0xF00000) | Math.max(k & 0xF0, var4 << 4);
	}
	
	private int calcLightBrightnessForSkyBlocks(int var1, int var2, int var3) {
		int var5 = this.func_35454_a(EnumSkyBlock.Sky, var1, var2, var3);
		int var6 = this.func_35454_a(EnumSkyBlock.Block, var1, var2, var3);
		return var5 << 20 | var6 << 4;
	}
}
