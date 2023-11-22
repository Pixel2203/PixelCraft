package com.example.examplemod.item.custom;

import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class Talisman extends Item {


    public Talisman() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
        System.out.println("HELLO");
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean p_41408_) {
        if(!(entity instanceof Player player)){
            return;
        }
        if(level.isClientSide){
            if(player.hasEffect(MobEffectRegistry.HUNGER_REGENERATION.get())){
                return;
            }
            player.playSound(getSoundOnGrantEffect());
        }
        getEffects().forEach(player::addEffect);
    }


    protected abstract List<MobEffectInstance> getEffects();

    protected abstract SoundEvent getSoundOnGrantEffect();

}
