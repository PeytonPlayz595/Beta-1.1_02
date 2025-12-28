package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet1Login extends Packet {
	public int protocolVersion;
	public String username;
	public String password;
	public long field_4074_d;
	public byte field_4073_e;

	public Packet1Login() {
	}

	public Packet1Login(String var1, String var2, int var3) {
		this.username = var1;
		this.password = var2;
		this.protocolVersion = var3;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.protocolVersion = var1.readInt();
		this.username = var1.readUTF();
		this.password = var1.readUTF();
		this.field_4074_d = var1.readLong();
		this.field_4073_e = var1.readByte();
	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.protocolVersion);
		var1.writeUTF(this.username);
		var1.writeUTF(this.password);
		var1.writeLong(this.field_4074_d);
		var1.writeByte(this.field_4073_e);
	}

	public void processPacket(NetHandler var1) {
		var1.handleLogin(this);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + this.password.length() + 4 + 5;
	}
}
