package com.example.examplemod.item.items.potion.potions.blizzardpoisoning;

import com.example.examplemod.item.items.potion.CustomSplashPotionItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;

public class BlizzardPoisoningSplashPotionItem extends CustomSplashPotionItem {
    public BlizzardPoisoningSplashPotionItem(Properties p_43241_) {
        super(p_43241_);
    }

    @Override
    protected ThrownPotion getThrownPotion(Level level, Player player) {
        return null;
    }
}
