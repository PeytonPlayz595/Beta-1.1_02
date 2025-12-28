package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet8 extends Packet {
	public int healthMP;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.healthMP = var1.readShort();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeShort(this.healthMP);
	}

	public void processPacket(NetHandler var1) {
		var1.handleHealth(this);
	}

	public int getPacketSize() {
		return 2;
	}
}
