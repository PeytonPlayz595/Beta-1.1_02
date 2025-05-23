package net.minecraft.src;

import net.lax1dude.eaglercraft.Random;

public class WorldGenHellLava extends WorldGenerator {
	private int field_4158_a;

	public WorldGenHellLava(int var1) {
		this.field_4158_a = var1;
	}

	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		if(var1.getBlockId(var3, var4 + 1, var5) != Block.bloodStone.blockID) {
			return false;
		} else if(var1.getBlockId(var3, var4, var5) != 0 && var1.getBlockId(var3, var4, var5) != Block.bloodStone.blockID) {
			return false;
		} else {
			int var6 = 0;
			if(var1.getBlockId(var3 - 1, var4, var5) == Block.bloodStone.blockID) {
				++var6;
			}

			if(var1.getBlockId(var3 + 1, var4, var5) == Block.bloodStone.blockID) {
				++var6;
			}

			if(var1.getBlockId(var3, var4, var5 - 1) == Block.bloodStone.blockID) {
				++var6;
			}

			if(var1.getBlockId(var3, var4, var5 + 1) == Block.bloodStone.blockID) {
				++var6;
			}

			if(var1.getBlockId(var3, var4 - 1, var5) == Block.bloodStone.blockID) {
				++var6;
			}

			int var7 = 0;
			if(var1.func_20084_d(var3 - 1, var4, var5)) {
				++var7;
			}

			if(var1.func_20084_d(var3 + 1, var4, var5)) {
				++var7;
			}

			if(var1.func_20084_d(var3, var4, var5 - 1)) {
				++var7;
			}

			if(var1.func_20084_d(var3, var4, var5 + 1)) {
				++var7;
			}

			if(var1.func_20084_d(var3, var4 - 1, var5)) {
				++var7;
			}

			if(var6 == 4 && var7 == 1) {
				var1.setBlockWithNotify(var3, var4, var5, this.field_4158_a);
				var1.field_4214_a = true;
				Block.blocksList[this.field_4158_a].updateTick(var1, var3, var4, var5, var2);
				var1.field_4214_a = false;
			}

			return true;
		}
	}
}
