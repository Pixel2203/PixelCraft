package com.example.examplemod.registry.api;

import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import com.example.examplemod.API.result.ResultTypes;
import com.example.examplemod.API.ritual.ModRituals;
import com.example.examplemod.API.ritual.RitualAPI;

public class RitualRecipeRegistry {
    private static ModRecipe EXTRACT_LIVE = new ModRecipe(ResultTypes.RITUAL, ModRituals.EXTRACT_LIVE, ModIngredients.BLAZE_ROD.ingredient,ModIngredients.GLOWSTONE_DUST.ingredient);
    public static void register(){
        RitualAPI.registerRecipe(EXTRACT_LIVE);
    }
}
