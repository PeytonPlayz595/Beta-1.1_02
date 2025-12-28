package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet22Collect extends Packet {
	public int collectedEntityId;
	public int collectorEntityId;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.collectedEntityId = var1.readInt();
		this.collectorEntityId = var1.readInt();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.collectedEntityId);
		var1.writeInt(this.collectorEntityId);
	}

	public void processPacket(NetHandler var1) {
		var1.handleCollect(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
