package com.example.examplemod.potion.custom.freezing;

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
    protected List<MobEffectInstance> getPotionEffects(int duration, int amplifier) {
        return List.of(new MobEffectInstance(MobEffectRegistry.FREEZE.get(),duration,amplifier));
    }
}

