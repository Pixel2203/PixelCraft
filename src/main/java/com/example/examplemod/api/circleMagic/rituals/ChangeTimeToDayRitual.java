package com.example.examplemod.api.circleMagic.rituals;

import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class ChangeTimeToDayRitual extends ModRitual {
    public ChangeTimeToDayRitual(int ritualProgress) {
        super(ritualProgress);
    }


    @Override
    public int tick(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance) {
        if(level.isClientSide()){
            return 0;
        }
        level.setDayTime(12);
        isFinished = true;
        return this.ritualProgress;
    }

    @Override
    public void onFinish(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance) {

    }

    @Override
    public ModRituals getType() {
        return ModRituals.CHANGE_TIME_TO_DAY;
    }
}
