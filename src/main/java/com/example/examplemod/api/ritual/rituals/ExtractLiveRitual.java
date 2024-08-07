package com.example.examplemod.api.ritual.rituals;

import com.example.examplemod.api.ritual.util.ModRitual;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Random;

public class ExtractLiveRitual extends ModRitual {

    private final int rangeX = 10;
    private final int rangeY = 2;
    private final int rangeZ = 10;

    private int currentX;
    private int currentY;
    private int currentZ;

    private static final Block[] GRASS_BLOCK_REPLACEABLES=
            {
            Blocks.STONE,
            Blocks.STONE,
            Blocks.DIRT,
            Blocks.ANDESITE,
            Blocks.COARSE_DIRT
            };
    public ExtractLiveRitual(Level level, BlockPos blockPos, BlockState blockState, int ritualProgress) {
        super(level, blockPos, blockState, ritualProgress);
        this.currentX = ritualProgress;
        this.currentY = ritualProgress/3;
        this.currentZ = ritualProgress;
    }

    @Override
    public int tick() {
        int changedBlocks = 0;
        if(Objects.isNull(level) || Objects.isNull(blockPos) || Objects.isNull(blockState)){
            return 0;
        }

        if(isFinished()){
            return this.ritualProgress;
        }
        this.currentX++;
        this.currentZ++;
        if(ritualProgress % 3 == 0){
            this.currentY++;
        }


        for(int xOffset = -currentX; xOffset <= currentX; xOffset++){
            int x = blockPos.getX() + xOffset;

            for(int yOffset = -currentY; yOffset <= currentY; yOffset++){
                int y = blockPos.getY() + yOffset;

                for(int zOffset = -currentZ; zOffset <= currentZ; zOffset++){
                    int z = blockPos.getZ() + zOffset;

                    BlockPos pos = new BlockPos(x,y,z);
                    if(level.getBlockState(pos).is(Blocks.GRASS_BLOCK)){
                        level.setBlockAndUpdate(pos,getRandomGrassBlockReplaceable().defaultBlockState());
                        changedBlocks++;
                    }else if(level.getBlockState(pos).is(Blocks.ROSE_BUSH)){
                        Random random = new Random();
                        int probability = random.nextInt(0,4);
                        if(probability == 3){
                            level.setBlockAndUpdate(pos,Blocks.DEAD_BUSH.defaultBlockState());
                        }else{
                            level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
        }
        if(changedBlocks > 0){
            level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,0.25f,1f);
        }
        if(this.currentX >= this.rangeX && this.currentZ >= this.rangeZ && this.currentY >= rangeY){
            this.isFinished = true;
        }
        return ++this.ritualProgress;
    }

    private Block getRandomGrassBlockReplaceable(){
        Random random = new Random();
        int r = random.nextInt(GRASS_BLOCK_REPLACEABLES.length);
        return GRASS_BLOCK_REPLACEABLES[r];
    }

    @Override
    public void finishRitual() {

    }
}
