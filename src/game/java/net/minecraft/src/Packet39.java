package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet39 extends Packet {
	public int field_6365_a;
	public int field_6364_b;

	public int getPacketSize() {
		return 8;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_6365_a = var1.readInt();
		this.field_6364_b = var1.readInt();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.field_6365_a);
		var1.writeInt(this.field_6364_b);
	}

	public void processPacket(NetHandler var1) {
		var1.func_6497_a(this);
	}
}
