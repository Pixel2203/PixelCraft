package com.example.examplemod.potion.hungerregeneration;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.potion.CustomSplashPotion;
import com.example.examplemod.potion.CustomThrownPotion;
import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class ThrownHungerRegenerationPotion extends CustomThrownPotion {
    public ThrownHungerRegenerationPotion(Level p_37535_, LivingEntity p_37536_) {
        super(p_37535_, p_37536_);
    }

    @Override
    protected List<MobEffectInstance> getPotionEffects() {
        return List.of(new MobEffectInstance(MobEffectRegistry.HUNGER_REGENERATION.get(), getEffectDuration(), getEffectAmplifier()));
    }
}
