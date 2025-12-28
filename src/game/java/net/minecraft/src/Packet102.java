package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet102 extends Packet {
	public int field_20024_a;
	public int field_20023_b;
	public int field_20027_c;
	public short field_20026_d;
	public ItemStack field_20025_e;

	public Packet102() {
	}

	public Packet102(int var1, int var2, int var3, ItemStack var4, short var5) {
		this.field_20024_a = var1;
		this.field_20023_b = var2;
		this.field_20027_c = var3;
		this.field_20025_e = var4;
		this.field_20026_d = var5;
	}

	public void processPacket(NetHandler var1) {
		var1.func_20091_a(this);
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_20024_a = var1.readByte();
		this.field_20023_b = var1.readShort();
		this.field_20027_c = var1.readByte();
		this.field_20026_d = var1.readShort();
		short var2 = var1.readShort();
		if(var2 >= 0) {
			byte var3 = var1.readByte();
			byte var4 = var1.readByte();
			this.field_20025_e = new ItemStack(var2, var3, var4);
		} else {
			this.field_20025_e = null;
		}

	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeByte(this.field_20024_a);
		var1.writeShort(this.field_20023_b);
		var1.writeByte(this.field_20027_c);
		var1.writeShort(this.field_20026_d);
		if(this.field_20025_e == null) {
			var1.writeShort(-1);
		} else {
			var1.writeShort(this.field_20025_e.itemID);
			var1.writeByte(this.field_20025_e.stackSize);
			var1.writeByte(this.field_20025_e.itemDamage);
		}

	}

	public int getPacketSize() {
		return 10;
	}
}
