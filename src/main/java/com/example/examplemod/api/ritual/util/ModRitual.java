package com.example.examplemod.api.ritual.util;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ModRitual {
    protected Level level;
    protected BlockState blockState;
    protected BlockPos blockPos;
    protected int ritualProgress;
    @Getter
    protected boolean isFinished;
    public ModRitual(Level level, BlockPos blockPos, BlockState blockState, int ritualProgress){
        this.level = level;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.ritualProgress = ritualProgress;
    }
    public abstract int tick();

    public abstract void finishRitual();
}
