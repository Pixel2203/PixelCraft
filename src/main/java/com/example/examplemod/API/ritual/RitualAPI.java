package com.example.examplemod.API.ritual;

import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RitualAPI {
    private static final List<ModRecipe> RITUAL_RECIPES = new ArrayList<>();


    public static ModRecipe getRecipeBySerializedIngredients(List<ModIngredient> ingredients){
        ModRecipe foundRecipe = null;
        List<ModRecipe> recipesWithRightLength = RITUAL_RECIPES.parallelStream().filter(modRecipe -> modRecipe.ingredients().length == ingredients.size()).toList();
        for(ModRecipe recipe : recipesWithRightLength){
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

    public static void registerRecipe(ModRecipe recipe) {
        if(Objects.isNull(recipe)){
            return;
        }
        RITUAL_RECIPES.add(recipe);
    }


    // Rituals
    public static void performExtractLiveRitual(Level level){
        System.out.println("starte Ritual...");
    }

    public static void performRitual(String ritualID, Level level) {
        switch (ritualID){
            case ModRituals.EXTRACT_LIVE -> RitualAPI.performExtractLiveRitual(level);
        }
    }
}
