package net.minecraft.src;

public class MetadataChunkBlock {
	public final EnumSkyBlock field_1299_a;
	public int field_1298_b;
	public int field_1304_c;
	public int field_1303_d;
	public int field_1302_e;
	public int field_1301_f;
	public int field_1300_g;

	public MetadataChunkBlock(EnumSkyBlock var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		this.field_1299_a = var1;
		this.field_1298_b = var2;
		this.field_1304_c = var3;
		this.field_1303_d = var4;
		this.field_1302_e = var5;
		this.field_1301_f = var6;
		this.field_1300_g = var7;
	}

	public void func_4127_a(World var1) {
		int var2 = this.field_1302_e - this.field_1298_b + 1;
		int var3 = this.field_1301_f - this.field_1304_c + 1;
		int var4 = this.field_1300_g - this.field_1303_d + 1;
		int var5 = var2 * var3 * var4;
		if(var5 <= -Short.MIN_VALUE) {
			for(int var6 = this.field_1298_b; var6 <= this.field_1302_e; ++var6) {
				for(int var7 = this.field_1303_d; var7 <= this.field_1300_g; ++var7) {
					if(var1.blockExists(var6, 0, var7)) {
						for(int var8 = this.field_1304_c; var8 <= this.field_1301_f; ++var8) {
							if(var8 >= 0 && var8 < 128) {
								int var9 = var1.getSavedLightValue(this.field_1299_a, var6, var8, var7);
								int var11 = var1.getBlockId(var6, var8, var7);
								int var12 = Block.lightOpacity[var11];
								if(var12 == 0) {
									var12 = 1;
								}

								int var13 = 0;
								if(this.field_1299_a == EnumSkyBlock.Sky) {
									if(var1.canExistingBlockSeeTheSky(var6, var8, var7)) {
										var13 = 15;
									}
								} else if(this.field_1299_a == EnumSkyBlock.Block) {
									var13 = Block.lightValue[var11];
								}

								int var14;
								int var20;
								if(var12 >= 15 && var13 == 0) {
									var20 = 0;
								} else {
									var14 = var1.getSavedLightValue(this.field_1299_a, var6 - 1, var8, var7);
									int var15 = var1.getSavedLightValue(this.field_1299_a, var6 + 1, var8, var7);
									int var16 = var1.getSavedLightValue(this.field_1299_a, var6, var8 - 1, var7);
									int var17 = var1.getSavedLightValue(this.field_1299_a, var6, var8 + 1, var7);
									int var18 = var1.getSavedLightValue(this.field_1299_a, var6, var8, var7 - 1);
									int var19 = var1.getSavedLightValue(this.field_1299_a, var6, var8, var7 + 1);
									var20 = var14;
									if(var15 > var14) {
										var20 = var15;
									}

									if(var16 > var20) {
										var20 = var16;
									}

									if(var17 > var20) {
										var20 = var17;
									}

									if(var18 > var20) {
										var20 = var18;
									}

									if(var19 > var20) {
										var20 = var19;
									}

									var20 -= var12;
									if(var20 < 0) {
										var20 = 0;
									}

									if(var13 > var20) {
										var20 = var13;
									}
								}

								if(var9 != var20) {
									var1.setLightValue(this.field_1299_a, var6, var8, var7, var20);
									var14 = var20 - 1;
									if(var14 < 0) {
										var14 = 0;
									}

									var1.neighborLightPropagationChanged(this.field_1299_a, var6 - 1, var8, var7, var14);
									var1.neighborLightPropagationChanged(this.field_1299_a, var6, var8 - 1, var7, var14);
									var1.neighborLightPropagationChanged(this.field_1299_a, var6, var8, var7 - 1, var14);
									if(var6 + 1 >= this.field_1302_e) {
										var1.neighborLightPropagationChanged(this.field_1299_a, var6 + 1, var8, var7, var14);
									}

									if(var8 + 1 >= this.field_1301_f) {
										var1.neighborLightPropagationChanged(this.field_1299_a, var6, var8 + 1, var7, var14);
									}

									if(var7 + 1 >= this.field_1300_g) {
										var1.neighborLightPropagationChanged(this.field_1299_a, var6, var8, var7 + 1, var14);
									}
								}
							}
						}
					}
				}
			}

		}
	}

	public boolean func_866_a(int var1, int var2, int var3, int var4, int var5, int var6) {
		if(var1 >= this.field_1298_b && var2 >= this.field_1304_c && var3 >= this.field_1303_d && var4 <= this.field_1302_e && var5 <= this.field_1301_f && var6 <= this.field_1300_g) {
			return true;
		} else {
			byte var7 = 1;
			if(var1 >= this.field_1298_b - var7 && var2 >= this.field_1304_c - var7 && var3 >= this.field_1303_d - var7 && var4 <= this.field_1302_e + var7 && var5 <= this.field_1301_f + var7 && var6 <= this.field_1300_g + var7) {
				int var8 = this.field_1302_e - this.field_1298_b;
				int var9 = this.field_1301_f - this.field_1304_c;
				int var10 = this.field_1300_g - this.field_1303_d;
				if(var1 > this.field_1298_b) {
					var1 = this.field_1298_b;
				}

				if(var2 > this.field_1304_c) {
					var2 = this.field_1304_c;
				}

				if(var3 > this.field_1303_d) {
					var3 = this.field_1303_d;
				}

				if(var4 < this.field_1302_e) {
					var4 = this.field_1302_e;
				}

				if(var5 < this.field_1301_f) {
					var5 = this.field_1301_f;
				}

				if(var6 < this.field_1300_g) {
					var6 = this.field_1300_g;
				}

				int var11 = var4 - var1;
				int var12 = var5 - var2;
				int var13 = var6 - var3;
				int var14 = var8 * var9 * var10;
				int var15 = var11 * var12 * var13;
				if(var15 - var14 <= 2) {
					this.field_1298_b = var1;
					this.field_1304_c = var2;
					this.field_1303_d = var3;
					this.field_1302_e = var4;
					this.field_1301_f = var5;
					this.field_1300_g = var6;
					return true;
				}
			}

			return false;
		}
	}
}
