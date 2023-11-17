package com.example.examplemod.API.ritual;

import com.example.examplemod.API.recipe.ModRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ModRitual {
    protected Level level;
    protected BlockState blockState;
    protected BlockPos blockPos;
    protected int ritualProgress;
    protected boolean isFinished;
    public ModRitual(Level level, BlockPos blockPos, BlockState blockState, int ritualProgress){
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
