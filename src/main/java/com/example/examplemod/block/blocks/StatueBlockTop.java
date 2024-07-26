package com.example.examplemod.block.blocks;

import com.example.examplemod.block.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StatueBlockTop extends StatueBlock{

    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);
    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        return;
    }

    @Override
    protected void checkAndDestroy(Level level, BlockPos pos, BlockState blockState) {
        if (level.getBlockState(pos.below()).getBlock() == BlockRegistry.StatuePolishedTuffBlock.get()) {
            level.destroyBlock(pos.below(), true);
//            Block.dropResources(blockState, level, pos.below(), null);
            level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos.below());
        }
        level.destroyBlock(pos, true);
        level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos);
    }
}
