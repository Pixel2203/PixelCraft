package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.effect.BlizzardPoisoningEffect;
import com.example.examplemod.effect.FreezeEffect;
import com.example.examplemod.effect.HungerRegenerationEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectRegistry {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ExampleMod.MODID);

    public static final RegistryObject<MobEffect> FREEZE = MOB_EFFECTS.register("freeze", () -> new FreezeEffect(MobEffectCategory.HARMFUL,3124867));
    public static final RegistryObject<MobEffect> HUNGER_REGENERATION = MOB_EFFECTS.register("hunger_regeneration", () -> new HungerRegenerationEffect(MobEffectCategory.BENEFICIAL,3124867));
    public static final RegistryObject<MobEffect> BLIZZARD_POISONING = MOB_EFFECTS.register("blizzard_poisoning", () -> new BlizzardPoisoningEffect(MobEffectCategory.HARMFUL,3124867));
    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
