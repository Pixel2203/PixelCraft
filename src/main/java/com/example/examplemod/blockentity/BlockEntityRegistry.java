package com.example.examplemod.blockentity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.blockentity.custom.KettleBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITTES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExampleMod.MODID);
    public static final RegistryObject<BlockEntityType<KettleBlockEntity>> KETTLE_BLOCK_ENTITY =
            BLOCK_ENTITTES.register("kettle_block_entity", () -> BlockEntityFactory.KettleBlockEntity);

    public static void registerBlockEntityTypes(IEventBus eventBus){
        BLOCK_ENTITTES.register(eventBus);
    }
}
