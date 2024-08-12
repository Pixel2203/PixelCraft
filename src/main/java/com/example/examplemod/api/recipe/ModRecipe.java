package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

public record ModRecipe<T>(ResultTypes resultType,  Lazy<T> result, ImmutableList<Item> ingredients){}
