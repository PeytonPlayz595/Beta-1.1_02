package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet4UpdateTime extends Packet {
	public long time;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.time = var1.readLong();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeLong(this.time);
	}

	public void processPacket(NetHandler var1) {
		var1.handleUpdateTime(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
