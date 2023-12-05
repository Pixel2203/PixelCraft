package com.example.examplemod.API.scroll;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class ScrollSpell {
    private int tickInterval;
    private int ticks;
    public ScrollSpell(int tickInterval){
        this.tickInterval = tickInterval;
        this.ticks = 0;
    }
    public void tick(Level level, BlockPos entityPos){
        ticks++;
        if(ticks < tickInterval){
            return;
        }
        ticks = 0;
        scheduledTick(level,entityPos);
    }
    public abstract void scheduledTick(Level level, BlockPos blockPos);
}
