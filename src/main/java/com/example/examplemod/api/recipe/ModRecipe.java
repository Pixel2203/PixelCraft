package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ModRecipe<T> {
    private final RecipeOrigin origin;
    private final ResultTypes resultType;
    private final List<ItemStack> ingredients;
    private final Lazy<T> result;
}
