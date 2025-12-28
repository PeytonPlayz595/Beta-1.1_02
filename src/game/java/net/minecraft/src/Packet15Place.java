package net.minecraft.src;

import java.io.IOException;

import net.peyton.eagler.minecraft.network.PacketBuffer;

public class Packet15Place extends Packet {
	public int id;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public ItemStack direction;

	public Packet15Place() {
	}

	public Packet15Place(int var1, int var2, int var3, int var4, ItemStack var5) {
		this.id = var1;
		this.xPosition = var2;
		this.yPosition = var3;
		this.zPosition = var4;
		this.direction = var5;
	}

	public void readPacketData(PacketBuffer var1) throws IOException {
		this.id = var1.readInt();
		this.xPosition = var1.read();
		this.yPosition = var1.readInt();
		this.zPosition = var1.read();
		short var2 = var1.readShort();
		if(var2 >= 0) {
			byte var3 = var1.readByte();
			byte var4 = var1.readByte();
			this.direction = new ItemStack(var2, var3, var4);
		} else {
			this.direction = null;
		}

	}

	public void writePacketData(PacketBuffer var1) throws IOException {
		var1.writeInt(this.id);
		var1.write(this.xPosition);
		var1.writeInt(this.yPosition);
		var1.write(this.zPosition);
		if(this.direction == null) {
			var1.writeShort(-1);
		} else {
			var1.writeShort(this.direction.itemID);
			var1.writeByte(this.direction.stackSize);
			var1.writeByte(this.direction.itemDamage);
		}

	}

	public void processPacket(NetHandler var1) {
		var1.handlePlace(this);
	}

	public int getPacketSize() {
		return 14;
	}
}
