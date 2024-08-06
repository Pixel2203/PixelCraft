package com.example.examplemod.block.template;

import com.example.examplemod.api.BlockDynamicUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class MultiBlockTop extends MultiBlockBase {
    private String blockmiddle;
    private String blocktop;
    private String blockbase;
    private int distanceMiddle = 1;
    private int distanceBase = 2;

    public MultiBlockTop(Properties properties, String blocktop, String blockmiddle, String blockbase, boolean tall3) {
        super(properties, blocktop, blockmiddle, blockbase, tall3);
        this.blocktop = blocktop;
        this.blockmiddle = blockmiddle;
        this.blockbase = blockbase;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        return;
    }

    @Override
    protected void checkAndDestroy(Level level, BlockPos pos, BlockState blockState) {
        if (level.isClientSide) {
            return;
        }
        if (BlockDynamicUtil.isBlockPos(level, pos, blockmiddle, true, distanceMiddle)) {
            level.destroyBlock(pos.below(distanceMiddle), false);
//            level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos.below(distanceMiddle));
        }
        if (BlockDynamicUtil.isBlockPos(level, pos, blockbase, true, distanceBase)) {
            level.destroyBlock(pos.below(distanceBase), false);
//            level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos.below(distanceBase));
        }
        level.destroyBlock(pos, false);
//        level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos);
    }
}