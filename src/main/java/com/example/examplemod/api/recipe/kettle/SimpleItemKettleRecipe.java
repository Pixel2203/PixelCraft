package com.example.examplemod.api.recipe.kettle;

import com.example.examplemod.api.result.ResultTypes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class SimpleItemKettleRecipe extends KettleRecipe {
    private final ItemStack result;

    public SimpleItemKettleRecipe(LinkedList<ItemStack> ingredients, ItemStack result) {
        super(ingredients, ResultTypes.ITEM);
        this.result = result;
    }


    @Override
    public @Nullable ItemStack getResult(List<ItemStack> ingredients) {
        return this.result;
    }
}
