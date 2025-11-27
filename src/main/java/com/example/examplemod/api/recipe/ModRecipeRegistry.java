package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModRecipeRegistry {

    private static final Map<RecipeOrigin, List<ModRecipe<?>>> RECIPES = new EnumMap<>(RecipeOrigin.class);

    public static <T> ModRecipe<T> register(
            RecipeOrigin origin,
            ResultTypes type,
            Lazy<T> result,
            ItemStack... ingredients
    ) {

        var recipe = new ModRecipe<>(origin, type, List.of(ingredients),result);

        RECIPES.computeIfAbsent(origin, o -> new ArrayList<>())
                .add(recipe);

        return recipe;
    }

    public static List<ModRecipe<?>> getRecipes(RecipeOrigin origin) {
        return RECIPES.getOrDefault(origin, List.of());
    }
}
