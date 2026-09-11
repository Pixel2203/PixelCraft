package com.example.examplemod.api.recipe;

import com.example.examplemod.api.circleMagic.rituals.Ritual;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class BowlRecipe {
    private final List<ItemStack> ingredients;
    private final Ritual resultingRitual;
}
