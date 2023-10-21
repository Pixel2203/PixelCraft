package com.example.examplemod.potion.freezing;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;

public class test extends Potion {
    @Override
    public List<MobEffectInstance> getEffects() {
        return List.of(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
    }
}
