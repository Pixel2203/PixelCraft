package com.example.examplemod.potion.hungerregeneration;

import com.example.examplemod.potion.CustomSplashPotion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;

public class HungerRegenerationSplashPotion extends CustomSplashPotion {
    public HungerRegenerationSplashPotion(Properties properties) {
        super(properties);
    }

    @Override
    protected ThrownPotion getThrownPotion(Level level, Player player) {
        return new ThrownHungerRegenerationPotion(level,player);
    }

}
