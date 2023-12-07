package com.example.examplemod.blockentity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import com.example.examplemod.blockentity.entities.KettleBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITTES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<BlockEntityType<KettleBlockEntity>> KETTLE_BLOCK_ENTITY = BLOCK_ENTITTES.register("kettle_block_entity", () -> Factory.KettleBlockEntity);
    public static final RegistryObject<BlockEntityType<GoldenChalkBlockEntity>> GOLDEN_CHALK_BLOCK_ENTITY = BLOCK_ENTITTES.register("golden_chalk_block_entity", () -> Factory.GoldenChalkBlockEntity);

    public static void registerBlockEntityTypes(IEventBus eventBus){
        BLOCK_ENTITTES.register(eventBus);
    }


    private static class Factory {
        public static BlockEntityType<KettleBlockEntity> KettleBlockEntity =
                BlockEntityType.Builder.of(KettleBlockEntity::new, BlockRegistry.CauldronCustomBlock.get()).build(null);
        public static BlockEntityType<GoldenChalkBlockEntity> GoldenChalkBlockEntity =
                BlockEntityType.Builder.of(GoldenChalkBlockEntity::new, BlockRegistry.GoldenChalkBlock.get()).build(null);
    }
}
