package com.example.examplemod.effect;

import it.unimi.dsi.fastutil.ints.Int2IntSortedMaps;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HungerRegeneration extends MobEffect {
    public HungerRegeneration(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        if(livingEntity.level().isClientSide()){
            Player player = (Player)livingEntity;
            int currentFoodLevel = player.getFoodData().getFoodLevel();
            boolean hungry = player.getFoodData().needsFood();
            if(hungry){
                player.getFoodData().setFoodLevel(Math.min(20,currentFoodLevel+1));
            }


        }
        super.applyEffectTick(livingEntity, i);
    }
}
