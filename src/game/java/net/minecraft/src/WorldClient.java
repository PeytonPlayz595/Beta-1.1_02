package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class WorldClient extends World {
	private LinkedList<WorldBlockPositionType> field_1057_z = new LinkedList<>();
	private NetClientHandler sendQueue;
	private ChunkProviderClient C;
	private MCHashTable field_1055_D = new MCHashTable();
	private Set<Entity> E = new HashSet<>();
	private Set<Entity> field_1053_F = new HashSet<>();

	public WorldClient(NetClientHandler var1, long var2, int var4) {
		super("MpServer", WorldProvider.func_4101_a(var4), var2);
		this.sendQueue = var1;
		this.spawnX = 8;
		this.spawnY = 64;
		this.spawnZ = 8;
	}

	public void tick() {
		++this.worldTime;
		int var1 = this.calculateSkylightSubtracted(1.0F);
		int var2;
		if(var1 != this.skylightSubtracted) {
			this.skylightSubtracted = var1;

			for(var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
				((IWorldAccess)this.worldAccesses.get(var2)).updateAllRenderers();
			}
		}

		for(var2 = 0; var2 < 10 && !this.field_1053_F.isEmpty(); ++var2) {
			Entity var3 = (Entity)this.field_1053_F.iterator().next();
			if(!this.loadedEntityList.contains(var3)) {
				this.entityJoinedWorld(var3);
			}
		}

		this.sendQueue.processReadPackets();

		for(var2 = 0; var2 < this.field_1057_z.size(); ++var2) {
			WorldBlockPositionType var4 = (WorldBlockPositionType)this.field_1057_z.get(var2);
			if(--var4.field_1206_d == 0) {
				super.setBlockAndMetadata(var4.field_1202_a, var4.field_1201_b, var4.field_1207_c, var4.field_1205_e, var4.field_1204_f);
				super.markBlockNeedsUpdate(var4.field_1202_a, var4.field_1201_b, var4.field_1207_c);
				this.field_1057_z.remove(var2--);
			}
		}

	}

	public void func_711_c(int var1, int var2, int var3, int var4, int var5, int var6) {
		for(int var7 = 0; var7 < this.field_1057_z.size(); ++var7) {
			WorldBlockPositionType var8 = (WorldBlockPositionType)this.field_1057_z.get(var7);
			if(var8.field_1202_a >= var1 && var8.field_1201_b >= var2 && var8.field_1207_c >= var3 && var8.field_1202_a <= var4 && var8.field_1201_b <= var5 && var8.field_1207_c <= var6) {
				this.field_1057_z.remove(var7--);
			}
		}

	}

	protected IChunkProvider func_4081_a(VFile2 var1) {
		this.C = new ChunkProviderClient(this);
		return this.C;
	}

	public void func_4076_b() {
		this.spawnX = 8;
		this.spawnY = 64;
		this.spawnZ = 8;
	}

	protected void func_4080_j() {
	}

	public void scheduleBlockUpdate(int var1, int var2, int var3, int var4) {
	}

	public boolean TickUpdates(boolean var1) {
		return false;
	}

	public void func_713_a(int var1, int var2, boolean var3) {
		if(var3) {
			this.C.func_538_d(var1, var2);
		} else {
			this.C.func_539_c(var1, var2);
		}

		if(!var3) {
			this.func_701_b(var1 * 16, 0, var2 * 16, var1 * 16 + 15, 128, var2 * 16 + 15);
		}

	}

	public boolean entityJoinedWorld(Entity var1) {
		boolean var2 = super.entityJoinedWorld(var1);
		this.E.add(var1);
		if(!var2) {
			this.field_1053_F.add(var1);
		}

		return var2;
	}

	public void setEntityDead(Entity var1) {
		super.setEntityDead(var1);
		this.E.remove(var1);
	}

	protected void obtainEntitySkin(Entity var1) {
		super.obtainEntitySkin(var1);
		if(this.field_1053_F.contains(var1)) {
			this.field_1053_F.remove(var1);
		}

	}

	protected void releaseEntitySkin(Entity var1) {
		super.releaseEntitySkin(var1);
		if(this.E.contains(var1)) {
			this.field_1053_F.add(var1);
		}

	}

	public void func_712_a(int var1, Entity var2) {
		Entity var3 = this.func_709_b(var1);
		if(var3 != null) {
			this.setEntityDead(var3);
		}

		this.E.add(var2);
		var2.field_620_ab = var1;
		if(!this.entityJoinedWorld(var2)) {
			this.field_1053_F.add(var2);
		}

		this.field_1055_D.addKey(var1, var2);
	}

	public Entity func_709_b(int var1) {
		return (Entity)this.field_1055_D.lookup(var1);
	}

	public Entity removeEntityFromWorld(int var1) {
		Entity var2 = (Entity)this.field_1055_D.removeObject(var1);
		if(var2 != null) {
			this.E.remove(var2);
			this.setEntityDead(var2);
		}

		return var2;
	}

	public boolean setBlockMetadata(int var1, int var2, int var3, int var4) {
		int var5 = this.getBlockId(var1, var2, var3);
		int var6 = this.getBlockMetadata(var1, var2, var3);
		if(super.setBlockMetadata(var1, var2, var3, var4)) {
			this.field_1057_z.add(new WorldBlockPositionType(this, var1, var2, var3, var5, var6));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadata(int var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getBlockId(var1, var2, var3);
		int var7 = this.getBlockMetadata(var1, var2, var3);
		if(super.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
			this.field_1057_z.add(new WorldBlockPositionType(this, var1, var2, var3, var6, var7));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlock(int var1, int var2, int var3, int var4) {
		int var5 = this.getBlockId(var1, var2, var3);
		int var6 = this.getBlockMetadata(var1, var2, var3);
		if(super.setBlock(var1, var2, var3, var4)) {
			this.field_1057_z.add(new WorldBlockPositionType(this, var1, var2, var3, var5, var6));
			return true;
		} else {
			return false;
		}
	}

	public boolean func_714_c(int var1, int var2, int var3, int var4, int var5) {
		this.func_711_c(var1, var2, var3, var1, var2, var3);
		if(super.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
			this.notifyBlockChange(var1, var2, var3, var4);
			return true;
		} else {
			return false;
		}
	}

	public void sendQuittingDisconnectingPacket() {
		this.sendQueue.addToSendQueue(new Packet255KickDisconnect("Quitting"));
	}
}
