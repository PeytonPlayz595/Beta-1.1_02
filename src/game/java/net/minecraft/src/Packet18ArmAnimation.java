package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet18ArmAnimation extends Packet {
	public int entityId;
	public int animate;

	public Packet18ArmAnimation() {
	}

	public Packet18ArmAnimation(Entity var1, int var2) {
		this.entityId = var1.field_620_ab;
		this.animate = var2;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.entityId = var1.readInt();
		this.animate = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeByte(this.animate);
	}

	public void processPacket(NetHandler var1) {
		var1.handleArmAnimation(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
