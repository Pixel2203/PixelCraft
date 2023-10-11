package com.example.examplemod.API.brewing.kettle.recipe;

import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredients;
import com.example.examplemod.API.brewing.kettle.items.KettleResults;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class KettleRecipes {

    public static KettleRecipe TestKettleRecipe = new KettleRecipe(KettleResults.DIAMONDS_5.stack, KettleIngredients.BLAZE_ROD.ingredient,KettleIngredients.GLOWSTONE_DUST.ingredient);
    public static KettleRecipe TestKettleRecipe2 = new KettleRecipe(KettleResults.DIARRHEA_POTION.stack, KettleIngredients.GLOWSTONE_DUST.ingredient,KettleIngredients.BLAZE_ROD.ingredient);

}
