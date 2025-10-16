package net.minecraft.src;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.EaglerZLIB;

public class CompressedStreamTools {
	public static NBTTagCompound readCompressed(InputStream var0) throws IOException {
		try (DataInputStream datainputstream = new DataInputStream(EaglerZLIB.newGZIPInputStream(var0))) {
			return read(datainputstream);
		}
	}

	public static void writeGzippedCompoundToOutputStream(NBTTagCompound var0, OutputStream var1) throws IOException {
		try (DataOutputStream dataoutputstream = new DataOutputStream(EaglerZLIB.newGZIPOutputStream(var1))) {
			write(var0, dataoutputstream);
		}
	}

	public static NBTTagCompound read(DataInput var0) throws IOException {
		NBTBase var1 = NBTBase.readTag(var0);
		if (var1 instanceof NBTTagCompound) {
			return (NBTTagCompound) var1;
		} else {
			throw new IOException("Root tag must be a named compound tag");
		}
	}

	public static void write(NBTTagCompound var0, DataOutput var1) throws IOException {
		NBTBase.writeTag(var0, var1);
	}
}
