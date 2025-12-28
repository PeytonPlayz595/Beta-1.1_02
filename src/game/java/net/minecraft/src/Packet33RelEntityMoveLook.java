package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet33RelEntityMoveLook extends Packet30Entity {
	public Packet33RelEntityMoveLook() {
		this.rotating = true;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		super.readPacketData(var1);
		this.xPosition = var1.readByte();
		this.yPosition = var1.readByte();
		this.zPosition = var1.readByte();
		this.yaw = var1.readByte();
		this.pitch = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		super.writePacketData(var1);
		var1.writeByte(this.xPosition);
		var1.writeByte(this.yPosition);
		var1.writeByte(this.zPosition);
		var1.writeByte(this.yaw);
		var1.writeByte(this.pitch);
	}

	public int getPacketSize() {
		return 9;
	}
}
