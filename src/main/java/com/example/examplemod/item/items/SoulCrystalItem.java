package com.example.examplemod.item.items;

import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.entity.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.C;

public class SoulCrystalItem extends Item {
    public SoulCrystalItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide && hand == InteractionHand.MAIN_HAND){
        }
        return super.use(level, player, hand);
    }



    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        if(player.level().isClientSide() || hand == InteractionHand.OFF_HAND){
            return InteractionResult.PASS;
        }
        if(entity.getType() == EntityRegistry.SOUL_ENTITY.get()){
            CompoundTag nbt;
            if(itemStack.hasTag()){
                nbt = itemStack.getTag();
            }else {
                nbt = new CompoundTag();
            }
            float charge = nbt.getFloat(CustomNBTTags.ENERGY_CHARGE);

        }

        return super.interactLivingEntity(itemStack, player, entity, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack p_41430_, int p_41431_) {
        super.onUseTick(level, entity, p_41430_, p_41431_);
    }
}
