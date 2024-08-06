package com.example.examplemod.item.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MultiBlockBlockItem extends BlockItem {

    private int Distance;
    public MultiBlockBlockItem(Block block, Properties properties, int Distance) {
        super(block, properties);
        this.Distance = Distance;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (isSpaceAvailable(level, pos)) {
            return super.place(context);
        }
        else {
            return InteractionResult.FAIL;
        }
    }

    private boolean isSpaceAvailable(Level level, BlockPos pos) {
        int Dis = 0;

        for (int i = 1; i <= Distance; i++) {
            if (level.getBlockState(pos.above(i)).isAir()) {
                Dis++;
            }
            if (Dis == Distance) {
                return true;
            }
        }
        return false;
    }
}
