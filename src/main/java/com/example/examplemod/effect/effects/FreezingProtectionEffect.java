package com.example.examplemod.effect.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FreezingProtectionEffect extends MobEffect {
    private int tickInterval = 0;
    public FreezingProtectionEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {
        Level level = entity.level();
        if(level.isClientSide){
            return;
        }

        boolean meltedAnyBlock = false;
        BlockPos entityPos  = entity.getOnPos();
        for(int x = -2; x <= 2; x++){
            for(int y = -2; y <= 2; y++){
                for(int z = -2; z <= 2; z++){
                    BlockPos foundPos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
                    BlockState foundBlock = level.getBlockState(foundPos);
                    if(foundBlock.is(Blocks.SNOW) || foundBlock.is(Blocks.SNOW_BLOCK) || foundBlock.is(Blocks.POWDER_SNOW)){
                        level.setBlockAndUpdate(foundPos,Blocks.AIR.defaultBlockState());
                        meltedAnyBlock = true;
                    }
                }
            }
        }
        tickInterval++;
        if(tickInterval == 40){
            tickInterval = 0;
            if(meltedAnyBlock){
                //entity.playSound(SoundEvents.LAVA_EXTINGUISH);
                entity.level().playSound(null,entityPos,SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS,1,1);
            }
        }
    }
}
