package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet255KickDisconnect extends Packet {
	public String reason;

	public Packet255KickDisconnect() {
	}

	public Packet255KickDisconnect(String var1) {
		this.reason = var1;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.reason = var1.readUTF();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeUTF(this.reason);
	}

	public void processPacket(NetHandler var1) {
		var1.handleKickDisconnect(this);
	}

	public int getPacketSize() {
		return this.reason.length();
	}
}
