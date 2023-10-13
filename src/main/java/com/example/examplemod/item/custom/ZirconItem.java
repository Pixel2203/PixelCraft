package com.example.examplemod.item.custom;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ScreenEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ZirconItem extends Item {
    public ZirconItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide() && hand==InteractionHand.MAIN_HAND){
            System.out.println("Player Rightclicked with zirocn!");
        }
        level.addParticle(ParticleTypes.BUBBLE , player.position().x() , player.position().y() , player.position().z()
                , 0,0,0);
        return super.use(level, player, hand);
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.pixelcraft.itemdecs1.tooltip"));

        while (pLevel.isDay()) {
            pTooltipComponents.add(Component.translatable("tooltip.pixelcraft.itemdecs2.tooltip"));
            pTooltipComponents.add(Component.translatable("tooltip.pixelcraft.itemdecs3.tooltip"));}
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }



}
