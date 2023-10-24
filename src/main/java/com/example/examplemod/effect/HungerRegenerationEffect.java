package com.example.examplemod.effect;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.level.NoteBlockEvent;

public class HungerRegenerationEffect extends MobEffect {
    private byte effectTicker = 0;
    private final byte tickInterval = 20;
    public HungerRegenerationEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }


    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        if(!livingEntity.level().isClientSide()){
            if(livingEntity instanceof Player player){
                this.effectTicker++;
                if(effectTicker <= this.tickInterval){
                    return;
                }
                effectTicker = 0;
                FoodData playerFoodData = player.getFoodData();
                boolean hungry = playerFoodData.needsFood();

                if(hungry){
                    playerFoodData.eat(i+1,1.0F);
                }
            }




        }
        super.applyEffectTick(livingEntity, i);
    }
}
