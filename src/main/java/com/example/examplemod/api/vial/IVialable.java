package com.example.examplemod.api.vial;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface IVialable {

    public VialResult tap(ServerLevel level, BlockState blockState, BlockPos blockPos);
}
