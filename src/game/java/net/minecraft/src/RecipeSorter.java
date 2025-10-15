package net.minecraft.src;

import java.util.Comparator;

class RecipeSorter implements Comparator<CraftingRecipe> {
	
	public static final RecipeSorter instance = new RecipeSorter();
	
	private RecipeSorter() {
	}

	public int compare(CraftingRecipe var1, CraftingRecipe var2) {
		return var2.getRecipeSize() < var1.getRecipeSize() ? -1 : (var2.getRecipeSize() > var1.getRecipeSize() ? 1 : 0);
	}
}
