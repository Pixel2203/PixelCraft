package com.example.examplemod.api.kettle;

import com.example.examplemod.blockentity.entities.KettleBlockEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;

@RequiredArgsConstructor
public abstract class KettleLogic {
    protected final KettleBlockEntity kettleBlockEntity;

    protected ServerLevel getServerLevel() {
        return (ServerLevel)this.kettleBlockEntity.getLevel();
    }

}
