package com.example.examplemod.API.brewing.kettle.recipe;

import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import net.minecraft.util.StringUtil;

public class KettleRecipeFactory {
    public static String getNextRecipeString(String currentRecipe, KettleIngredient ingredient){
        if(StringUtil.isNullOrEmpty(currentRecipe)){
            currentRecipe = ingredient.id();
        }else {
            currentRecipe +=  "," +ingredient.id();
        }
        return currentRecipe;
    }
}
