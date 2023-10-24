package com.example.examplemod.potion.custom.blizzardpoisoning;

import com.example.examplemod.potion.CustomSplashPotion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;

public class BlizzardPoisoningSplashPotion extends CustomSplashPotion {
    public BlizzardPoisoningSplashPotion(Properties p_43241_) {
        super(p_43241_);
    }

    @Override
    protected ThrownPotion getThrownPotion(Level level, Player player) {
        return null;
    }
}
