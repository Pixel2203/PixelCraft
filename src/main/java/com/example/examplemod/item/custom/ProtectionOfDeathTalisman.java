package com.example.examplemod.item.custom;

import com.example.examplemod.item.custom.talisman.InstantaneousEffect;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ProtectionOfDeathTalisman extends Talisman implements InstantaneousEffect {

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean p_41408_) {
    }

    @Override
    public SoundEvent effectAppliedSound() {
        return null;
    }
}
