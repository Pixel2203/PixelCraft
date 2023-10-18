package com.example.examplemod.item.custom.ritual;

import com.example.examplemod.block.BlockFactory;
import com.example.examplemod.block.custom.chalk.ChalkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class WhiteChalkItem extends Item {
    public WhiteChalkItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        if(level.isClientSide()){
            return InteractionResult.FAIL;
        }
        if (level.getBlockState(clickedPos).getBlock() == Blocks.AIR) {
            return InteractionResult.FAIL;
        }
        level.setBlockAndUpdate(clickedPos, BlockFactory.WhiteChalkBlock_BLK.defaultBlockState());
        return InteractionResult.SUCCESS;
    }
}
