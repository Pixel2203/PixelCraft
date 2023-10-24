package com.example.examplemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FreezeEffect extends MobEffect {
    public FreezeEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        if(!livingEntity.level().isClientSide()) {
            livingEntity.setDeltaMovement(Vec3.ZERO);
            livingEntity.teleportTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        }
        super.applyEffectTick(livingEntity, i);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
