package com.example.examplemod.API.ritual;

import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.recipe.ModRecipe;
import com.example.examplemod.API.result.ResultTypes;
import com.example.examplemod.API.ritual.util.ModRituals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RitualAPI {
    private static final List<ModRecipe> RITUAL_RECIPES = new ArrayList<>();
    public static void register(){
        registerRecipe(Factory.EXTRACT_LIVE);
        registerRecipe(Factory.CHANGE_TIME_TO_DAY);
    }
    private static void registerRecipe(ModRecipe recipe) {
        if(Objects.isNull(recipe)){
            return;
        }
        RITUAL_RECIPES.add(recipe);
    }
    public static ModRecipe<?> getRitualRecipeBySerializedIngredients(List<ModIngredient> ingredients){
        ModRecipe<?> foundRecipe = null;
        List<ModRecipe> recipesWithRightLength = RITUAL_RECIPES.parallelStream().filter(modRecipe -> modRecipe.ingredients().length == ingredients.size()).toList();
        for(ModRecipe<?> recipe : recipesWithRightLength){
            foundRecipe = recipe;
            for(ModIngredient ingredient : recipe.ingredients()){
                if(!ingredients.contains(ingredient)){
                    foundRecipe = null;
                    break;
                }
            }
            if(Objects.nonNull(foundRecipe)){
                break;
            }
        }
        return foundRecipe;
    }
    private static class Factory {
        public static ModRecipe<String> EXTRACT_LIVE = new ModRecipe(ResultTypes.RITUAL, ModRituals.EXTRACT_LIVE, ModIngredients.BLAZE_ROD,ModIngredients.GLOWSTONE_DUST);
        public static ModRecipe<String> CHANGE_TIME_TO_DAY = new ModRecipe(ResultTypes.RITUAL, ModRituals.CHANGE_TIME_TO_DAY, ModIngredients.REDSTONE_DUST, ModIngredients.GREEN_DYE, ModIngredients.BLAZE_ROD);

    }
}
