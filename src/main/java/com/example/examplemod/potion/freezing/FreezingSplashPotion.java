package com.example.examplemod.potion.freezing;

import com.example.examplemod.potion.CustomSplashPotion;
import com.example.examplemod.potion.flora.ThrownFloraPotion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FreezingSplashPotion extends CustomSplashPotion<ThrownFreezePotion> {
    public FreezingSplashPotion(Properties p_43241_, int duration, int amplifier) {
        super(p_43241_, duration, amplifier);
    }

    @Override
    protected ThrownFreezePotion getThrownPotion(Level level, Player player, CustomSplashPotion potion) {
        return null;
    }
}
