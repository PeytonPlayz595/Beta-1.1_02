package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.lax1dude.eaglercraft.EagRuntime;
import net.peyton.eagler.minecraft.suppliers.PacketSupplier;

public abstract class Packet {
	private static Int2ObjectMap<PacketSupplier<Packet>> packetIdToClassMap = new Int2ObjectOpenHashMap<>();
	private static ObjectIntMap<Class<? extends Packet>> packetClassToIdMap = new ObjectIntHashMap<>();
	public final long field_20018_j = EagRuntime.steadyTimeMillis();
	public boolean isChunkDataPacket = false;
	
	private static Logger LOGGER = LogManager.getLogger();

	static void addIdClassMapping(int var0, Class<? extends Packet> var1, PacketSupplier<Packet> var2) {
		if(packetIdToClassMap.containsKey(var0)) {
			throw new IllegalArgumentException("Duplicate packet id:" + var0);
		} else if(packetClassToIdMap.containsKey(var1)) {
			throw new IllegalArgumentException("Duplicate packet class:" + var1);
		} else {
			packetIdToClassMap.put(var0, var2);
			packetClassToIdMap.put(var1, var0);
		}
	}

	public static Packet getNewPacket(int var0) {
		try {
			PacketSupplier<Packet> var1 = packetIdToClassMap.get(var0);
			return var1 == null ? null : var1.createPacket();
		} catch (Exception var2) {
			LOGGER.error("Skipping packet with id {}", var0);
			LOGGER.error(var2);
			return null;
		}
	}

	public final int getPacketId() {
		return packetClassToIdMap.get(this.getClass());
	}

	public static Packet readPacket(DataInputStream var0) throws IOException {
		int var1 = var0.read();
		if(var1 == -1) {
			return null;
		} else {
			Packet var2 = getNewPacket(var1);
			if(var2 == null) {
				throw new IOException("Bad packet id " + var1);
			} else {
				var2.readPacketData(var0);
				return var2;
			}
		}
	}

	public static void writePacket(Packet var0, DataOutputStream var1) throws IOException {
		var1.write(var0.getPacketId());
		var0.writePacketData(var1);
	}

	public abstract void readPacketData(DataInputStream var1) throws IOException;

	public abstract void writePacketData(DataOutputStream var1) throws IOException;

	public abstract void processPacket(NetHandler var1);

	public abstract int getPacketSize();

	static {
		addIdClassMapping(0, Packet0KeepAlive.class, Packet0KeepAlive::new);
		addIdClassMapping(1, Packet1Login.class, Packet1Login::new);
		addIdClassMapping(2, Packet2Handshake.class, Packet2Handshake::new);
		addIdClassMapping(3, Packet3Chat.class, Packet3Chat::new);
		addIdClassMapping(4, Packet4UpdateTime.class, Packet4UpdateTime::new);
		addIdClassMapping(5, Packet5PlayerInventory.class, Packet5PlayerInventory::new);
		addIdClassMapping(6, Packet6SpawnPosition.class, Packet6SpawnPosition::new);
		addIdClassMapping(7, Packet7.class, Packet7::new);
		addIdClassMapping(8, Packet8.class, Packet8::new);
		addIdClassMapping(9, Packet9.class, Packet9::new);
		addIdClassMapping(10, Packet10Flying.class, Packet10Flying::new);
		addIdClassMapping(11, Packet11PlayerPosition.class, Packet11PlayerPosition::new);
		addIdClassMapping(12, Packet12PlayerLook.class, Packet12PlayerLook::new);
		addIdClassMapping(13, Packet13PlayerLookMove.class, Packet13PlayerLookMove::new);
		addIdClassMapping(14, Packet14BlockDig.class, Packet14BlockDig::new);
		addIdClassMapping(15, Packet15Place.class, Packet15Place::new);
		addIdClassMapping(16, Packet16BlockItemSwitch.class, Packet16BlockItemSwitch::new);
		addIdClassMapping(18, Packet18ArmAnimation.class, Packet18ArmAnimation::new);
		addIdClassMapping(20, Packet20NamedEntitySpawn.class, Packet20NamedEntitySpawn::new);
		addIdClassMapping(21, Packet21PickupSpawn.class, Packet21PickupSpawn::new);
		addIdClassMapping(22, Packet22Collect.class, Packet22Collect::new);
		addIdClassMapping(23, Packet23VehicleSpawn.class, Packet23VehicleSpawn::new);
		addIdClassMapping(24, Packet24MobSpawn.class, Packet24MobSpawn::new);
		addIdClassMapping(28, Packet28.class, Packet28::new);
		addIdClassMapping(29, Packet29DestroyEntity.class, Packet29DestroyEntity::new);
		addIdClassMapping(30, Packet30Entity.class, Packet30Entity::new);
		addIdClassMapping(31, Packet31RelEntityMove.class, Packet31RelEntityMove::new);
		addIdClassMapping(32, Packet32EntityLook.class, Packet32EntityLook::new);
		addIdClassMapping(33, Packet33RelEntityMoveLook.class, Packet33RelEntityMoveLook::new);
		addIdClassMapping(34, Packet34EntityTeleport.class, Packet34EntityTeleport::new);
		addIdClassMapping(38, Packet38.class, Packet38::new);
		addIdClassMapping(39, Packet39.class, Packet39::new);
		addIdClassMapping(50, Packet50PreChunk.class, Packet50PreChunk::new);
		addIdClassMapping(51, Packet51MapChunk.class, Packet51MapChunk::new);
		addIdClassMapping(52, Packet52MultiBlockChange.class, Packet52MultiBlockChange::new);
		addIdClassMapping(53, Packet53BlockChange.class, Packet53BlockChange::new);
		addIdClassMapping(60, Packet60.class, Packet60::new);
		addIdClassMapping(100, Packet100.class, Packet100::new);
		addIdClassMapping(101, Packet101.class, Packet101::new);
		addIdClassMapping(102, Packet102.class, Packet102::new);
		addIdClassMapping(103, Packet103.class, Packet103::new);
		addIdClassMapping(104, Packet104.class, Packet104::new);
		addIdClassMapping(105, Packet105.class, Packet105::new);
		addIdClassMapping(106, Packet106.class, Packet106::new);
		addIdClassMapping(130, Packet130.class, Packet130::new);
		addIdClassMapping(255, Packet255KickDisconnect.class, Packet255KickDisconnect::new);
	}
}
