package com.example.examplemod.blockentity;

import com.example.examplemod.registry.BlockRegistry;
import com.example.examplemod.blockentity.custom.KettleBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityFactory {
    public static BlockEntityType<KettleBlockEntity> KettleBlockEntity =
            BlockEntityType.Builder.of(KettleBlockEntity::new, BlockRegistry.CauldronCustomBlock.get()).build(null);
}
