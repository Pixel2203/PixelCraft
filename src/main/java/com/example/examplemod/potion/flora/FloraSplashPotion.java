package com.example.examplemod.potion.flora;

import com.example.examplemod.potion.CustomSplashPotion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FloraSplashPotion extends CustomSplashPotion<ThrownFloraPotion> {


    public FloraSplashPotion(Properties p_43241_, int duration, int amplifier) {
        super(p_43241_, duration, amplifier);
    }


    @Override
    protected ThrownFloraPotion getThrownPotion(Level level, Player player, CustomSplashPotion potion) {
        return new ThrownFloraPotion(level, player,potion);
    }
}
