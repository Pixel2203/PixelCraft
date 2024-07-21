package com.example.examplemod.event.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.renderers.ScrollEntityRenderer;
import com.example.examplemod.entity.renderers.SoulEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Renderers {


    @SubscribeEvent
    public static void onRegisterEntityRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegistry.SCROLL_ENTITY.get(), ScrollEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SOUL_ENTITY.get(), SoulEntityRenderer::new);
    }


}
