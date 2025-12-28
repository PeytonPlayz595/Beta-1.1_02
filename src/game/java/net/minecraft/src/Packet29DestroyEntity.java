package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet29DestroyEntity extends Packet {
	public int entityId;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.entityId = var1.readInt();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.entityId);
	}

	public void processPacket(NetHandler var1) {
		var1.handleDestroyEntity(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
