package com.example.examplemod.item.potions;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.level.Level;

public class FloraPotion extends SplashPotionItem {
    public FloraPotion(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_43243_, Player p_43244_, InteractionHand p_43245_) {
        return super.use(p_43243_, p_43244_, p_43245_);
    }
}
