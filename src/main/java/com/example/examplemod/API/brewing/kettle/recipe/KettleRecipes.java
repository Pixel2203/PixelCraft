package com.example.examplemod.API.brewing.kettle.recipe;

import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredients;
import com.example.examplemod.API.brewing.kettle.result.KettleResults;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import com.example.examplemod.API.brewing.kettle.result.ResultTypes;

public class KettleRecipes {

    public static KettleRecipe TestKettleRecipe = new KettleRecipe(ResultTypes.ITEM,KettleResults.DIAMONDS_5.stack, KettleIngredients.BLAZE_ROD.ingredient,KettleIngredients.GLOWSTONE_DUST.ingredient);
    public static KettleRecipe TestKettleRecipe2 = new KettleRecipe(ResultTypes.POTION,KettleResults.DIARRHEA_POTION.stack, KettleIngredients.GLOWSTONE_DUST.ingredient,KettleIngredients.BLAZE_ROD.ingredient);
    public static KettleRecipe FloraPotionRecipe = new KettleRecipe(ResultTypes.POTION,KettleResults.FLORA_POTION_LEVEL3.stack,KettleIngredients.GLOWSTONE_DUST.ingredient,KettleIngredients.GLOWSTONE_DUST.ingredient,KettleIngredients.BLAZE_ROD.ingredient);
}
