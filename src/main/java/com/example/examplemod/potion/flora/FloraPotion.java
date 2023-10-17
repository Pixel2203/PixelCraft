package com.example.examplemod.potion.flora;

import com.example.examplemod.potion.CustomPotion;
import com.example.examplemod.potion.flora.ThrownFloraPotion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FloraPotion extends CustomPotion<ThrownFloraPotion> {
    public FloraPotion(Properties p_43241_) {
        super(p_43241_);
    }

    @Override
    protected ThrownFloraPotion getThrownPotion(Level level, Player player) {
        return new ThrownFloraPotion(level, player);
    }
}
