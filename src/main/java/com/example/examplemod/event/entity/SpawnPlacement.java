package com.example.examplemod.event.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.ExampleEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpawnPlacement {


    @SubscribeEvent
    public static void onRegisterSpawnPlacement(SpawnPlacementRegisterEvent event){
        event.register(EntityRegistry.EXAMPLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, ExampleEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
    }


}
