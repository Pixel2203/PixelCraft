package com.example.examplemod.api.kettle;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

@RequiredArgsConstructor
public abstract class BlockEntityLogic<T extends BlockEntity> {
    protected final T blockEntity;

    protected ServerLevel getServerLevel() {
        return (ServerLevel)this.blockEntity.getLevel();
    }

}
