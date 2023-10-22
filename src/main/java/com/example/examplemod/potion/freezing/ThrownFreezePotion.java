package com.example.examplemod.potion.freezing;

import com.example.examplemod.potion.CustomSplashPotion;
import com.example.examplemod.potion.CustomThrownPotion;
import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class ThrownFreezePotion extends CustomThrownPotion {


    public ThrownFreezePotion(Level p_37535_, LivingEntity p_37536_) {
        super(p_37535_, p_37536_);
    }

    @Override
    protected List<MobEffectInstance> getPotionEffects() {
        return List.of(new MobEffectInstance(MobEffectRegistry.FREEZE.get(),getEffectDuration(),getEffectAmplifier()));
    }
}

