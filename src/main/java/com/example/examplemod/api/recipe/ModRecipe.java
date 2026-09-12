package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public abstract class ModRecipe {
    private final LinkedList<ItemStack> ingredients;

    public abstract RecipeOrigin getOrigin();
}
