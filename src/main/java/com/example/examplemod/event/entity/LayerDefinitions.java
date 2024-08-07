package com.example.examplemod.event.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.models.ScrollEntityModel;
import com.example.examplemod.entity.models.SoulEntityModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LayerDefinitions {


    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ScrollEntityModel.LAYER_LOCATION, ScrollEntityModel::createBodyLayer);
        event.registerLayerDefinition(SoulEntityModel.LAYER_LOCATION, SoulEntityModel::createBodyLayer);
    }


}
