package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.capabilities.PlayerSoulEnergy;
import com.example.examplemod.capabilities.PlayerSoulEnergyProvider;
import com.example.examplemod.datagen.ModLootTableProvider;
import com.example.examplemod.particle.ParticleFactory;
import com.example.examplemod.particle.custom.CustomBubbleProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventHandler {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        LogUtils.getLogger().info("Registering Curious items!");
    }
    @SubscribeEvent
    public static void registerParticleProvider (RegisterParticleProvidersEvent event){
        Minecraft.getInstance().particleEngine.register(ParticleFactory.CustomBubbleParticle.get(),
                    CustomBubbleProvider::new
                );

        LogUtils.getLogger().info("RegisterParticleProvider has been registered!!");
    }




}
