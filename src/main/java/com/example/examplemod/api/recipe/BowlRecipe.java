package com.example.examplemod.api.recipe;

import com.example.examplemod.api.distilleryBowl.BowlFluids;
import lombok.Builder;
import net.minecraft.world.item.ItemStack;


@Builder
public record BowlRecipe(BowlFluids baseFluid, ItemStack in, BowlFluids out) {
}
