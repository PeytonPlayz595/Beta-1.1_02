package net.minecraft.src;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.peyton.eagler.minecraft.suppliers.EntitySupplier;
import net.peyton.java.awt.Color;

public class MobSpawnerBase {
	public static final MobSpawnerBase rainforest = (new MobSpawnerBase()).func_4123_b(588342).setBiomeName("Rainforest").func_4124_a(2094168);
	public static final MobSpawnerBase swampland = (new MobSpawnerSwamp()).func_4123_b(522674).setBiomeName("Swampland").func_4124_a(9154376);
	public static final MobSpawnerBase seasonalForest = (new MobSpawnerBase()).func_4123_b(10215459).setBiomeName("Seasonal Forest");
	public static final MobSpawnerBase forest = (new MobSpawnerBase()).func_4123_b(353825).setBiomeName("Forest").func_4124_a(5159473);
	public static final MobSpawnerBase savanna = (new MobSpawnerDesert()).func_4123_b(14278691).setBiomeName("Savanna");
	public static final MobSpawnerBase shrubland = (new MobSpawnerBase()).func_4123_b(10595616).setBiomeName("Shrubland");
	public static final MobSpawnerBase taiga = (new MobSpawnerBase()).func_4123_b(3060051).setBiomeName("Taiga").func_4122_b().func_4124_a(8107825);
	public static final MobSpawnerBase desert = (new MobSpawnerDesert()).func_4123_b(16421912).setBiomeName("Desert");
	public static final MobSpawnerBase plains = (new MobSpawnerDesert()).func_4123_b(16767248).setBiomeName("Plains");
	public static final MobSpawnerBase iceDesert = (new MobSpawnerDesert()).func_4123_b(16772499).setBiomeName("Ice Desert").func_4122_b().func_4124_a(12899129);
	public static final MobSpawnerBase tundra = (new MobSpawnerBase()).func_4123_b(5762041).setBiomeName("Tundra").func_4122_b().func_4124_a(12899129);
	public static final MobSpawnerBase hell = (new MobSpawnerHell()).func_4123_b(16711680).setBiomeName("Hell");
	public String biomeName;
	public int field_6503_n;
	public byte topBlock = (byte)Block.grass.blockID;
	public byte fillerBlock = (byte)Block.dirt.blockID;
	public int field_6502_q = 5169201;
	protected ObjectArrayList<EntitySupplier<Entity>> biomeMonsters = new ObjectArrayList<>();
	protected ObjectArrayList<EntitySupplier<Entity>> biomeCreatures = new ObjectArrayList<>();
	private static MobSpawnerBase[] biomeLookupTable = new MobSpawnerBase[4096];

	public MobSpawnerBase() {
		biomeMonsters.add(EntitySpider::new);
		biomeMonsters.add(EntityZombie::new);
		biomeMonsters.add(EntitySkeleton::new);
		biomeMonsters.add(EntityCreeper::new);
		
		biomeCreatures.add(EntitySheep::new);
		biomeCreatures.add(EntityPig::new);
		biomeCreatures.add(EntityChicken::new);
		biomeCreatures.add(EntityCow::new);
	}
	
	public static void generateBiomeLookup() {
		for(int var0 = 0; var0 < 64; ++var0) {
			for(int var1 = 0; var1 < 64; ++var1) {
				biomeLookupTable[var0 + var1 * 64] = getBiome((float)var0 / 63.0F, (float)var1 / 63.0F);
			}
		}

		desert.topBlock = desert.fillerBlock = (byte)Block.sand.blockID;
		iceDesert.topBlock = iceDesert.fillerBlock = (byte)Block.sand.blockID;
	}

	protected MobSpawnerBase func_4122_b() {
		return this;
	}

	protected MobSpawnerBase setBiomeName(String var1) {
		this.biomeName = var1;
		return this;
	}

	protected MobSpawnerBase func_4124_a(int var1) {
		this.field_6502_q = var1;
		return this;
	}

	protected MobSpawnerBase func_4123_b(int var1) {
		this.field_6503_n = var1;
		return this;
	}

	public static MobSpawnerBase getBiomeFromLookup(double var0, double var2) {
		int var4 = (int)(var0 * 63.0D);
		int var5 = (int)(var2 * 63.0D);
		return biomeLookupTable[var4 + var5 * 64];
	}

	public static MobSpawnerBase getBiome(float var0, float var1) {
		var1 *= var0;
		return var0 < 0.1F ? tundra : (var1 < 0.2F ? (var0 < 0.5F ? tundra : (var0 < 0.95F ? savanna : desert)) : (var1 > 0.5F && var0 < 0.7F ? swampland : (var0 < 0.5F ? taiga : (var0 < 0.97F ? (var1 < 0.35F ? shrubland : forest) : (var1 < 0.45F ? plains : (var1 < 0.9F ? seasonalForest : rainforest))))));
	}

	public int getSkyColorByTemp(float var1) {
		var1 /= 3.0F;
		if(var1 < -1.0F) {
			var1 = -1.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		return Color.getHSBColor(224.0F / 360.0F - var1 * 0.05F, 0.5F + var1 * 0.1F, 1.0F).getRGB();
	}

	public ObjectArrayList<EntitySupplier<Entity>> getEntitiesForType(EnumCreatureType var1) {
		return var1 == EnumCreatureType.monster ? this.biomeMonsters : (var1 == EnumCreatureType.creature ? this.biomeCreatures : null);
	}

	static {
		generateBiomeLookup();
	}
}
