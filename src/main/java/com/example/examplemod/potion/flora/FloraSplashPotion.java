package com.example.examplemod.potion.flora;

import com.example.examplemod.potion.CustomSplashPotion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FloraSplashPotion extends CustomSplashPotion<ThrownFloraPotion> {


    public FloraSplashPotion(Properties p_43241_) {
        super(p_43241_);
    }


    @Override
    protected ThrownFloraPotion getThrownPotion(Level level, Player player) {
        return new ThrownFloraPotion(level, player);
    }
}
