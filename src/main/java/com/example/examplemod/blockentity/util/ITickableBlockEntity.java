package com.example.examplemod.blockentity.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public interface ITickableBlockEntity {
    void tick();
    static <T extends BlockEntity>BlockEntityTicker<T> getTickerHelper(Level level){
        return level.isClientSide() ? null : (level0,pos,state,blockEntity) -> ((ITickableBlockEntity)blockEntity).tick();
    }
}
