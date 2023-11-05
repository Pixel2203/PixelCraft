package com.example.examplemod.registry.api;

import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.recipe.ModRecipe;
import com.example.examplemod.API.result.ResultTypes;
import com.example.examplemod.API.ritual.rituals.ModRituals;
import com.example.examplemod.API.RitualAPI;

public class RitualRecipeRegistry {
    private static ModRecipe EXTRACT_LIVE = new ModRecipe(ResultTypes.RITUAL, ModRituals.EXTRACT_LIVE, ModIngredients.BLAZE_ROD,ModIngredients.GLOWSTONE_DUST);
    public static void register(){
        RitualAPI.registerRecipe(EXTRACT_LIVE);
    }
}
