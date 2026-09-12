package com.example.examplemod.api.recipe;

import com.example.examplemod.api.distilleryBowl.ModFluids;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BowlRecipeRegistry {
    private static final List<BowlRecipe> RECIPES = new ArrayList<>();

    public static void register() {
        register(BowlRecipe.builder()
                        .baseFluid(ModFluids.WATER)
                        .in(new ItemStack(ItemRegistry.GLIMMER_LEAF.get()))
                        .out(ModFluids.SHIMMER_ESSENCE)
                        .build());
    }


    public static void register(BowlRecipe recipe) {
        RECIPES.add(recipe);
    }

    public static List<BowlRecipe> getRecipes() {
        return new ArrayList<>(RECIPES);
    }
}
