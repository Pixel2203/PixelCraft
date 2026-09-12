package com.example.examplemod.api.vial;

import com.example.examplemod.api.distilleryBowl.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public interface IVialable {
    @Nullable
    ModFluids tap(ServerLevel level, BlockState blockState, BlockPos blockPos);
}
