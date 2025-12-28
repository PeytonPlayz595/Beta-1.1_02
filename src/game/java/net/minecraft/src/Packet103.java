package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet103 extends Packet {
	public int field_20042_a;
	public int field_20041_b;
	public ItemStack field_20043_c;

	public void processPacket(NetHandler var1) {
		var1.func_20088_a(this);
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.field_20042_a = var1.readByte();
		this.field_20041_b = var1.readShort();
		short var2 = var1.readShort();
		if(var2 >= 0) {
			byte var3 = var1.readByte();
			byte var4 = var1.readByte();
			this.field_20043_c = new ItemStack(var2, var3, var4);
		} else {
			this.field_20043_c = null;
		}

	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeByte(this.field_20042_a);
		var1.writeShort(this.field_20041_b);
		if(this.field_20043_c == null) {
			var1.writeShort(-1);
		} else {
			var1.writeShort(this.field_20043_c.itemID);
			var1.writeByte(this.field_20043_c.stackSize);
			var1.writeByte(this.field_20043_c.itemDamage);
		}

	}

	public int getPacketSize() {
		return 7;
	}
}
