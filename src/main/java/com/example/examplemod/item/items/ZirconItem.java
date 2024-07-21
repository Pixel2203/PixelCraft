package com.example.examplemod.item.items;

import com.example.examplemod.capabilities.PlayerSoulEnergyProvider;
import com.example.examplemod.networking.NetworkMessages;
import com.example.examplemod.networking.packets.ExampleC2SPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZirconItem extends Item {
    private static final Logger log = LoggerFactory.getLogger(ZirconItem.class);

    public ZirconItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide() && hand==InteractionHand.MAIN_HAND){
            System.out.println("Player Rightclicked with zirocn!");
            level.addParticle(ParticleTypes.BUBBLE , player.position().x() , player.position().y() , player.position().z(), 0,0,0);
            return super.use(level, player, hand);
        }

        return super.use(level, player, hand);
    }
}
