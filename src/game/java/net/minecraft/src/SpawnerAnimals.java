package net.minecraft.src;

import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongSet;
import com.carrotsearch.hppc.cursors.LongCursor;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SpawnerAnimals {
	private static LongSet eligibleChunksForSpawning = new LongHashSet();
	private static Logger LOGGER = LogManager.getLogger();

	protected static ChunkPosition getRandomSpawningPointInChunk(World var0, int var1, int var2) {
		int var3 = var1 + var0.rand.nextInt(16);
		int var4 = var0.rand.nextInt(128);
		int var5 = var2 + var0.rand.nextInt(16);
		return new ChunkPosition(var3, var4, var5);
	}

	public static final int performSpawning(World var0) {
		eligibleChunksForSpawning.clear();

		int var1;
		for(var1 = 0; var1 < var0.playerEntities.size(); ++var1) {
			EntityPlayer var2 = (EntityPlayer)var0.playerEntities.get(var1);
			int var3 = MathHelper.floor_double(var2.posX / 16.0D);
			int var4 = MathHelper.floor_double(var2.posZ / 16.0D);
			byte var5 = 8;

			for(int var6 = -var5; var6 <= var5; ++var6) {
				for(int var7 = -var5; var7 <= var5; ++var7) {
					long chunkcoordintpair = ChunkCoordIntPair.chunkXZ2Int(var6 + var3, var7 + var4);
					eligibleChunksForSpawning.add(chunkcoordintpair);
				}
			}
		}

		var1 = 0;

		for(int var28 = 0; var28 < EnumCreatureType.values().length; ++var28) {
			EnumCreatureType var29 = EnumCreatureType.values()[var28];
			if(var0.countEntities(var29.field_4278_c) <= var29.maxNumberOfEntityType * eligibleChunksForSpawning.size() / 256) {

				label110: for(LongCursor chunkcoordintpair1 : eligibleChunksForSpawning) {
					long chunkcoordintpair1l = chunkcoordintpair1.value;
					int chunkXPos = (int) (chunkcoordintpair1l & 4294967295L);
					int chunkZPos = (int) (chunkcoordintpair1l >>> 32);
					int var8;
					int var10;
					int var11;
					int var12;
					ObjectArrayList<EntitySupplier<Entity>> var33;
					do {
						do {
							do {
								do {
									MobSpawnerBase var32 = var0.func_4075_a().func_4074_a(chunkXPos, chunkZPos);
									var33 = var32.getEntitiesForType(var29);
								} while(var33 == null);
							} while(var33.size() == 0);

							var8 = var0.rand.nextInt(var33.size());
							ChunkPosition var9 = getRandomSpawningPointInChunk(var0, chunkXPos * 16, chunkZPos * 16);
							var10 = var9.x;
							var11 = var9.y;
							var12 = var9.z;
						} while(var0.isBlockOpaqueCube(var10, var11, var12));
					} while(var0.getBlockMaterial(var10, var11, var12) != Material.air);

					int var13 = 0;

					for(int var14 = 0; var14 < 3; ++var14) {
						int var15 = var10;
						int var16 = var11;
						int var17 = var12;
						byte var18 = 6;

						for(int var19 = 0; var19 < 4; ++var19) {
							var15 += var0.rand.nextInt(var18) - var0.rand.nextInt(var18);
							var16 += var0.rand.nextInt(1) - var0.rand.nextInt(1);
							var17 += var0.rand.nextInt(var18) - var0.rand.nextInt(var18);
							if(var0.isBlockOpaqueCube(var15, var16 - 1, var17) && !var0.isBlockOpaqueCube(var15, var16, var17) && !var0.getBlockMaterial(var15, var16, var17).getIsLiquid() && !var0.isBlockOpaqueCube(var15, var16 + 1, var17)) {
								float var20 = (float)var15 + 0.5F;
								float var21 = (float)var16;
								float var22 = (float)var17 + 0.5F;
								if(var0.getClosestPlayer((double)var20, (double)var21, (double)var22, 24.0D) == null) {
									float var23 = var20 - (float)var0.spawnX;
									float var24 = var21 - (float)var0.spawnY;
									float var25 = var22 - (float)var0.spawnZ;
									float var26 = var23 * var23 + var24 * var24 + var25 * var25;
									if(var26 >= 576.0F) {
										EntityLiving var34;
										try {
											var34 = (EntityLiving)var33.get(var8).createEntity(var0);
										} catch (Exception var27) {
											LOGGER.debug(var27);
											return var1;
										}

										var34.setLocationAndAngles((double)var20, (double)var21, (double)var22, var0.rand.nextFloat() * 360.0F, 0.0F);
										if(var34.getCanSpawnHere()) {
											++var13;
											var0.entityJoinedWorld(var34);
											if(var34 instanceof EntitySpider && var0.rand.nextInt(100) == 0) {
												EntitySkeleton var35 = new EntitySkeleton(var0);
												var35.setLocationAndAngles((double)var20, (double)var21, (double)var22, var34.rotationYaw, 0.0F);
												var0.entityJoinedWorld(var35);
												var35.mountEntity(var34);
											}

											if(var13 >= var34.getMaxSpawnedInChunk()) {
												continue label110;
											}
										}

										var1 += var13;
									}
								}
							}
						}
					}
				}
			}
		}

		return var1;
	}
}
