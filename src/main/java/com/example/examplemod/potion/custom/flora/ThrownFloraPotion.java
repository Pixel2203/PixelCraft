package com.example.examplemod.potion.custom.flora;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.potion.CustomThrownPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ThrownFloraPotion extends CustomThrownPotion {
    public ThrownFloraPotion(Level p_37535_, LivingEntity p_37536_) {
        super(p_37535_, p_37536_);
    }

    @Override
    protected List<MobEffectInstance> getPotionEffects(int duration, int amplifier) {
        return null;
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult hitResult) {
        int[] potionBounds = getPotionBounds();
        if(Objects.isNull(potionBounds)){
            return;
        }
        int boundRow = potionBounds[0];
        int boundColumn = potionBounds[1];
        BlockPos blockHitPos = hitResult.getBlockPos();
        Random probabilityGenerator = new Random();
        Level level = level();
           for(int row = -boundRow; row <= boundRow; row++){
               for(int column = -boundColumn; column <= boundColumn;column++){
                   if(probabilityGenerator.nextInt(3) <2){
                       continue;
                   }
                   BlockPos foundBlockPos = new BlockPos(
                           (blockHitPos.getX() + row),
                           blockHitPos.getY(),
                           (blockHitPos.getZ() + column)
                   );
                   BlockState foundBlock = level.getBlockState(foundBlockPos);
                   if(foundBlock.getBlock() == Blocks.GRASS_BLOCK){
                       level.setBlockAndUpdate(foundBlockPos.above(),Blocks.WHITE_TULIP.defaultBlockState());
                   }
               }
           }
        super.onHitBlock(hitResult);
    }
    private int[] getPotionBounds(){
        if(!getItem().hasTag()){
            return null;
        }
        CompoundTag nbt = getItem().getTag();
        if(Objects.isNull(nbt)){
            return null;
        }
        return nbt.getIntArray(ExampleMod.MODID + "_potion_bounds");
    }
}
