package com.example.examplemod.api.circleMagic.rituals;

import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public abstract class Ritual {
    protected int ritualProgress;
    @Getter
    private boolean isFinished;
    public Ritual(int ritualProgress){
        this.ritualProgress = ritualProgress;
    }
    public abstract int tick(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance);

    public abstract void onFinish(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance);

    public abstract ModRituals getType();

    protected void finish() { this.isFinished = true; }
}
