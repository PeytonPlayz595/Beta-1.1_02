package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet7 extends Packet {
	public int field_9277_a;
	public int field_9276_b;
	public int field_9278_c;

	public Packet7() {
	}

	public Packet7(int var1, int var2, int var3) {
		this.field_9277_a = var1;
		this.field_9276_b = var2;
		this.field_9278_c = var3;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_9277_a = var1.readInt();
		this.field_9276_b = var1.readInt();
		this.field_9278_c = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.field_9277_a);
		var1.writeInt(this.field_9276_b);
		var1.writeByte(this.field_9278_c);
	}

	public void processPacket(NetHandler var1) {
		var1.func_6499_a(this);
	}

	public int getPacketSize() {
		return 9;
	}
}
