package com.example.examplemod.API.ritual;

import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
}
