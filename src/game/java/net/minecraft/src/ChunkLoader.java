package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class ChunkLoader implements IChunkLoader {
	private VFile2 saveDir;
	private static final String hex = "0123456789ABCDEF";
	
	private Logger LOGGER = LogManager.getLogger();

	public ChunkLoader(VFile2 var1, boolean var2) {
		this.saveDir = var1;
	}

	private String chunkFileForXZ(int var1, int var2) {
		int unsignedX = var1 + 1900000;
		int unsignedZ = var2 + 1900000;
		
		char[] path = new char[12];
		for(int i = 5; i >= 0; --i) {
			path[i] = hex.charAt((unsignedX >>> (i << 2)) & 0xF);
			path[i + 6] = hex.charAt((unsignedZ >>> (i << 2)) & 0xF);
		}
		
		return new String(path);
	}

	public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
		VFile2 var4 = new VFile2(this.saveDir, this.chunkFileForXZ(var2, var3) + ".dat");
		if(var4 != null && var4.exists()) {
			try {
				NBTTagCompound var6;
				try (InputStream is = var4.getInputStream()) {
					var6 = CompressedStreamTools.readCompressed(is);
				}
				if(!var6.hasKey("Level")) {
					LOGGER.warn("Chunk file at {},{} is missing level data, skipping", var2, var3);
					return null;
				}

				if(!var6.getCompoundTag("Level").hasKey("Blocks")) {
					LOGGER.warn("Chunk file at {},{} is missing block data, skipping", var2, var3);
					return null;
				}

				Chunk var7 = loadChunkIntoWorldFromCompound(var1, var6.getCompoundTag("Level"));
				if(!var7.isAtLocation(var2, var3)) {
					LOGGER.info("Chunk file at {},{} is is in the wrong location; relocating. (Expected {}, {}, got {}, {})", var2, var3, var2, var3, var7.xPosition, var7.zPosition);
					var6.setInteger("xPos", var2);
					var6.setInteger("zPos", var3);
					var7 = loadChunkIntoWorldFromCompound(var1, var6.getCompoundTag("Level"));
				}

				return var7;
			} catch (Exception var8) {
				LOGGER.error(var8);
			}
		}

		return null;
	}

	public void saveChunk(World var1, Chunk var2) throws IOException {
		VFile2 var3 = new VFile2(this.saveDir, this.chunkFileForXZ(var2.xPosition, var2.zPosition) + ".dat");
		if(var3.exists()) {
			var1.sizeOnDisk -= var3.length();
		}

		try {
			NBTTagCompound var6 = new NBTTagCompound();
			NBTTagCompound var7 = new NBTTagCompound();
			var6.setTag("Level", var7);
			this.storeChunkInCompound(var2, var1, var7);
			
			try (OutputStream os = var3.getOutputStream()) {
				CompressedStreamTools.writeGzippedCompoundToOutputStream(var6, os);
			}
			
			var1.sizeOnDisk += var3.length();
		} catch (Exception var8) {
			LOGGER.error(var8);
		}

	}

	public void storeChunkInCompound(Chunk var1, World var2, NBTTagCompound var3) {
		var3.setInteger("xPos", var1.xPosition);
		var3.setInteger("zPos", var1.zPosition);
		var3.setLong("LastUpdate", var2.worldTime);
		var3.setByteArray("Blocks", var1.blocks);
		var3.setByteArray("Data", var1.data.data);
		var3.setByteArray("SkyLight", var1.skylightMap.data);
		var3.setByteArray("BlockLight", var1.blocklightMap.data);
		var3.setByteArray("HeightMap", var1.heightMap);
		var3.setBoolean("TerrainPopulated", var1.isTerrainPopulated);
		var1.hasEntities = false;
		NBTTagList var4 = new NBTTagList();

		NBTTagCompound var8;
		for(int var5 = 0, var6 = var1.mapSize; var5 < var6; ++var5) {
			List<Entity> list = var1.entities.get(var5);

			for(int i = 0, j = list.size(); i < j; ++i) {
				Entity var7 = (Entity)list.get(i);
				var1.hasEntities = true;
				var8 = new NBTTagCompound();
				if(var7.func_358_c(var8)) {
					var4.setTag(var8);
				}
			}
		}

		var3.setTag("Entities", var4);
		NBTTagList var9 = new NBTTagList();
		Iterator<TileEntity> var6 = var1.chunkTileEntityMap.values().iterator();

		while(var6.hasNext()) {
			TileEntity var10 = var6.next();
			var8 = new NBTTagCompound();
			var10.writeToNBT(var8);
			var9.setTag(var8);
		}

		var3.setTag("TileEntities", var9);
	}

	public static Chunk loadChunkIntoWorldFromCompound(World var0, NBTTagCompound var1) {
		int var2 = var1.getInteger("xPos");
		int var3 = var1.getInteger("zPos");
		Chunk var4 = new Chunk(var0, var2, var3);
		var4.blocks = var1.getByteArray("Blocks");
		var4.data = new NibbleArray(var1.getByteArray("Data"));
		var4.skylightMap = new NibbleArray(var1.getByteArray("SkyLight"));
		var4.blocklightMap = new NibbleArray(var1.getByteArray("BlockLight"));
		var4.heightMap = var1.getByteArray("HeightMap");
		var4.isTerrainPopulated = var1.getBoolean("TerrainPopulated");
		if(!var4.data.isValid()) {
			var4.data = new NibbleArray(var4.blocks.length);
		}

		if(var4.heightMap == null || !var4.skylightMap.isValid()) {
			var4.heightMap = new byte[256];
			var4.skylightMap = new NibbleArray(var4.blocks.length);
			var4.func_1024_c();
		}

		if(!var4.blocklightMap.isValid()) {
			var4.blocklightMap = new NibbleArray(var4.blocks.length);
			var4.func_1014_a();
		}

		NBTTagList var5 = var1.getTagList("Entities");
		if(var5 != null) {
			for(int var6 = 0; var6 < var5.tagCount(); ++var6) {
				NBTTagCompound var7 = (NBTTagCompound)var5.tagAt(var6);
				Entity var8 = EntityList.createEntityFromNBT(var7, var0);
				var4.hasEntities = true;
				if(var8 != null) {
					var4.addEntity(var8);
				}
			}
		}

		NBTTagList var10 = var1.getTagList("TileEntities");
		if(var10 != null) {
			for(int var11 = 0; var11 < var10.tagCount(); ++var11) {
				NBTTagCompound var12 = (NBTTagCompound)var10.tagAt(var11);
				TileEntity var9 = TileEntity.createAndLoadEntity(var12);
				if(var9 != null) {
					var4.func_1001_a(var9);
				}
			}
		}

		return var4;
	}

	public void func_814_a() {
	}

	public void saveExtraData() {
	}

	public void saveExtraChunkData(World var1, Chunk var2) throws IOException {
	}
}
