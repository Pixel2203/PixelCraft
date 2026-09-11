package com.example.examplemod.api.recipe;

import com.example.examplemod.api.circleMagic.rituals.Ritual;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class KettleRecipe {
    private final List<ItemStack> ingredients;
    private final Ritual resultingRitual;
    private Function<List<ItemStack>, ItemStack> crafterFunction;

    @Nullable
    public ItemStack getCrafterResult(List<ItemStack> ing) {
        return crafterFunction.apply(ing);
    }
}
