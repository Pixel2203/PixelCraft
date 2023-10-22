package com.example.examplemod.potion.freezing;

import com.example.examplemod.potion.CustomSplashPotion;
import com.example.examplemod.potion.CustomThrownPotion;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class ThrownFreezePotion extends CustomThrownPotion {


    public ThrownFreezePotion(Level p_37535_, LivingEntity p_37536_, CustomSplashPotion potion) {
        super(p_37535_, p_37536_, potion);
    }

    @Override
    protected List<MobEffectInstance> getPotionEffects() {
        return null;
    }
}

