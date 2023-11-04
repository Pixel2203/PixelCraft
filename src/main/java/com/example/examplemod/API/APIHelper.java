package com.example.examplemod.API;

import com.example.examplemod.API.ingredient.ModIngredient;
import net.minecraft.util.StringUtil;

public class APIHelper {
    public static String getNextRecipeString(String currentRecipe, ModIngredient ingredient){
        if(StringUtil.isNullOrEmpty(currentRecipe)){
            currentRecipe = ingredient.id();
        }else {
            currentRecipe +=  "," +ingredient.id();
        }
        return currentRecipe;
    }
}
