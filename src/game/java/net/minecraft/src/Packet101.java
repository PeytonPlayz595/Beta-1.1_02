package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet101 extends Packet {
	public int field_20034_a;

	public Packet101() {
	}

	public Packet101(int var1) {
		this.field_20034_a = var1;
	}

	public void processPacket(NetHandler var1) {
		var1.func_20092_a(this);
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_20034_a = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeByte(this.field_20034_a);
	}

	public int getPacketSize() {
		return 1;
	}
}
