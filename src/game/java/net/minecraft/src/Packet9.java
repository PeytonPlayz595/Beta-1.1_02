package net.minecraft.src;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet9 extends Packet {
	public void processPacket(NetHandler var1) {
		var1.func_9448_a(this);
	}

	public void readPacketData(PacketBuffer var1) {
	}

	public void writePacketData(PacketBuffer var1) {
	}

	public int getPacketSize() {
		return 0;
	}
}
