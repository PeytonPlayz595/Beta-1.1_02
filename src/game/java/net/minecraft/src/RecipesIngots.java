package net.minecraft.src;

public class RecipesIngots {
	private Object[][] recipeItems = new Object[][]{{Block.blockGold, Item.ingotGold}, {Block.blockSteel, Item.ingotIron}, {Block.blockDiamond, Item.diamond}};

	public void addRecipes(CraftingManager var1) {
		for(int var2 = 0; var2 < this.recipeItems.length; ++var2) {
			Block var3 = (Block)this.recipeItems[var2][0];
			Item var4 = (Item)this.recipeItems[var2][1];
			var1.addRecipe(new ItemStack(var3), new Object[]{"###", "###", "###", '#', var4});
			var1.addRecipe(new ItemStack(var4, 9), new Object[]{"#", '#', var3});
		}

	}
}
