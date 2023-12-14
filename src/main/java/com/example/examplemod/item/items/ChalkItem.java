package com.example.examplemod.item.items;

import com.example.examplemod.block.blocks.ChalkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ChalkItem extends Item {
    protected ChalkBlock blockToPlace;
    public ChalkItem(Properties p_41383_, ChalkBlock blockToPlace) {
        super(p_41383_);
        this.blockToPlace = blockToPlace;
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos().above();
        if(level.isClientSide()){
            return InteractionResult.FAIL;
        }
        if (level.getBlockState(clickedPos).getBlock() != Blocks.AIR) {
            return InteractionResult.FAIL;
        }
        level.setBlockAndUpdate(clickedPos, this.blockToPlace.defaultBlockState());
        return InteractionResult.SUCCESS;
    }
}
