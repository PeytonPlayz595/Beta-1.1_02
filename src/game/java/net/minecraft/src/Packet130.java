package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet130 extends Packet {
	public int field_20020_a;
	public int field_20019_b;
	public int field_20022_c;
	public String[] field_20021_d;

	public Packet130() {
		this.isChunkDataPacket = true;
	}

	public Packet130(int var1, int var2, int var3, String[] var4) {
		this.isChunkDataPacket = true;
		this.field_20020_a = var1;
		this.field_20019_b = var2;
		this.field_20022_c = var3;
		this.field_20021_d = var4;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_20020_a = var1.readInt();
		this.field_20019_b = var1.readShort();
		this.field_20022_c = var1.readInt();
		this.field_20021_d = new String[4];

		for(int var2 = 0; var2 < 4; ++var2) {
			this.field_20021_d[var2] = var1.readUTF();
		}

	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.field_20020_a);
		var1.writeShort(this.field_20019_b);
		var1.writeInt(this.field_20022_c);

		for(int var2 = 0; var2 < 4; ++var2) {
			var1.writeUTF(this.field_20021_d[var2]);
		}

	}

	public void processPacket(NetHandler var1) {
		var1.func_20093_a(this);
	}

	public int getPacketSize() {
		int var1 = 0;

		for(int var2 = 0; var2 < 4; ++var2) {
			var1 += this.field_20021_d[var2].length();
		}

		return var1;
	}
}
