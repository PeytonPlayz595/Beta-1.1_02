package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet38 extends Packet {
	public int field_9274_a;
	public byte field_9273_b;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_9274_a = var1.readInt();
		this.field_9273_b = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.field_9274_a);
		var1.writeByte(this.field_9273_b);
	}

	public void processPacket(NetHandler var1) {
		var1.func_9447_a(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
