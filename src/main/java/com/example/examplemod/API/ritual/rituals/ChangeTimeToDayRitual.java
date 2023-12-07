package com.example.examplemod.API.ritual.rituals;

import com.example.examplemod.API.ritual.util.ModRitual;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ChangeTimeToDayRitual extends ModRitual {
    public ChangeTimeToDayRitual(Level level, BlockPos blockPos, BlockState blockState, int ritualProgress) {
        super(level, blockPos, blockState, ritualProgress);
    }

    @Override
    public int tick() {
        if(level.isClientSide()){
            return 0;
        }
        ((ServerLevel)level).setDayTime(12);
        isFinished = true;
        return this.ritualProgress;
    }

    @Override
    public void finishRitual() {

    }

}
