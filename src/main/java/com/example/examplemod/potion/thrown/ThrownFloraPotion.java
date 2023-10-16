package com.example.examplemod.potion.thrown;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class ThrownFloraPotion extends ThrownPotion implements ItemSupplier {
    public ThrownFloraPotion(Level p_37535_, LivingEntity p_37536_) {
        super(p_37535_, p_37536_);
    }
    @Override
    protected void onHitBlock(@NotNull BlockHitResult hitResult) {
        ItemStack potion = this.getItem();
        if(!potion.hasTag()){
            return;
        }
        CompoundTag nbt = potion.getTag();
        if(Objects.isNull(nbt)){
            return;
        }
        int potionLevel = nbt.getInt(ExampleMod.MODID+ "_potionLevel");
        int[] potionBounds = nbt.getIntArray(ExampleMod.MODID + "_potionbounds");
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
}
