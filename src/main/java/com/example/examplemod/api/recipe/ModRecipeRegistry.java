package com.example.examplemod.api.recipe;

import com.example.examplemod.api.circleMagic.rituals.ModRituals;
import com.example.examplemod.api.recipe.kettle.ChargedSoulLightCrystalKettleRecipe;
import com.example.examplemod.api.recipe.kettle.SimpleItemKettleRecipe;
import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.api.vial.VialType;
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
                Vial.createVialWithType(VialType.SOUL_DEW),
                new ItemStack(ItemRegistry.WHITE_CHALK.get()));

        ModRecipeRegistry.register(new ChargedSoulLightCrystalKettleRecipe());
        simpleRitual(ModRituals.EXTRACT_LIVE, Vial.createVialWithType(VialType.SHIMMER_ESSENCE));
        simpleRitual(ModRituals.UNDEAD_CLEANSE, new ItemStack(Items.GLOWSTONE_DUST));
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
        RitualRecipe recipe = new RitualRecipe(List.of(ingredients), ritual);
        ModRecipeRegistry.register(recipe);
    }
}
