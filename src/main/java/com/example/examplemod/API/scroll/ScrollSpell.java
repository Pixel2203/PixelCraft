package com.example.examplemod.API.scroll;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public abstract class ScrollSpell {
    private int tickInterval;
    private int ticks;
    public ScrollSpell(int tickInterval){
        this.tickInterval = tickInterval;
        this.ticks = 0;
    }
    public void tick(Level level, BlockPos entityPos){
        if(level.isClientSide){
            return;
        }
        ticks++;
        if(ticks < tickInterval){
            return;
        }
        ticks = 0;
        scheduledTick((ServerLevel)level,entityPos);
    }
    public abstract void scheduledTick(ServerLevel level, BlockPos blockPos);
}
