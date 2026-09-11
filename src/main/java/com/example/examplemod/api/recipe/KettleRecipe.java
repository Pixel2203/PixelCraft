package com.example.examplemod.api.recipe;

import com.example.examplemod.api.circleMagic.rituals.ModRitual;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class RitualRecipe{
    private final List<ItemStack> ingredients;
    private final ModRitual resultingRitual;
}
