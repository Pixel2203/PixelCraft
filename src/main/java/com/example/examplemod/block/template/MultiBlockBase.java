package com.example.examplemod.block.template;

import com.example.examplemod.api.BlockDynamicUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public class MultiBlockBase extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private String blockmiddle;
    private String blocktop;
    private String blockbase;
    private boolean tall3 = false;
    private int distanceMiddle = 1;
    private int distanceTop = 2;
    /**
     * @param tall3 If true, the placement is defined to place 3 Blocks instead of 2
     */
    public MultiBlockBase(Properties pProperties, String blocktop, String blockmiddle, String blockbase, Boolean tall3) {
        super(pProperties);
        this.blocktop = blocktop;
        this.blockmiddle = blockmiddle;
        this.blockbase = blockbase;
        this.tall3 = tall3;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);

    }
    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        checkAndDestroy(level, pos, state);
        return true;
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        checkAndDestroy(level, pos, state);
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    protected void checkAndDestroy(Level level, BlockPos pos, BlockState blockState) {
        if (level.isClientSide) {
            return;
        }

        if (BlockDynamicUtil.isBlockPos(level, pos, blockmiddle, false, distanceMiddle)) {
            level.destroyBlock(pos.above(distanceMiddle), false);
        }

        if (tall3) {
            if (BlockDynamicUtil.isBlockPos(level, pos, blocktop, false, distanceTop)) {
                level.destroyBlock(pos.above(distanceTop), false);
            }
        }
        level.destroyBlock(pos, false);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        Direction facing = blockState.getValue(FACING);
        BlockState BlockStateMiddle = BlockDynamicUtil.getBlockFromString(blockmiddle).defaultBlockState().setValue(FACING, facing);
        if (tall3) {
            BlockState BlockStateTop = BlockDynamicUtil.getBlockFromString(blocktop).defaultBlockState().setValue(FACING, facing);
            level.setBlock(blockPos.above(distanceTop), BlockStateTop, 3);

        }
        level.setBlock(blockPos.above(distanceMiddle), BlockStateMiddle, 3);

    }
}

