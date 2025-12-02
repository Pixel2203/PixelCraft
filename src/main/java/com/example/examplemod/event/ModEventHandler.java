package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.runes.RuneType;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.particle.ParticleFactory;
import com.example.examplemod.particle.custom.CustomBubbleProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

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

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // z.B. in deiner ModClientEvents Klasse, bei ClientSetup
        ItemProperties.register(ItemRegistry.SOUL_CRYSTAL.get(), new ResourceLocation(CustomNBTTags.ENERGY_CHARGE),
                (stack, level, entity, seed) -> {
                    if(!stack.hasTag()) return 0f;
                    CompoundTag tag = stack.getTag();
                    if(tag.contains(CustomNBTTags.ENERGY_CHARGE)) {
                        double charge = tag.getDouble(CustomNBTTags.ENERGY_CHARGE);
                        if(charge >= 0.7) return 4f;
                        if(charge >= 0.4) return 3f;
                        if(charge >= 0.2) return 2f;
                        if(charge >= 0.1) return 1f;
                    }
                    return 0f;
                }
        );

        ItemProperties.register(ItemRegistry.VIAL.get(), new ResourceLocation(ExampleMod.MODID + ":type"),
                (stack, p_174677_, p_174678_, p_174679_) -> {
                    CompoundTag tag = stack.getOrCreateTag();
                    if(!tag.contains(ExampleMod.MODID)) return 0f;
                    if(!tag.getCompound(ExampleMod.MODID).contains("type")) return 0f;
                    return 1;
                }
        );

        ItemProperties.register(ItemRegistry.WOODEN_RUNE.get(), new ResourceLocation(ExampleMod.MODID + ":rune_type"),
                (stack, p_174677_, p_174678_, p_174679_) -> {
                    CompoundTag tag = stack.getOrCreateTag();
                    if(!tag.contains(ExampleMod.MODID)) return -1f;
                    if(!tag.getCompound(ExampleMod.MODID).contains("runeType")) return -1f;
                    String runeType = tag.getCompound(ExampleMod.MODID).getString("runeType");
                    return RuneType.valueOf(runeType).ordinal();
                }
        );

    }






}
