package com.example.examplemod.API.recipe;

import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.result.ResultTypes;
import com.example.examplemod.API.ritual.util.ModRituals;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RecipeAPI {
    public static final List<ModRecipe<?>> RITUAL_RECIPES = new ArrayList<>();
    public static final List<ModRecipe<?>> KETTLE_RECIPES = new ArrayList<>();

    public static void registerRecipe(List<ModRecipe<?>> RECIPE_LIST, ModRecipe<?> recipe) {
        if(Objects.isNull(recipe)){
            return;
        }
        RECIPE_LIST.add(recipe);
    }
    public static void register(){
        registerRecipe(RITUAL_RECIPES,RitualFactory.EXTRACT_LIVE);
        registerRecipe(RITUAL_RECIPES,RitualFactory.CHANGE_TIME_TO_DAY);
    }
    public static @NotNull Optional<ModRecipe<?>> getRecipeBySerializedIngredients(String recipeSerialized){
        return RITUAL_RECIPES.stream().filter(modRecipe -> modRecipe.serializedRecipe().equalsIgnoreCase(recipeSerialized)).findFirst();
    }
    private static class RitualFactory {
        public static ModRecipe<String> EXTRACT_LIVE = new ModRecipe<>(ResultTypes.RITUAL, ModRituals.EXTRACT_LIVE, ModIngredients.BLAZE_ROD,ModIngredients.GLOWSTONE_DUST);
        public static ModRecipe<String> CHANGE_TIME_TO_DAY = new ModRecipe<>(ResultTypes.RITUAL, ModRituals.CHANGE_TIME_TO_DAY, ModIngredients.REDSTONE_DUST, ModIngredients.GREEN_DYE, ModIngredients.BLAZE_ROD);

    }
}

