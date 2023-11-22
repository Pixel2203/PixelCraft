package com.example.examplemod.item.custom;

import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class HungerRegenerationTalisman extends Item {

    public HungerRegenerationTalisman() {
        super(new Item.Properties());
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        System.out.println("HI");
    }
}
