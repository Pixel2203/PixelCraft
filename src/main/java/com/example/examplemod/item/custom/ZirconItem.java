package com.example.examplemod.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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
}
