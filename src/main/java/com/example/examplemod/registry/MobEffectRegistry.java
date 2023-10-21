package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.effect.FreezeEffect;
import com.example.examplemod.effect.HungerRegeneration;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectRegistry {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ExampleMod.MODID);

    private static final RegistryObject<MobEffect> FREEZE = MOB_EFFECTS.register("freeze", () -> new FreezeEffect(MobEffectCategory.HARMFUL,3124867));
    private static final RegistryObject<MobEffect> HUNGER_REGENERATION = MOB_EFFECTS.register("hunger_regeneration", () -> new HungerRegeneration(MobEffectCategory.HARMFUL,3124867));
    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
