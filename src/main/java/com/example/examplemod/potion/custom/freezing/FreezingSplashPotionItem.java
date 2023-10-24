package com.example.examplemod.potion.custom.freezing;

import com.example.examplemod.API.translation.CustomTranslatable;
import com.example.examplemod.potion.CustomSplashPotionItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FreezingSplashPotionItem extends CustomSplashPotionItem {
    public FreezingSplashPotionItem(Properties p_43241_) {
        super(p_43241_);
    }

    @Override
    protected ThrownFreezePotion getThrownPotion(Level level, Player player) {
        return new ThrownFreezePotion(level,player);
    }

    @Override
    protected Component getTranslatedDescription() {
        return Component.translatable(CustomTranslatable.POTION_FREEZE_DESCRIPTION);
    }
}
