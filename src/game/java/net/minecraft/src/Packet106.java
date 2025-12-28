package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet106 extends Packet {
	public int field_20029_a;
	public short field_20028_b;
	public boolean field_20030_c;

	public Packet106() {
	}

	public Packet106(int var1, short var2, boolean var3) {
		this.field_20029_a = var1;
		this.field_20028_b = var2;
		this.field_20030_c = var3;
	}

	public void processPacket(NetHandler var1) {
		var1.func_20089_a(this);
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_20029_a = var1.readByte();
		this.field_20028_b = var1.readShort();
		this.field_20030_c = var1.readByte() != 0;
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeByte(this.field_20029_a);
		var1.writeShort(this.field_20028_b);
		var1.writeByte(this.field_20030_c ? 1 : 0);
	}

	public int getPacketSize() {
		return 4;
	}
}
