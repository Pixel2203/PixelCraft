package com.example.examplemod.potion;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;

public abstract class CustomThrownSplashPotion extends ThrownPotion implements ItemSupplier {

    public CustomThrownSplashPotion(Level p_37535_, LivingEntity p_37536_) {
        super(p_37535_, p_37536_);
    }
}
