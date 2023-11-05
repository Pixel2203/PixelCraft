package com.example.examplemod.API.ritual.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class Ritual {
    protected Level level;
    protected BlockState blockState;
    protected BlockPos blockPos;
    protected int ritualProgress;
    protected boolean isFinished;
    public Ritual(Level level, BlockPos blockPos, BlockState blockState, int ritualProgress){
        this.level = level;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.ritualProgress = ritualProgress;
    }
    public abstract int tick();
    public boolean isFinished(){
        return this.isFinished;
    }
    public abstract void finishRitual();

}
