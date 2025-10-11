package net.minecraft.src;

public class ChunkCache implements IBlockAccess {
	protected int field_1060_a;
	protected int field_1059_b;
	private Chunk[][] field_1062_c;
	private World worldObj;

	public ChunkCache(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		this.worldObj = var1;
		this.field_1060_a = var2 >> 4;
		this.field_1059_b = var4 >> 4;
		int var8 = var5 >> 4;
		int var9 = var7 >> 4;
		this.field_1062_c = new Chunk[var8 - this.field_1060_a + 1][var9 - this.field_1059_b + 1];

		for(int var10 = this.field_1060_a; var10 <= var8; ++var10) {
			for(int var11 = this.field_1059_b; var11 <= var9; ++var11) {
				this.field_1062_c[var10 - this.field_1060_a][var11 - this.field_1059_b] = var1.getChunkFromChunkCoords(var10, var11);
			}
		}

	}

	public int getBlockId(int var1, int var2, int var3) {
		if(var2 < 0) {
			return 0;
		} else if(var2 >= 128) {
			return 0;
		} else {
			int var4 = (var1 >> 4) - this.field_1060_a;
			int var5 = (var3 >> 4) - this.field_1059_b;
			return this.field_1062_c[var4][var5].getBlockID(var1 & 15, var2, var3 & 15);
		}
	}

	public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
		int var4 = (var1 >> 4) - this.field_1060_a;
		int var5 = (var3 >> 4) - this.field_1059_b;
		return this.field_1062_c[var4][var5].getChunkBlockTileEntity(var1 & 15, var2, var3 & 15);
	}
	
	public int func_35451_b(int var1, int var2, int var3, int var4) {
		int var5 = this.func_35454_a(EnumSkyBlock.Sky, var1, var2, var3);
		int var6 = this.func_35454_a(EnumSkyBlock.Block, var1, var2, var3);
		if(var6 < var4) {
			var6 = var4;
		}

		return var5 << 20 | var6 << 4;
	}

	public float getLightBrightness(int var1, int var2, int var3) {
		return this.worldObj.worldProvider.lightBrightnessTable[this.func_4086_d(var1, var2, var3)];
	}

	public int func_4086_d(int var1, int var2, int var3) {
		return this.func_716_a(var1, var2, var3, true);
	}

	public int func_716_a(int var1, int var2, int var3, boolean var4) {
		if(var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
			int var5;
			int var6;
			if(var4) {
				var5 = this.getBlockId(var1, var2, var3);
				if(var5 == Block.stairSingle.blockID || var5 == Block.tilledField.blockID) {
					var6 = this.func_716_a(var1, var2 + 1, var3, false);
					int var7 = this.func_716_a(var1 + 1, var2, var3, false);
					int var8 = this.func_716_a(var1 - 1, var2, var3, false);
					int var9 = this.func_716_a(var1, var2, var3 + 1, false);
					int var10 = this.func_716_a(var1, var2, var3 - 1, false);
					if(var7 > var6) {
						var6 = var7;
					}

					if(var8 > var6) {
						var6 = var8;
					}

					if(var9 > var6) {
						var6 = var9;
					}

					if(var10 > var6) {
						var6 = var10;
					}

					return var6;
				}
			}

			if(var2 < 0) {
				return 0;
			} else if(var2 >= 128) {
				var5 = 15 - this.worldObj.skylightSubtracted;
				if(var5 < 0) {
					var5 = 0;
				}

				return var5;
			} else {
				var5 = (var1 >> 4) - this.field_1060_a;
				var6 = (var3 >> 4) - this.field_1059_b;
				return this.field_1062_c[var5][var6].getBlockLightValue(var1 & 15, var2, var3 & 15, this.worldObj.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public int getBlockMetadata(int var1, int var2, int var3) {
		if(var2 < 0) {
			return 0;
		} else if(var2 >= 128) {
			return 0;
		} else {
			int var4 = (var1 >> 4) - this.field_1060_a;
			int var5 = (var3 >> 4) - this.field_1059_b;
			return this.field_1062_c[var4][var5].getBlockMetadata(var1 & 15, var2, var3 & 15);
		}
	}

	public Material getBlockMaterial(int var1, int var2, int var3) {
		int var4 = this.getBlockId(var1, var2, var3);
		return var4 == 0 ? Material.air : Block.blocksList[var4].blockMaterial;
	}

	public boolean isBlockOpaqueCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public WorldChunkManager func_4075_a() {
		return this.worldObj.func_4075_a();
	}
	
	public int func_35454_a(EnumSkyBlock var1, int var2, int var3, int var4) {
		if(var3 < 0) {
			var3 = 0;
		}

		if(var3 >= 128) {
			var3 = 128 - 1;
		}

		if(var3 >= 0) {
			if(var3 < 128 && var2 >= -30000000 && var4 >= -30000000 && var2 < 30000000 && var4 <= 30000000) {
				int var5 = this.getBlockId(var2, var3, var4);
				int var6;
				if(var5 != Block.stairSingle.blockID && var5 != Block.tilledField.blockID && var5 != Block.stairCompactCobblestone.blockID && var5 != Block.stairCompactPlanks.blockID) {
					var5 = (var2 >> 4) - this.field_1060_a;
					var6 = (var4 >> 4) - this.field_1059_b;
					return this.field_1062_c[var5][var6].getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
				}

				var6 = this.func_35453_b(var1, var2, var3 + 1, var4);
				int var7 = this.func_35453_b(var1, var2 + 1, var3, var4);
				int var8 = this.func_35453_b(var1, var2 - 1, var3, var4);
				int var9 = this.func_35453_b(var1, var2, var3, var4 + 1);
				int var10 = this.func_35453_b(var1, var2, var3, var4 - 1);
				if(var7 > var6) {
					var6 = var7;
				}

				if(var8 > var6) {
					var6 = var8;
				}

				if(var9 > var6) {
					var6 = var9;
				}

				if(var10 > var6) {
					var6 = var10;
				}

				return var6;
			}
		}

		return var1.field_1722_c;
	}
	
	public int func_35453_b(EnumSkyBlock var1, int var2, int var3, int var4) {
		if(var3 < 0) {
			var3 = 0;
		}

		if(var3 >= 128) {
			var3 = 128 - 1;
		}

		if(var3 >= 0) {
			if(var3 < 128 && var2 >= -30000000 && var4 >= -30000000 && var2 < 30000000 && var4 <= 30000000) {
				int var5 = (var2 >> 4) - this.field_1060_a;
				int var6 = (var4 >> 4) - this.field_1059_b;
				return this.field_1062_c[var5][var6].getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
			}
		}

		return var1.field_1722_c;
	}
}
