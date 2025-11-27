package com.example.examplemod.item.items;

import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.SoulLightEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
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
            SoulLightEntity soulLight = EntityRegistry.SOUL_LIGHT_ENTITY.get().create(level);
            soulLight.setOwner((ServerPlayer) player);
            soulLight.moveTo(player.position());
            level.addFreshEntity(soulLight);
        }

        return super.use(level, player, hand);
    }
}
