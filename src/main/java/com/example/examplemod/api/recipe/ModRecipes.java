package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.Vial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModRecipes {


    public static void register() {

        simpleItemRecipe(RecipeOrigin.KETTLE,
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.BLAZE_ROD));

        simpleItemRecipe(RecipeOrigin.KETTLE,
                new ItemStack(ItemRegistry.GOLDEN_CHALK.get()),
                Vial.createVialWithType(VialType.SOUL_DEW),
                new ItemStack(ItemRegistry.WHITE_CHALK.get()));
    }

    private static void simpleItemRecipe(RecipeOrigin origin, ItemStack result, ItemStack ... ingredients) {
        ModRecipeRegistry.register(origin, ResultTypes.ITEM, () -> result, ingredients);
    }
}
