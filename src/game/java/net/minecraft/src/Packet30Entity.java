package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet30Entity extends Packet {
	public int entityId;
	public byte xPosition;
	public byte yPosition;
	public byte zPosition;
	public byte yaw;
	public byte pitch;
	public boolean rotating = false;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.entityId = var1.readInt();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.entityId);
	}

	public void processPacket(NetHandler var1) {
		var1.handleEntity(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
