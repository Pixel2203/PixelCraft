package com.example.examplemod.API.kettle;

import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import com.example.examplemod.API.result.ModResults;
import com.example.examplemod.API.result.ResultTypes;

public class KettleRecipes {

    public static ModRecipe TestKettleRecipe = new ModRecipe(ResultTypes.ITEM, ModResults.DIAMONDS_5.stack, ModIngredients.BLAZE_ROD.ingredient, ModIngredients.GLOWSTONE_DUST.ingredient);
    public static ModRecipe TestKettleRecipe2 = new ModRecipe(ResultTypes.POTION, ModResults.DIARRHEA_POTION.stack, ModIngredients.GLOWSTONE_DUST.ingredient, ModIngredients.BLAZE_ROD.ingredient);
    public static ModRecipe FloraPotionRecipe = new ModRecipe(ResultTypes.POTION, ModResults.FLORA_POTION_LEVEL3.stack, ModIngredients.GLOWSTONE_DUST.ingredient, ModIngredients.GLOWSTONE_DUST.ingredient, ModIngredients.BLAZE_ROD.ingredient);
}
