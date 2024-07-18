package com.example.examplemod.api.recipe;

import com.example.examplemod.api.ingredient.ModIngredients;
import com.example.examplemod.api.result.ModResults;
import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.api.ritual.util.ModRituals;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
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

        registerRecipe(RITUAL_RECIPES, RitualRecipeFactory.EXTRACT_LIVE);
        registerRecipe(RITUAL_RECIPES, RitualRecipeFactory.CHANGE_TIME_TO_DAY);

        registerRecipe(KETTLE_RECIPES,KettleRecipeFactory.DIAMOND_RECIPE);
        registerRecipe(KETTLE_RECIPES,KettleRecipeFactory.FLORA_POTION_RECIPE);
    }
    public static @NotNull Optional<ModRecipe<?>> getRecipeBySerializedIngredients(List<ModRecipe<?>> RECIPE_LIST, String recipeSerialized){
        return RECIPE_LIST.stream().filter(modRecipe -> modRecipe.serializedRecipe().equalsIgnoreCase(recipeSerialized)).findFirst();
    }


    private static class RitualRecipeFactory {
        public static ModRecipe<String> EXTRACT_LIVE = new ModRecipe<>(ResultTypes.RITUAL, Lazy.of(() -> ModRituals.EXTRACT_LIVE), ModIngredients.BLAZE_ROD,ModIngredients.GLOWSTONE_DUST);
        public static ModRecipe<String> CHANGE_TIME_TO_DAY = new ModRecipe<>(ResultTypes.RITUAL, Lazy.of(() -> ModRituals.CHANGE_TIME_TO_DAY), ModIngredients.REDSTONE_DUST, ModIngredients.GREEN_DYE, ModIngredients.BLAZE_ROD);
    }


    private static class KettleRecipeFactory {
        public static ModRecipe<ItemStack> DIAMOND_RECIPE = new ModRecipe<>(ResultTypes.ITEM, ModResults.DIAMONDS_5, ModIngredients.BLAZE_ROD, ModIngredients.GLOWSTONE_DUST);
        public static ModRecipe<ItemStack> FLORA_POTION_RECIPE = new ModRecipe<>(ResultTypes.POTION, ModResults.FLORA_POTION_LEVEL3, ModIngredients.GLOWSTONE_DUST, ModIngredients.GLOWSTONE_DUST, ModIngredients.BLAZE_ROD);
    }
}

