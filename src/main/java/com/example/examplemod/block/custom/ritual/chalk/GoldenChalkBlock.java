package com.example.examplemod.block.custom.ritual.chalk;

import com.example.examplemod.block.BlockFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        if(!checkForSmallCircle(level,blockPos)){
            return InteractionResult.FAIL;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, hitResult);
    }

    private ItemEntity[] getItemEntitesInRangeFromBlockPos(Level level, BlockPos blockPos, int range){
        return null;
    }



    /**
     * Checks if a circle with white chalk has been drawn around the Center (Golden Chalk)
     * Structure:
     *  W = White Chalk
     *  A = AIR
     *  U = Any other Block
     * U W W W U
     * W A A A W
     * W A O A W
     * W A A A W
     * U W W W U
     * @param level
     * @param blockPos
     * @return
     */
    private boolean checkForSmallCircle(Level level, BlockPos blockPos){
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
        return false;
    }

    /**
     * Gets all Blocks in a given radius around a certain BlockPos
     * Returns a 2D Char Map
     * 'A' = MINECRAFT:AIR
     * 'W' = PIXELCRAFT:WHITE_CHALK
     * 'U' = ANY OTHER BLOCK
     * 'O' = PIXELCRAFT:GOLDEN_CHALK
     * @param level
     * @param goldenChalkPos
     * @param range
     * @return 2D Char Array - Char[][]
     */
    private char[][] getBlockMap(Level level,BlockPos goldenChalkPos, int range){
        int bounds = range * 2 + 1;
        char[][] map = new char[bounds][bounds];
        int goldenChalkX = goldenChalkPos.getX();
        int goldenChalkZ = goldenChalkPos.getZ();

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                BlockPos foundBlockPos = new BlockPos(goldenChalkX + x, goldenChalkPos.getY(), goldenChalkZ + z);
                Block foundBlock = level.getBlockState(foundBlockPos).getBlock();

                int i = x + range;
                int j = z + range;

                if (foundBlock == BlockFactory.WhiteChalkBlock_BLK) {
                    map[i][j] = 'W';
                } else if (foundBlock == BlockFactory.GoldenChalkBlock_BLK) {
                    map[i][j] = 'O';
                } else if (foundBlock == Blocks.AIR) {
                    map[i][j] = 'A';
                } else {
                    map[i][j] = 'U';
                }
            }
        }

        return map;

    }
}
