package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet5PlayerInventory extends Packet {
	public int type;
	public int stacks;
	public int field_20044_c;

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.type = var1.readInt();
		this.stacks = var1.readShort();
		this.field_20044_c = var1.readShort();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.type);
		var1.writeShort(this.stacks);
		var1.writeShort(this.field_20044_c);
	}

	public void processPacket(NetHandler var1) {
		var1.handlePlayerInventory(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
