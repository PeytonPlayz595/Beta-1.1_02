package net.minecraft.src;

public class MobSpawnerHell extends MobSpawnerBase {
	public MobSpawnerHell() {
		this.biomeMonsters.add(EntityGhast::new);
		this.biomeMonsters.add(EntityPigZombie::new);
	}
}
