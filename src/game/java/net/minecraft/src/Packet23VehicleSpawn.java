package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet23VehicleSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int type;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.entityId = var1.readInt();
		this.type = var1.readByte();
		this.xPosition = var1.readInt();
		this.yPosition = var1.readInt();
		this.zPosition = var1.readInt();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeByte(this.type);
		var1.writeInt(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.writeInt(this.zPosition);
	}

	public void processPacket(NetHandler var1) {
		var1.handleVehicleSpawn(this);
	}

	public int getPacketSize() {
		return 17;
	}
}
