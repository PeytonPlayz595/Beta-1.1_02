package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet32EntityLook extends Packet30Entity {
	public Packet32EntityLook() {
		this.rotating = true;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		super.readPacketData(var1);
		this.yaw = var1.readByte();
		this.pitch = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		super.writePacketData(var1);
		var1.writeByte(this.yaw);
		var1.writeByte(this.pitch);
	}

	public int getPacketSize() {
		return 6;
	}
}
