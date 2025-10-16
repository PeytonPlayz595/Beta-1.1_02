package net.minecraft.src;

import java.util.Comparator;

public class RenderSorter implements Comparator<WorldRenderer> {
	public static RenderSorter instance = new RenderSorter();
	
	private EntityPlayer field_4274_a;

	public RenderSorter() {
	}
	
	public RenderSorter setPlayer(EntityPlayer var1) {
		this.field_4274_a = var1;
		return this;
	}

	public int compare(WorldRenderer var1, WorldRenderer var2) {
		boolean var3 = var1.isInFrustrum;
		boolean var4 = var2.isInFrustrum;
		if (var3 && !var4) {
			return 1;
		} else if (var4 && !var3) {
			return -1;
		} else {
			double var5 = (double) var1.distanceToEntity(this.field_4274_a);
			double var7 = (double) var2.distanceToEntity(this.field_4274_a);
			return var5 < var7 ? 1 : (var5 > var7 ? -1 : (var1.field_1735_w < var2.field_1735_w ? 1 : -1));
		}
	}
}