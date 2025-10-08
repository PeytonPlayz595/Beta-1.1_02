package net.minecraft.src;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class ChunkProviderLoadOrGenerate implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	
	private Long2ObjectMap<Chunk> chunks = new Long2ObjectOpenHashMap<>(1024);
	
	private World worldObj;
	int lastQueriedChunkXPos = -999999999;
	int lastQueriedChunkZPos = -999999999;
	private Chunk lastQueriedChunk;
	
	private Logger LOGGER = LogManager.getLogger();

	public ChunkProviderLoadOrGenerate(World var1, IChunkLoader var2, IChunkProvider var3) {
		this.blankChunk = new Chunk(var1, new byte[-Short.MIN_VALUE], 0, 0);
		this.blankChunk.field_1524_q = true;
		this.blankChunk.neverSave = true;
		this.worldObj = var1;
		this.chunkLoader = var2;
		this.chunkProvider = var3;
	}

	public boolean chunkExists(int var1, int var2) {
		if(var1 == this.lastQueriedChunkXPos && var2 == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return true;
		} else {
			Chunk chunk = chunks.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
			dummyChunk = chunk;
			return chunk != null && (chunk == this.blankChunk || chunk.isAtLocation(var1, var2));
		}
	}

	private Chunk dummyChunk = null; //Avoid redundant chunk lookups
	public Chunk provideChunk(int var1, int var2) {
		if(var1 == this.lastQueriedChunkXPos && var2 == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return this.lastQueriedChunk;
		} else {
			Chunk chunk;
			if(!this.chunkExists(var1, var2)) {
				chunk = dummyChunk;
				if(chunk != null) {
					chunk.onChunkUnload();
					this.saveChunk(chunk);
					this.saveExtraChunkData(chunk);
				}

				Chunk var6 = this.func_542_c(var1, var2);
				if(var6 == null) {
					if(this.chunkProvider == null) {
						var6 = this.blankChunk;
					} else {
						var6 = this.chunkProvider.provideChunk(var1, var2);
					}
				}

				this.chunks.put(ChunkCoordIntPair.chunkXZ2Int(var1, var2), var6);
				chunk = var6;
				var6.func_4143_d();
				if(chunk != null) {
					chunk.onChunkLoad();
				}

				if(chunk != null && !chunk.isTerrainPopulated && this.chunkExists(var1 + 1, var2 + 1) && this.chunkExists(var1, var2 + 1) && this.chunkExists(var1 + 1, var2)) {
					this.populate(this, var1, var2);
				}
				
				int i = var1 - 1;
				int j = var2 + 1;
				int k = var2 - 1;

				if(this.chunkExists(i, var2) && !this.provideChunk(i, var2).isTerrainPopulated && this.chunkExists(i, j) && this.chunkExists(var1, j) && this.chunkExists(i, var2)) {
					this.populate(this, i, var2);
				}

				if(this.chunkExists(var1, k) && !this.provideChunk(var1, k).isTerrainPopulated && this.chunkExists(var1 + 1, k) && this.chunkExists(var1, k) && this.chunkExists(var1 + 1, var2)) {
					this.populate(this, var1, k);
				}

				if(this.chunkExists(i, k) && !this.provideChunk(i, k).isTerrainPopulated && this.chunkExists(i, k) && this.chunkExists(var1, k) && this.chunkExists(i, var2)) {
					this.populate(this, i, k);
				}
			} else {
				chunk = dummyChunk;
			}

			this.lastQueriedChunkXPos = var1;
			this.lastQueriedChunkZPos = var2;
			this.lastQueriedChunk = chunk;
			return chunk;
		}
	}

	private Chunk func_542_c(int var1, int var2) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, var1, var2);
				if(var3 != null) {
					var3.lastSaveTime = this.worldObj.worldTime;
				}

				return var3;
			} catch (Exception var4) {
				LOGGER.error(var4);
				return null;
			}
		}
	}

	private void saveExtraChunkData(Chunk var1) {
		if(this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.worldObj, var1);
			} catch (Exception var3) {
				LOGGER.debug(var3);
			}

		}
	}

	private void saveChunk(Chunk var1) {
		if(this.chunkLoader != null) {
			try {
				var1.lastSaveTime = this.worldObj.worldTime;
				this.chunkLoader.saveChunk(this.worldObj, var1);
			} catch (IOException var3) {
				LOGGER.error(var3);
			}

		}
	}

	public void populate(IChunkProvider var1, int var2, int var3) {
		Chunk var4 = this.provideChunk(var2, var3);
		if(!var4.isTerrainPopulated) {
			var4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(var1, var2, var3);
				var4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		int var3 = 0;
		int var4 = 0;
		int var5;
		if(var2 != null) {
			for(Chunk chunk : this.chunks.values()) {
				if(chunk != null && chunk.needsSaving(var1)) {
					++var4;
				}
			}
		}

		var5 = 0;

		for(Chunk chunk : this.chunks.values()) {
			if(chunk != null) {
				if(var1 && !chunk.neverSave) {
					this.saveExtraChunkData(chunk);
				}

				if(chunk.needsSaving(var1)) {
					this.saveChunk(chunk);
					chunk.isModified = false;
					++var3;
					if(var3 == 2 && !var1) {
						return false;
					}

					if(var2 != null) {
						++var5;
						if(var5 % 10 == 0) {
							var2.setLoadingProgress(var5 * 100 / var4);
						}
					}
				}
			}
		}

		if(var1) {
			if(this.chunkLoader == null) {
				return true;
			}

			this.chunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean func_532_a() {
		if(this.chunkLoader != null) {
			this.chunkLoader.func_814_a();
		}

		return this.chunkProvider.func_532_a();
	}

	public boolean func_536_b() {
		return true;
	}
}
