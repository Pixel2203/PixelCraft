package com.example.examplemod.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public record BlockInfo(BlockPos blockPos, BlockState blockState) {
}
