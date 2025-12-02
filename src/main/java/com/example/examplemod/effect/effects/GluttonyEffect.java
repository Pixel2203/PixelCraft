package com.example.examplemod.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class GluttonyEffect extends MobEffect {
    public GluttonyEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int p_19468_) {
        if(living.level().isClientSide) return;
        if(living.hasEffect(MobEffects.HUNGER)) {
            living.removeEffect(MobEffects.HUNGER);
        }
    }
}
