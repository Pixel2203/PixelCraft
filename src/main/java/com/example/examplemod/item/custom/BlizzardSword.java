package com.example.examplemod.item.custom;

import com.example.examplemod.effect.BlizzardPoisoningEffect;
import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BlizzardSword extends SwordItem {
    public BlizzardSword(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!entity.level().isClientSide()){
            if(entity instanceof LivingEntity livingEntity){
                MobEffectInstance appliedEffect = new MobEffectInstance(MobEffectRegistry.BLIZZARD_POISONING.get(),200,0);
                livingEntity.addEffect(appliedEffect);
            }

        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
