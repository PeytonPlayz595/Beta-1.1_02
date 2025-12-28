package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet105 extends Packet {
	public int field_20032_a;
	public int field_20031_b;
	public int field_20033_c;

	public void processPacket(NetHandler var1) {
		var1.func_20090_a(this);
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_20032_a = var1.readByte();
		this.field_20031_b = var1.readShort();
		this.field_20033_c = var1.readShort();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeByte(this.field_20032_a);
		var1.writeShort(this.field_20031_b);
		var1.writeShort(this.field_20033_c);
	}

	public int getPacketSize() {
		return 5;
	}
}
