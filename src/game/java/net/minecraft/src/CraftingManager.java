package net.minecraft.src;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.carrotsearch.hppc.CharIntHashMap;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class CraftingManager {
	private static final CraftingManager instance = new CraftingManager();
	private ObjectArrayList<CraftingRecipe> recipes = new ObjectArrayList<>();
	private static CharIntHashMap temp;
	private Logger LOGGER = LogManager.getLogger();

	public static final CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		(new RecipesTools()).addRecipes(this);
		(new RecipesWeapons()).addRecipes(this);
		(new RecipesIngots()).addRecipes(this);
		(new RecipesFood()).addRecipes(this);
		(new RecipesCrafting()).addRecipes(this);
		(new RecipesArmor()).addRecipes(this);
		this.addRecipe(new ItemStack(Item.paper, 3), new Object[]{"###", '#', Item.reed});
		this.addRecipe(new ItemStack(Item.book, 1), new Object[]{"#", "#", "#", '#', Item.paper});
		this.addRecipe(new ItemStack(Block.fence, 2), new Object[]{"###", "###", '#', Item.stick});
		this.addRecipe(new ItemStack(Block.jukebox, 1), new Object[]{"###", "#X#", "###", '#', Block.planks, 'X', Item.diamond});
		this.addRecipe(new ItemStack(Block.bookShelf, 1), new Object[]{"###", "XXX", "###", '#', Block.planks, 'X', Item.book});
		this.addRecipe(new ItemStack(Block.blockSnow, 1), new Object[]{"##", "##", '#', Item.snowball});
		this.addRecipe(new ItemStack(Block.blockClay, 1), new Object[]{"##", "##", '#', Item.clay});
		this.addRecipe(new ItemStack(Block.brick, 1), new Object[]{"##", "##", '#', Item.brick});
		this.addRecipe(new ItemStack(Block.lightStone, 1), new Object[]{"###", "###", "###", '#', Item.lightStoneDust});
		this.addRecipe(new ItemStack(Block.cloth, 1), new Object[]{"###", "###", "###", '#', Item.silk});
		this.addRecipe(new ItemStack(Block.tnt, 1), new Object[]{"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
		this.addRecipe(new ItemStack(Block.stairSingle, 3), new Object[]{"###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Block.ladder, 1), new Object[]{"# #", "###", "# #", '#', Item.stick});
		this.addRecipe(new ItemStack(Item.doorWood, 1), new Object[]{"##", "##", "##", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.doorSteel, 1), new Object[]{"##", "##", "##", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Item.sign, 1), new Object[]{"###", "###", " X ", '#', Block.planks, 'X', Item.stick});
		this.addRecipe(new ItemStack(Block.planks, 4), new Object[]{"#", '#', Block.wood});
		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.torchWood, 4), new Object[]{"X", "#", 'X', Item.coal, '#', Item.stick});
		this.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{"# #", " # ", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.minecartTrack, 16), new Object[]{"X X", "X#X", "X X", 'X', Item.ingotIron, '#', Item.stick});
		this.addRecipe(new ItemStack(Item.minecartEmpty, 1), new Object[]{"# #", "###", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Block.pumpkinLantern, 1), new Object[]{"A", "B", 'A', Block.pumpkin, 'B', Block.torchWood});
		this.addRecipe(new ItemStack(Item.minecartCrate, 1), new Object[]{"A", "B", 'A', Block.crate, 'B', Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.minecartPowered, 1), new Object[]{"A", "B", 'A', Block.stoneOvenIdle, 'B', Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.boat, 1), new Object[]{"# #", "###", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.bucketEmpty, 1), new Object[]{"# #", " # ", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Item.flintAndSteel, 1), new Object[]{"A ", " B", 'A', Item.ingotIron, 'B', Item.flint});
		this.addRecipe(new ItemStack(Item.bread, 1), new Object[]{"###", '#', Item.wheat});
		this.addRecipe(new ItemStack(Block.stairCompactPlanks, 4), new Object[]{"#  ", "## ", "###", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.fishingRod, 1), new Object[]{"  #", " #X", "# X", '#', Item.stick, 'X', Item.silk});
		this.addRecipe(new ItemStack(Block.stairCompactCobblestone, 4), new Object[]{"#  ", "## ", "###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Item.painting, 1), new Object[]{"###", "#X#", "###", '#', Item.stick, 'X', Block.cloth});
		this.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed});
		this.addRecipe(new ItemStack(Block.lever, 1), new Object[]{"X", "#", '#', Block.cobblestone, 'X', Item.stick});
		this.addRecipe(new ItemStack(Block.torchRedstoneActive, 1), new Object[]{"X", "#", '#', Item.stick, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Item.pocketSundial, 1), new Object[]{" # ", "#X#", " # ", '#', Item.ingotGold, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Item.compass, 1), new Object[]{" # ", "#X#", " # ", '#', Item.ingotIron, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Block.button, 1), new Object[]{"#", "#", '#', Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlateStone, 1), new Object[]{"###", '#', Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlatePlanks, 1), new Object[]{"###", '#', Block.planks});
		this.recipes.sort(RecipeSorter.instance);
		LOGGER.info("{} recipes", this.recipes.size());
	}

	void addRecipe(ItemStack var1, Object... var2) {
		String var3 = "";
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;
		if(var2[var4] instanceof String[]) {
			String[] var11 = (String[])((String[])var2[var4++]);

			for(int var8 = 0; var8 < var11.length; ++var8) {
				String var9 = var11[var8];
				++var6;
				var5 = var9.length();
				var3 = var3 + var9;
			}
		} else {
			while(var2[var4] instanceof String) {
				String var7 = (String)var2[var4++];
				++var6;
				var5 = var7.length();
				var3 = var3 + var7;
			}
		}

		int var15;
		if (temp == null) {
			temp = new CharIntHashMap(); //workaround for a crash in TeaVM when this is initialized outside this function as a final object.
		}
		
		if (!temp.isEmpty()) {
			temp.clear();
		}
		for(; var4 < var2.length; var4 += 2) {
			char var13 = (char)var2[var4];
			var15 = 0;
			if(var2[var4 + 1] instanceof Item) {
				var15 = ((Item)var2[var4 + 1]).shiftedIndex;
			} else if(var2[var4 + 1] instanceof Block) {
				var15 = ((Block)var2[var4 + 1]).blockID;
			}

			temp.put(var13, var15);
		}

		int[] var14 = new int[var5 * var6];

		for(var15 = 0; var15 < var5 * var6; ++var15) {
			char var10 = var3.charAt(var15);
			if(temp.containsKey(var10)) {
				var14[var15] = temp.get(var10);
			} else {
				var14[var15] = -1;
			}
		}

		this.recipes.add(new CraftingRecipe(var5, var6, var14, var1));
	}

	public ItemStack craft(int[] var1) {
		for(int var2 = 0; var2 < this.recipes.size(); ++var2) {
			CraftingRecipe var3 = this.recipes.get(var2);
			if(var3.matchRecipe(var1)) {
				return var3.createResult(var1);
			}
		}

		return null;
	}
}
