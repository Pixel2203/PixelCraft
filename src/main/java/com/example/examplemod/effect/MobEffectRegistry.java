package com.example.examplemod.effect;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.effect.effects.*;
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
    public static final RegistryObject<MobEffect> GUARDIAN_ANGLE = MOB_EFFECTS.register("guardian_angle", () -> new GuardianAngleEffect(MobEffectCategory.BENEFICIAL,3124867));
    public static final RegistryObject<MobEffect> FREEZING_PROTECTION = MOB_EFFECTS.register("freezing_protection", () -> new FreezingProtectionEffect(MobEffectCategory.BENEFICIAL,3124867));
    public static final RegistryObject<MobEffect> GLUTTONY = MOB_EFFECTS.register("gluttony", () -> new GluttonyEffect(MobEffectCategory.BENEFICIAL,11029690));
    public static final RegistryObject<MobEffect> CRYPTLIGHT = MOB_EFFECTS.register("cryptlight", () -> new CryptlightEffect(MobEffectCategory.BENEFICIAL,0x0d4d00));
    public static final RegistryObject<MobEffect> ANIMALFRIEND = MOB_EFFECTS.register("animal_friend", () -> new AnimalFriend(MobEffectCategory.BENEFICIAL,0xEE0b0A));
    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
