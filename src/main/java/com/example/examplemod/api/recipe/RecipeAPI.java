package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ModResults;
import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.api.ritual.util.ModRituals;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Lazy;
import org.antlr.v4.runtime.misc.MultiMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Stream;

public class RecipeAPI {
    private static final List<ModRecipe<?>> RITUAL_RECIPES = new ArrayList<>();
    private static final List<ModRecipe<?>> KETTLE_RECIPES = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(RecipeAPI.class);

    public static void register(){

        RecipeFactory.RECIPES.forEach((s, modRecipe) -> {
            var origin = RecipeOrigins.valueOf(s);
            switch (origin) {
                case KETTLE -> KETTLE_RECIPES.add(modRecipe);
                case CHALK -> RITUAL_RECIPES.add(modRecipe);
                default -> log.error("Unknown recipe origin {}", origin);
            }
        });

    }
    public static @NotNull Optional<ModRecipe<?>> getRecipeBySerializedIngredients(RecipeOrigins origin, List<Item> ingredients){
        Stream<ModRecipe<?>> stream;
        switch (origin) {
            case KETTLE -> stream = KETTLE_RECIPES.stream();
            case CHALK -> stream = RITUAL_RECIPES.stream();
            default -> {
                log.error("Unknown recipe origin when getting recipe {}", origin);
                return Optional.empty();
            }
        }
        return  stream
                .filter(modRecipe -> modRecipe.ingredients().size() == ingredients.size())
                .filter(modRecipe -> new HashSet<>(ingredients).containsAll(modRecipe.ingredients()))
                .findFirst();
    }


    private static class RecipeFactory {
        static HashMultimap<String, ModRecipe<?>> RECIPES = HashMultimap.create();
        public static ModRecipe<?> EXTRACT_LIVE = registerRecipe(RecipeOrigins.CHALK,ResultTypes.RITUAL, Lazy.of(() -> ModRituals.EXTRACT_LIVE), Items.BLAZE_ROD,Items.GLOWSTONE_DUST);
        public static ModRecipe<?> CHANGE_TIME_TO_DAY = registerRecipe(RecipeOrigins.CHALK,ResultTypes.RITUAL, Lazy.of(() -> ModRituals.CHANGE_TIME_TO_DAY), Items.REDSTONE, Items.GREEN_DYE, Items.BLAZE_ROD);
        public static ModRecipe<?> DIAMOND_RECIPE = registerRecipe(RecipeOrigins.KETTLE,ResultTypes.ITEM, ModResults.DIAMONDS_5, Items.BLAZE_ROD, Items.GLOWSTONE_DUST);
        public static ModRecipe<?> FLORA_POTION_RECIPE = registerRecipe(RecipeOrigins.KETTLE,ResultTypes.POTION, ModResults.FLORA_POTION_LEVEL3, Items.GLOWSTONE_DUST, Items.GLOWSTONE_DUST, Items.BLAZE_ROD);

        private static <T> ModRecipe<T> registerRecipe(RecipeOrigins origin,ResultTypes type, Lazy<T> result, Item... ingredients){
            var recipe = new ModRecipe<T>(type, result, ImmutableList.copyOf(ingredients));
            RECIPES.put(origin.name(), recipe);
            return recipe;
        }


    }
    public enum RecipeOrigins {
        KETTLE(),
        CHALK()
    }

}

