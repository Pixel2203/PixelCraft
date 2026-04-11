package com.example.examplemod.item.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.SoulLightEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SoulLightCrystal extends Item {

    public SoulLightCrystal() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide()) return super.use(level, player, hand);
        if(hand != InteractionHand.MAIN_HAND) return super.use(level, player, hand);
        if(!ModUtils.isBound(player.getMainHandItem())) return super.use(level, player, hand);

        ItemStack stack = player.getItemInHand(hand);
        String boundTo = stack.getTag().getCompound(ExampleMod.MODID).getString(CustomNBTTags.BOUND_TO);
        Entity entity = ((ServerLevel)level).getEntity(UUID.fromString(boundTo));
        stack.shrink(1);

        if(!(entity instanceof Player p)) {
            player.playSound(SoundEvents.ALLAY_DEATH, 1.0F, 1.0F);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        SoulLightEntity soulLight = EntityRegistry.SOUL_LIGHT_ENTITY.get().create(level);
        if(soulLight != null){
            soulLight.setOwner(p);
            soulLight.moveTo(p.position().add(0,2,0));
            level.addFreshEntity(soulLight);
        }

        return super.use(level, player, hand);
    }
}
