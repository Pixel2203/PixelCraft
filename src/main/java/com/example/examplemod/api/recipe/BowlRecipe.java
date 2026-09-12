package com.example.examplemod.api.recipe;

import com.example.examplemod.api.distilleryBowl.ModFluids;
import lombok.Builder;
import net.minecraft.world.item.ItemStack;


@Builder
public record BowlRecipe(ModFluids baseFluid, ItemStack in, ModFluids out) {
}
