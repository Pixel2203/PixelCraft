package com.example.examplemod.registry.api;

import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.recipe.ModRecipe;
import com.example.examplemod.API.result.ResultTypes;
import com.example.examplemod.API.ritual.rituals.ModRituals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RitualRecipeRegistry {
    private static final List<ModRecipe> RITUAL_RECIPES = new ArrayList<>();
    private static ModRecipe EXTRACT_LIVE = new ModRecipe(ResultTypes.RITUAL, ModRituals.EXTRACT_LIVE, ModIngredients.BLAZE_ROD,ModIngredients.GLOWSTONE_DUST);
    public static void register(){
        registerRecipe(EXTRACT_LIVE);
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
}
