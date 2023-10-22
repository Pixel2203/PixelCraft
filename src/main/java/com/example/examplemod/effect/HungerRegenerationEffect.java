package com.example.examplemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class HungerRegenerationEffect extends MobEffect {
    private byte effectTicker = 0;
    public HungerRegenerationEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        if(livingEntity.level().isClientSide()){
            this.effectTicker++;
            if(effectTicker <= 100){
                return;
            }
            effectTicker = 0;
            Player player = (Player)livingEntity;
            int currentFoodLevel = player.getFoodData().getFoodLevel();
            FoodData playerFoodData = player.getFoodData();
            boolean hungry = playerFoodData.needsFood();

            if(hungry){
                playerFoodData.eat(i+1,1.0F);
            }
            System.out.println("Exh: "+  playerFoodData.getExhaustionLevel());
            System.out.println("LFL:" + playerFoodData.getLastFoodLevel());
            System.out.println("SAT: "+playerFoodData.getSaturationLevel());



        }
        super.applyEffectTick(livingEntity, i);
    }
}
