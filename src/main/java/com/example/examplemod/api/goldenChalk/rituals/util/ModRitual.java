package com.example.examplemod.api.goldenChalk.rituals.util;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ModRitual {
    protected int ritualProgress;
    @Getter
    protected boolean isFinished;
    public ModRitual(int ritualProgress){
        this.ritualProgress = ritualProgress;
    }
    public abstract int tick(ServerLevel level, BlockState blockState, BlockPos blockPos);

    public abstract void onFinish(ServerLevel level, BlockState blockState, BlockPos blockPos);

    public abstract ModRituals getType();
}
