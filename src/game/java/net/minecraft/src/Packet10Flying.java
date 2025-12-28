package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet10Flying extends Packet {
	public double xPosition;
	public double yPosition;
	public double zPosition;
	public double stance;
	public float yaw;
	public float pitch;
	public boolean onGround;
	public boolean moving;
	public boolean rotating;

	public Packet10Flying() {
	}

	public Packet10Flying(boolean var1) {
		this.onGround = var1;
	}

	public void processPacket(NetHandler var1) {
		var1.handleFlying(this);
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.onGround = var1.read() != 0;
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.write(this.onGround ? 1 : 0);
	}

	public int getPacketSize() {
		return 1;
	}
}
