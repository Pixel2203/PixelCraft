package com.example.examplemod.item.items.potion.potions.freezing;

import com.example.examplemod.API.ModUtils;
import com.example.examplemod.API.nbt.NBTHelper;
import com.example.examplemod.API.translation.CustomTranslatable;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.potion.CustomSplashPotionItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    public static ItemStack createFreezePotion(int amount, int duration, int amplifier){
        ItemStack potion = new ItemStack(ItemRegistry.POTION_FREEZE.get(),amount);
        NBTHelper.addCommonNbtData(potion,duration,amplifier);
        ModUtils.setHoverName(potion, CustomTranslatable.POTION_FREEZE_NAME);
        return potion;
    }
}
