package com.example.examplemod.api.recipe;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Function;

public interface CustomResultBuilders {
    Function<List<ItemStack>, ItemStack> SOUL_LIGHT_BUILDER = (itemStacks -> {

    });
}
