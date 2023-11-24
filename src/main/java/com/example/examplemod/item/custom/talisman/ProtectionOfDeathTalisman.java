package com.example.examplemod.item.custom.talisman;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProtectionOfDeathTalisman extends Talisman implements InstantaneousEffect {

    @Override
    public SoundEvent effectAppliedSound() {
        return SoundEvents.AMETHYST_BLOCK_FALL;
    }
}
