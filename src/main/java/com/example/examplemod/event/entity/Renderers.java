package com.example.examplemod.event.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.renderers.ExampleRenderer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Renderers {


    @SubscribeEvent
    public static void onRegisterEntityRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegistry.EXAMPLE.get(), ExampleRenderer::new);
    }


}
