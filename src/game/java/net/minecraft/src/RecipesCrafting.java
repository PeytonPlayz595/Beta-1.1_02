package net.minecraft.src;

public class RecipesCrafting {
	public void addRecipes(CraftingManager var1) {
		var1.addRecipe(new ItemStack(Block.crate), new Object[]{"###", "# #", "###", '#', Block.planks});
		var1.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[]{"###", "# #", "###", '#', Block.cobblestone});
		var1.addRecipe(new ItemStack(Block.workbench), new Object[]{"##", "##", '#', Block.planks});
	}
}
