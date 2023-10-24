package com.example.examplemod.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BlizzardPoisoningEffect extends MobEffect {
    private byte effectTicker = 0;
    private final byte effectInterval = 10;
    public BlizzardPoisoningEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int i) {
        if(!entity.level().isClientSide()){
            effectTicker++;
            if(effectTicker <= effectInterval){
                return;
            }
            effectTicker = 0;
            entity.hurt(entity.damageSources().magic(),1.0F);
        }
        super.applyEffectTick(entity, i);
    }
}
