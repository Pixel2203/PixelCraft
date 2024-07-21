package com.example.examplemod.event.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.ScrollEntity;
import com.example.examplemod.entity.entities.SoulEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Attributes {


    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.SCROLL_ENTITY.get(), ScrollEntity.createAttributes().build());
        event.put(EntityRegistry.SOUL_ENTITY.get(), SoulEntity.createAttributes().build());
    }


}
