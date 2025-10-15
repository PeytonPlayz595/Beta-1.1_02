package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

public class EntityList {
	private static Map<String, EntitySupplier<Entity>> stringToClassMapping = new HashMap<String, EntitySupplier<Entity>>();
	private static Map<Class<? extends Entity>, String> classToStringMapping = new HashMap<Class<? extends Entity>, String>();
	private static Int2ObjectMap<EntitySupplier<Entity>> IDtoClassMapping = new Int2ObjectOpenHashMap<>();
	private static final ObjectIntMap<Class<? extends Entity>> classToIDMapping = new ObjectIntHashMap<>();
	
	private static Logger LOGGER = LogManager.getLogger();

	private static void addMapping(Class<? extends Entity> var0, EntitySupplier<Entity> var3, String var1, int var2) {
		stringToClassMapping.put(var1, var3);
		classToStringMapping.put(var0, var1);
		IDtoClassMapping.put(var2, var3);
		classToIDMapping.put(var0, var2);
	}

	public static Entity createEntityInWorld(String var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier<Entity> var3 = stringToClassMapping.get(var0);
			if(var3 != null) {
				var2 = var3.createEntity(var1);
			}
		} catch (Exception var4) {
			LOGGER.error(var4);
		}

		return var2;
	}

	public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier<Entity> var3 = stringToClassMapping.get(var0.getString("id"));
			if(var3 != null) {
				var2 = var3.createEntity(var1);
			}
		} catch (Exception var4) {
			LOGGER.error(var4);
		}

		if(var2 != null) {
			var2.readFromNBT(var0);
		} else {
			LOGGER.warn("Skipping Entity with id {}", var0.getString("id"));
		}

		return var2;
	}

	public static Entity createEntity(int var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier<Entity> var3 = IDtoClassMapping.get(var0);
			if(var3 != null) {
				var2 = var3.createEntity(var1);
			}
		} catch (Exception var4) {
			LOGGER.error(var4);
		}

		if(var2 == null) {
			LOGGER.warn("Skipping Entity with id {}", var0);
		}

		return var2;
	}

	public static int getEntityID(Entity var0) {
		return ((Integer)classToIDMapping.get(var0.getClass())).intValue();
	}

	public static String getEntityString(Entity var0) {
		return (String)classToStringMapping.get(var0.getClass());
	}

	static {
		addMapping(EntityArrow.class, EntityArrow::new, "Arrow", 10);
		addMapping(EntitySnowball.class, EntitySnowball::new, "Snowball", 11);
		addMapping(EntityItem.class, EntityItem::new, "Item", 1);
		addMapping(EntityPainting.class, EntityPainting::new, "Painting", 9);
		addMapping(EntityLiving.class, EntityLiving::new, "Mob", 48);
		addMapping(EntityMobs.class, EntityMobs::new, "Monster", 49);
		addMapping(EntityCreeper.class, EntityCreeper::new, "Creeper", 50);
		addMapping(EntitySkeleton.class, EntitySkeleton::new, "Skeleton", 51);
		addMapping(EntitySpider.class, EntitySpider::new, "Spider", 52);
		addMapping(EntityZombieSimple.class, EntityZombieSimple::new, "Giant", 53);
		addMapping(EntityZombie.class, EntityZombie::new, "Zombie", 54);
		addMapping(EntitySlime.class, EntitySlime::new, "Slime", 55);
		addMapping(EntityGhast.class, EntityGhast::new, "Ghast", 56);
		addMapping(EntityPigZombie.class, EntityPigZombie::new, "PigZombie", 57);
		addMapping(EntityPig.class, EntityPig::new, "Pig", 90);
		addMapping(EntitySheep.class, EntitySheep::new, "Sheep", 91);
		addMapping(EntityCow.class, EntityCow::new, "Cow", 92);
		addMapping(EntityChicken.class, EntityChicken::new, "Chicken", 93);
		addMapping(EntityTNTPrimed.class, EntityTNTPrimed::new, "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, EntityFallingSand::new, "FallingSand", 21);
		addMapping(EntityMinecart.class, EntityMinecart::new, "Minecart", 40);
		addMapping(EntityBoat.class, EntityBoat::new, "Boat", 41);
	}
}
