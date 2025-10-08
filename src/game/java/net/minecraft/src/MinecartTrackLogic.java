package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

class MinecartTrackLogic {
	private World worldObj;
	private int field_1165_c;
	private int field_1164_d;
	private int field_1163_e;
	private int field_1162_f;
	private List<ChunkPosition> field_1161_g;
	final BlockMinecartTrack field_1160_a;

	public MinecartTrackLogic(BlockMinecartTrack var1, World var2, int var3, int var4, int var5) {
		this.field_1160_a = var1;
		this.field_1161_g = new ArrayList<>();
		this.worldObj = var2;
		this.field_1165_c = var3;
		this.field_1164_d = var4;
		this.field_1163_e = var5;
		this.field_1162_f = var2.getBlockMetadata(var3, var4, var5);
		this.func_789_a();
	}

	private void func_789_a() {
		this.field_1161_g.clear();
		if(this.field_1162_f == 0) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e - 1));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e + 1));
		} else if(this.field_1162_f == 1) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c - 1, this.field_1164_d, this.field_1163_e));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c + 1, this.field_1164_d, this.field_1163_e));
		} else if(this.field_1162_f == 2) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c - 1, this.field_1164_d, this.field_1163_e));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c + 1, this.field_1164_d + 1, this.field_1163_e));
		} else if(this.field_1162_f == 3) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c - 1, this.field_1164_d + 1, this.field_1163_e));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c + 1, this.field_1164_d, this.field_1163_e));
		} else if(this.field_1162_f == 4) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d + 1, this.field_1163_e - 1));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e + 1));
		} else if(this.field_1162_f == 5) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e - 1));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d + 1, this.field_1163_e + 1));
		} else if(this.field_1162_f == 6) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c + 1, this.field_1164_d, this.field_1163_e));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e + 1));
		} else if(this.field_1162_f == 7) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c - 1, this.field_1164_d, this.field_1163_e));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e + 1));
		} else if(this.field_1162_f == 8) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c - 1, this.field_1164_d, this.field_1163_e));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e - 1));
		} else if(this.field_1162_f == 9) {
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c + 1, this.field_1164_d, this.field_1163_e));
			this.field_1161_g.add(new ChunkPosition(this.field_1165_c, this.field_1164_d, this.field_1163_e - 1));
		}

	}

	private void func_785_b() {
		for(int var1 = 0; var1 < this.field_1161_g.size(); ++var1) {
			MinecartTrackLogic var2 = this.func_795_a((ChunkPosition)this.field_1161_g.get(var1));
			if(var2 != null && var2.func_793_b(this)) {
				this.field_1161_g.set(var1, new ChunkPosition(var2.field_1165_c, var2.field_1164_d, var2.field_1163_e));
			} else {
				this.field_1161_g.remove(var1--);
			}
		}

	}

	private boolean func_784_a(int var1, int var2, int var3) {
		return this.worldObj.getBlockId(var1, var2, var3) == this.field_1160_a.blockID ? true : (this.worldObj.getBlockId(var1, var2 + 1, var3) == this.field_1160_a.blockID ? true : this.worldObj.getBlockId(var1, var2 - 1, var3) == this.field_1160_a.blockID);
	}

	private MinecartTrackLogic func_795_a(ChunkPosition var1) {
		return this.worldObj.getBlockId(var1.x, var1.y, var1.z) == this.field_1160_a.blockID ? new MinecartTrackLogic(this.field_1160_a, this.worldObj, var1.x, var1.y, var1.z) : (this.worldObj.getBlockId(var1.x, var1.y + 1, var1.z) == this.field_1160_a.blockID ? new MinecartTrackLogic(this.field_1160_a, this.worldObj, var1.x, var1.y + 1, var1.z) : (this.worldObj.getBlockId(var1.x, var1.y - 1, var1.z) == this.field_1160_a.blockID ? new MinecartTrackLogic(this.field_1160_a, this.worldObj, var1.x, var1.y - 1, var1.z) : null));
	}

	private boolean func_793_b(MinecartTrackLogic var1) {
		for(int var2 = 0; var2 < this.field_1161_g.size(); ++var2) {
			ChunkPosition var3 = (ChunkPosition)this.field_1161_g.get(var2);
			if(var3.x == var1.field_1165_c && var3.z == var1.field_1163_e) {
				return true;
			}
		}

		return false;
	}

	private boolean func_794_b(int var1, int var2, int var3) {
		for(int var4 = 0; var4 < this.field_1161_g.size(); ++var4) {
			ChunkPosition var5 = (ChunkPosition)this.field_1161_g.get(var4);
			if(var5.x == var1 && var5.z == var3) {
				return true;
			}
		}

		return false;
	}

	private int func_790_c() {
		int var1 = 0;
		if(this.func_784_a(this.field_1165_c, this.field_1164_d, this.field_1163_e - 1)) {
			++var1;
		}

		if(this.func_784_a(this.field_1165_c, this.field_1164_d, this.field_1163_e + 1)) {
			++var1;
		}

		if(this.func_784_a(this.field_1165_c - 1, this.field_1164_d, this.field_1163_e)) {
			++var1;
		}

		if(this.func_784_a(this.field_1165_c + 1, this.field_1164_d, this.field_1163_e)) {
			++var1;
		}

		return var1;
	}

	private boolean handleKeyPress(MinecartTrackLogic var1) {
		if(this.func_793_b(var1)) {
			return true;
		} else if(this.field_1161_g.size() == 2) {
			return false;
		} else if(this.field_1161_g.size() == 0) {
			return true;
		} else {
			ChunkPosition var2 = (ChunkPosition)this.field_1161_g.get(0);
			return var1.field_1164_d == this.field_1164_d && var2.y == this.field_1164_d ? true : true;
		}
	}

	private void func_788_d(MinecartTrackLogic var1) {
		this.field_1161_g.add(new ChunkPosition(var1.field_1165_c, var1.field_1164_d, var1.field_1163_e));
		boolean var2 = this.func_794_b(this.field_1165_c, this.field_1164_d, this.field_1163_e - 1);
		boolean var3 = this.func_794_b(this.field_1165_c, this.field_1164_d, this.field_1163_e + 1);
		boolean var4 = this.func_794_b(this.field_1165_c - 1, this.field_1164_d, this.field_1163_e);
		boolean var5 = this.func_794_b(this.field_1165_c + 1, this.field_1164_d, this.field_1163_e);
		byte var6 = -1;
		if(var2 || var3) {
			var6 = 0;
		}

		if(var4 || var5) {
			var6 = 1;
		}

		if(var3 && var5 && !var2 && !var4) {
			var6 = 6;
		}

		if(var3 && var4 && !var2 && !var5) {
			var6 = 7;
		}

		if(var2 && var4 && !var3 && !var5) {
			var6 = 8;
		}

		if(var2 && var5 && !var3 && !var4) {
			var6 = 9;
		}

		if(var6 == 0) {
			if(this.worldObj.getBlockId(this.field_1165_c, this.field_1164_d + 1, this.field_1163_e - 1) == this.field_1160_a.blockID) {
				var6 = 4;
			}

			if(this.worldObj.getBlockId(this.field_1165_c, this.field_1164_d + 1, this.field_1163_e + 1) == this.field_1160_a.blockID) {
				var6 = 5;
			}
		}

		if(var6 == 1) {
			if(this.worldObj.getBlockId(this.field_1165_c + 1, this.field_1164_d + 1, this.field_1163_e) == this.field_1160_a.blockID) {
				var6 = 2;
			}

			if(this.worldObj.getBlockId(this.field_1165_c - 1, this.field_1164_d + 1, this.field_1163_e) == this.field_1160_a.blockID) {
				var6 = 3;
			}
		}

		if(var6 < 0) {
			var6 = 0;
		}

		this.worldObj.setBlockMetadataWithNotify(this.field_1165_c, this.field_1164_d, this.field_1163_e, var6);
	}

	private boolean func_786_c(int var1, int var2, int var3) {
		MinecartTrackLogic var4 = this.func_795_a(new ChunkPosition(var1, var2, var3));
		if(var4 == null) {
			return false;
		} else {
			var4.func_785_b();
			return var4.handleKeyPress(this);
		}
	}

	public void func_792_a(boolean var1) {
		boolean var2 = this.func_786_c(this.field_1165_c, this.field_1164_d, this.field_1163_e - 1);
		boolean var3 = this.func_786_c(this.field_1165_c, this.field_1164_d, this.field_1163_e + 1);
		boolean var4 = this.func_786_c(this.field_1165_c - 1, this.field_1164_d, this.field_1163_e);
		boolean var5 = this.func_786_c(this.field_1165_c + 1, this.field_1164_d, this.field_1163_e);
		byte var6 = -1;
		if((var2 || var3) && !var4 && !var5) {
			var6 = 0;
		}

		if((var4 || var5) && !var2 && !var3) {
			var6 = 1;
		}

		if(var3 && var5 && !var2 && !var4) {
			var6 = 6;
		}

		if(var3 && var4 && !var2 && !var5) {
			var6 = 7;
		}

		if(var2 && var4 && !var3 && !var5) {
			var6 = 8;
		}

		if(var2 && var5 && !var3 && !var4) {
			var6 = 9;
		}

		if(var6 == -1) {
			if(var2 || var3) {
				var6 = 0;
			}

			if(var4 || var5) {
				var6 = 1;
			}

			if(var1) {
				if(var3 && var5) {
					var6 = 6;
				}

				if(var4 && var3) {
					var6 = 7;
				}

				if(var5 && var2) {
					var6 = 9;
				}

				if(var2 && var4) {
					var6 = 8;
				}
			} else {
				if(var2 && var4) {
					var6 = 8;
				}

				if(var5 && var2) {
					var6 = 9;
				}

				if(var4 && var3) {
					var6 = 7;
				}

				if(var3 && var5) {
					var6 = 6;
				}
			}
		}

		if(var6 == 0) {
			if(this.worldObj.getBlockId(this.field_1165_c, this.field_1164_d + 1, this.field_1163_e - 1) == this.field_1160_a.blockID) {
				var6 = 4;
			}

			if(this.worldObj.getBlockId(this.field_1165_c, this.field_1164_d + 1, this.field_1163_e + 1) == this.field_1160_a.blockID) {
				var6 = 5;
			}
		}

		if(var6 == 1) {
			if(this.worldObj.getBlockId(this.field_1165_c + 1, this.field_1164_d + 1, this.field_1163_e) == this.field_1160_a.blockID) {
				var6 = 2;
			}

			if(this.worldObj.getBlockId(this.field_1165_c - 1, this.field_1164_d + 1, this.field_1163_e) == this.field_1160_a.blockID) {
				var6 = 3;
			}
		}

		if(var6 < 0) {
			var6 = 0;
		}

		this.field_1162_f = var6;
		this.func_789_a();
		this.worldObj.setBlockMetadataWithNotify(this.field_1165_c, this.field_1164_d, this.field_1163_e, var6);

		for(int var7 = 0; var7 < this.field_1161_g.size(); ++var7) {
			MinecartTrackLogic var8 = this.func_795_a((ChunkPosition)this.field_1161_g.get(var7));
			if(var8 != null) {
				var8.func_785_b();
				if(var8.handleKeyPress(this)) {
					var8.func_788_d(this);
				}
			}
		}

	}

	static int func_791_a(MinecartTrackLogic var0) {
		return var0.func_790_c();
	}
}
