package com.example.examplemod.item.items.potion.potions.flora;

import com.example.examplemod.api.BlockInfo;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.item.items.potion.CustomThrownPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ThrownFloraPotion extends CustomThrownPotion {
    private static final List<Block> FLOWERS_TO_PLACE = List.of(
            Blocks.WHITE_TULIP,
            Blocks.RED_TULIP,
            Blocks.ORANGE_TULIP,
            Blocks.ROSE_BUSH
    );
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
        int inflateX = potionBounds[0];
        int inflateY = potionBounds[1];
        int inflateZ = potionBounds[2];
        BlockPos blockHitPos = hitResult.getBlockPos();
        Random probabilityGenerator = new Random();
        Level level = level();
        AABB boundingBox = new AABB(blockHitPos);

        ModUtils.getBlocksInBoundingBox(level,  boundingBox.inflate(inflateX,inflateY,inflateZ))
                .stream()
                .filter(blockInfo -> blockInfo.blockState().is(Blocks.GRASS_BLOCK))
                .map(BlockInfo::blockPos)
                .map(BlockPos::above)
                .filter(blockPos -> level.getBlockState(blockPos).is(Blocks.AIR))
                .filter(blockPos -> probabilityGenerator.nextInt(3) < 2)
                .forEach(blockPos -> {
                    int flowerIndex = probabilityGenerator.nextInt(FLOWERS_TO_PLACE.size());
                    var flowerToPlace = FLOWERS_TO_PLACE.get(flowerIndex).defaultBlockState();
                    level.setBlockAndUpdate(blockPos, flowerToPlace);
                });
        /*
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
                       int flowerIndex = probabilityGenerator.nextInt(FLOWERS_TO_PLACE.size());
                       level.setBlockAndUpdate(foundBlockPos.above(), FLOWERS_TO_PLACE.get(flowerIndex).defaultBlockState());

                   }
               }
           }

         */
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
        return nbt.getIntArray(CustomNBTTags.POTION_BOUNDS);
    }
}
