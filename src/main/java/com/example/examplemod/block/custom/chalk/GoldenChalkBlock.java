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

public class GoldenChalkBlock extends Block{
    public GoldenChalkBlock(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if(!level.isClientSide()){
            return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
        }
        if(interactionHand != InteractionHand.MAIN_HAND){
            return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
        }
        if(!checkForCircle()){
            return InteractionResult.FAIL;
        }
        char[][] blockMap = getBlockMap(level, blockPos);
        return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
    }
    private boolean checkForCircle(){
        return true;
    }
    private char[][] getBlockMap(Level level,BlockPos goldenChalkPos){
        char[][] map = new char[5][5];
        for(int x = -2; x <= 2; x++){
            for(int z=-2; z<=2; z++){
                BlockPos foundBlockPos = new BlockPos(goldenChalkPos.getX() + x, goldenChalkPos.getY() , goldenChalkPos.getZ() + z);
                Block foundBlock = level.getBlockState(foundBlockPos).getBlock();
                if(foundBlock == BlockFactory.WhiteChalkBlock_BLK){
                    map[x+2][z+2]='W';
                    continue;
                }
                if(foundBlock == BlockFactory.GoldenChalkBlock_BLK){
                    map[x+2][z+2]='O';
                    continue;
                }
                map[x+2][z+2] = 'X';

            }
            System.out.println("%s,%s,%s,%s,%s".formatted(map[x+2][0],map[x+2][1],map[x+2][2],map[x+2][3],map[x+2][4]));
        }
        return map;
    }
}
