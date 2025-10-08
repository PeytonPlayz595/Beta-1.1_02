package net.minecraft.src;

public enum EnumCreatureType {
	monster(IMobs.class, 100),
	creature(EntityAnimals.class, 20);

	public final Class<?> field_4278_c;
	public final int maxNumberOfEntityType;

	private EnumCreatureType(Class<?> var3, int var4) {
		this.field_4278_c = var3;
		this.maxNumberOfEntityType = var4;
	}
}
