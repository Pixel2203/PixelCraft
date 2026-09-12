package com.example.examplemod.api.recipe;

import com.example.examplemod.api.circleMagic.ModRituals;
import com.example.examplemod.api.distilleryBowl.ModFluids;
import com.example.examplemod.api.recipe.kettle.ChargedSoulLightCrystalKettleRecipe;
import com.example.examplemod.api.recipe.kettle.SimpleItemKettleRecipe;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.Vial;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ModRecipeRegistry {

    private static final List<ModRecipe> RECIPES = new ArrayList<>();

    public static void register(ModRecipe recipe) {
        log.info("ModRecipeRegistry | register | Registering recipe");
        RECIPES.add(recipe);

    }

    public static void register() {
        simpleKettleRecipe(
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.BLAZE_ROD));

        simpleKettleRecipe(
                new ItemStack(ItemRegistry.GOLDEN_CHALK.get()),
                Vial.createVialWithType(ModFluids.SOUL_DEW),
                new ItemStack(ItemRegistry.WHITE_CHALK.get()));

        ModRecipeRegistry.register(new ChargedSoulLightCrystalKettleRecipe());
        simpleRitual(ModRituals.EXTRACT_LIVE, Vial.createVialWithType(ModFluids.SHIMMER_ESSENCE));
        simpleRitual(ModRituals.UNDEAD_CLEANSE, new ItemStack(Items.GLOWSTONE_DUST));
        simpleRitual(ModRituals.TEST_RITUAL_TO_GET_HERB, new ItemStack(Items.GRASS));
    }


    public static List<ModRecipe> getRecipes(RecipeOrigin origin) {
        return RECIPES.stream().filter(recipe -> recipe.getOrigin() == origin).toList();
    }

    private static void simpleKettleRecipe(ItemStack result, ItemStack ... ingredients) {
        LinkedList<ItemStack> stacks = new LinkedList<>(List.of(ingredients));
        ModRecipe recipe = new SimpleItemKettleRecipe(stacks, result);
        ModRecipeRegistry.register( recipe );
    }


    private static void simpleRitual(ModRituals ritual, ItemStack ... ingredients) {
        LinkedList<ItemStack> stacks = new LinkedList<>(List.of(ingredients));
        RitualRecipe recipe = new RitualRecipe(stacks, ritual);
        ModRecipeRegistry.register(recipe);
    }
}
