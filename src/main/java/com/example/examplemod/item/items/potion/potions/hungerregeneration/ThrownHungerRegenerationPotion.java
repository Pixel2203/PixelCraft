package com.example.examplemod.item.items.potion.potions.hungerregeneration;

import com.example.examplemod.effect.MobEffectRegistry;
import com.example.examplemod.item.items.potion.CustomThrownPotion;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class ThrownHungerRegenerationPotion extends CustomThrownPotion {
    public ThrownHungerRegenerationPotion(Level p_37535_, LivingEntity p_37536_) {
        super(p_37535_, p_37536_);
    }

    @Override
    protected List<MobEffectInstance> getPotionEffects(int duration, int amplifier) {
        return List.of(new MobEffectInstance(MobEffectRegistry.HUNGER_REGENERATION.get(),duration,amplifier));
    }

}
