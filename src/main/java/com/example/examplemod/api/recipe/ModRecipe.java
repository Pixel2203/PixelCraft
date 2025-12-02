package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ModRecipe<T> {
    private final RecipeOrigin origin;
    private final ResultTypes resultType;
    private final List<ItemStack> ingredients;
    private final Lazy<T> result;
    private Function<List<ItemStack>, ItemStack> crafterFunction;

    @Nullable
    public ItemStack getCrafterResult(List<ItemStack> ing) {
        return crafterFunction.apply(ing);
    }
}
