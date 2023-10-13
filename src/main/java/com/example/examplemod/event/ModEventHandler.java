package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.particle.ParticleFactory;
import com.example.examplemod.particle.custom.CustomBubbleProvider;
import com.mojang.logging.LogUtils;
import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventHandler {
    public static final Lazy<KeyMapping> EXAMPLE_MAPPING = Lazy.of();
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        LogUtils.getLogger().info("COMMON SETUP GEFEUERT!");
    }
    @SubscribeEvent
    public static void registerParticleProvider (RegisterParticleProvidersEvent event){
        Minecraft.getInstance().particleEngine.register(ParticleFactory.CustomBubbleParticle.get(),
                    CustomBubbleProvider::new
                );

        LogUtils.getLogger().warn("RegisterParticleProvider has been registered!!");
    }
}
