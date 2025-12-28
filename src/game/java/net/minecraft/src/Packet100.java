package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet100 extends Packet {
	public int field_20038_a;
	public int field_20037_b;
	public String field_20040_c;
	public int field_20039_d;

	public void processPacket(NetHandler var1) {
		var1.func_20087_a(this);
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_20038_a = var1.readByte();
		this.field_20037_b = var1.readByte();
		this.field_20040_c = var1.readUTF();
		this.field_20039_d = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeByte(this.field_20038_a);
		var1.writeByte(this.field_20037_b);
		var1.writeUTF(this.field_20040_c);
		var1.writeByte(this.field_20039_d);
	}

	public int getPacketSize() {
		return 3 + this.field_20040_c.length();
	}
}
