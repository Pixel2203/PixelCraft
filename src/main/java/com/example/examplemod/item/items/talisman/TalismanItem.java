package com.example.examplemod.item.items.talisman;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Objects;

public abstract class TalismanItem extends Item implements ICurioItem {

    public TalismanItem() {
        super(new Item.Properties().stacksTo(1).defaultDurability(0));
    }
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        Entity entity = slotContext.entity();
        if(!(entity instanceof Player player)){
            return;
        }
        if(prevStack.is(stack.getItem())){
            return;
        }
        if(Objects.isNull(getEquipSound())){
            return;
        }
        player.playSound(getEquipSound());
        if(this instanceof EffectOverTime effectOverTime){
            effectOverTime.effectsToApply().forEach(player::addEffect);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        Entity entity = slotContext.entity();
        if(!(entity instanceof Player player)){
            return;
        }
        if(newStack.is(stack.getItem())){
            return;
        }
        removeAllAppliedEffects(slotContext.entity());
        if(Objects.nonNull(getUnEquipSound())){
            player.playSound(getUnEquipSound());
        }


    }
    public void removeAllAppliedEffects(LivingEntity entity){
        if(this instanceof EffectOverTime effectOverTime){
            effectOverTime.effectsToApply().stream().map(MobEffectInstance::getEffect).forEach(entity::removeEffect);
        }
    }

    protected abstract SoundEvent getEquipSound();
    protected abstract SoundEvent getUnEquipSound();
}
