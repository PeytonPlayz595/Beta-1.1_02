package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet16BlockItemSwitch extends Packet {
	public int id;

	public Packet16BlockItemSwitch() {
	}

	public Packet16BlockItemSwitch(int var1) {
		this.id = var1;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.id = var1.readShort();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeShort(this.id);
	}

	public void processPacket(NetHandler var1) {
		var1.handleBlockItemSwitch(this);
	}

	public int getPacketSize() {
		return 2;
	}
}
