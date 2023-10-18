package com.example.examplemod.block.custom.chalk;

import com.example.examplemod.block.BlockFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GoldenChalkBlock extends ChalkBlock{
    public GoldenChalkBlock() {
        super();
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if(!level.isClientSide()){
            return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
        }
        if(interactionHand != InteractionHand.MAIN_HAND){
            return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
        }
        if(!checkForCircle(level,blockPos)){
            return InteractionResult.FAIL;
        }
        System.out.println("HEUREKA");
        return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
    }
    private boolean checkForCircle(Level level, BlockPos blockPos){
        char [][] map = getBlockMap(level,blockPos,2);
        if(map[0][1] == 'W' && map[0][2] == 'W' && map[0][3] == 'W'){
            if(map[4][1] == 'W' && map[4][2] == 'W' && map[4][3] == 'W'){
                if(map[1][0] == 'W' && map[2][0] == 'W' && map[3][0] == 'W'){
                    if(map[1][4] == 'W' && map[2][4] == 'W' && map[3][4] == 'W'){
                        return true;
                    }
                }
            }
        }
        return true;
    }
    private char[][] getBlockMap(Level level,BlockPos goldenChalkPos, int range){
        int bounds = range * 2 + 1;
        char[][] map = new char[bounds][bounds];
        for(int x = -range; x <= range; x++){
            for(int z=-range; z<=range; z++){
                BlockPos foundBlockPos = new BlockPos(goldenChalkPos.getX() + x, goldenChalkPos.getY() , goldenChalkPos.getZ() + z);
                Block foundBlock = level.getBlockState(foundBlockPos).getBlock();
                if(foundBlock == BlockFactory.WhiteChalkBlock_BLK){
                    map[x+range][z+range]='W';
                    continue;
                }
                if(foundBlock == BlockFactory.GoldenChalkBlock_BLK){
                    map[x+range][z+range]='O';
                    continue;
                }
                if(foundBlock == Blocks.AIR){
                    map[x+range][z+range] = 'A';
                    continue;
                }
                map[x+range][z+range] = 'U';

            }
            //System.out.println("%s,%s,%s,%s,%s".formatted(map[x][0],map[x][1],map[x][2],map[x][3],map[x][4]));
        }
        return map;
    }
}
