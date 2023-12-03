package com.example.examplemod.event.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.models.ExampleEntityModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LayerDefinitions {


    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ExampleEntityModel.LAYER_LOCATION,ExampleEntityModel::createBodyLayer);
    }


}
