package com.example.examplemod.item.custom;

import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class HungerRegenerationTalisman extends Talisman {

    @Override
    protected List<MobEffectInstance> getEffects() {
        return List.of(
                new MobEffectInstance(MobEffectRegistry.HUNGER_REGENERATION.get(),200,0)
        );
    }

    @Override
    protected SoundEvent getSoundOnGrantEffect() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }
}
