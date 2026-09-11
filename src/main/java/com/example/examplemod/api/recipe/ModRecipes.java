package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.api.circleMagic.rituals.ModRituals;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.Vial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class ModRecipes {

    public static void register() {

        simpleKettleRecipe(RecipeOrigin.KETTLE,
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.BLAZE_ROD));

        simpleKettleRecipe(RecipeOrigin.KETTLE,
                new ItemStack(ItemRegistry.GOLDEN_CHALK.get()),
                Vial.createVialWithType(VialType.SOUL_DEW),
                new ItemStack(ItemRegistry.WHITE_CHALK.get()));


        simpleBowlRecipe(new ItemStack(ItemRegistry.GLIMMER_LEAF.get()), VialType.SHIMMER_ESSENCE);

        ModRecipeRegistry.registerCustom(
                RecipeOrigin.KETTLE,
                ResultTypes.CUSTOM,
                CustomResultBuilders.SOUL_LIGHT_BUILDER,
                Vial.createVialWithType(VialType.SHIMMER_ESSENCE),
                new ItemStack(ItemRegistry.SOUL_FRAGMENT.get()),
                new ItemStack(Items.AMETHYST_SHARD));

        simpleRitual(ModRituals.EXTRACT_LIVE, Vial.createVialWithType(VialType.SHIMMER_ESSENCE));
        simpleRitual(ModRituals.UNDEAD_CLEANSE, new ItemStack(Items.GLOWSTONE_DUST));

    }

    private static void simpleKettleRecipe(RecipeOrigin origin, ItemStack result, ItemStack ... ingredients) {
        ModRecipe recipe = new KettleRecipe()
        ModRecipeRegistry.register( result, ingredients);
    }

    private static void simpleBowlRecipe(ItemStack herbIngredient, VialType result) {
        ModRecipe recipe = new BowlRecipe(in)
        ModRecipeRegistry.register(RecipeOrigin.BOWL, ResultTypes.VIAL, () -> result, herbIngredient);
    }

    private static void simpleRitual(ModRituals ritual, ItemStack ... ingredients) {
        RitualRecipe recipe = new RitualRecipe(List.of(ingredients), ritual);
        ModRecipeRegistry.register(recipe);
    }


}
