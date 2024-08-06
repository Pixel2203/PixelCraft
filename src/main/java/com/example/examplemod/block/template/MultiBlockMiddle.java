package com.example.examplemod.block.template;

import com.example.examplemod.api.BlockDynamicUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

;


public class MultiBlockMiddle extends MultiBlockBase {
    private String blockmiddle;
    private String blocktop;
    private String blockbase;
    private boolean tall3;
    private int distanceBase = 1;
    private int distanceTop = 1;


    public MultiBlockMiddle(Properties properties, String blocktop, String blockmiddle, String blockbase, boolean tall3) {
        super(properties, blocktop, blockmiddle,blockbase , tall3);
        this.blocktop = blocktop;
        this.blockmiddle = blockmiddle;
        this.blockbase = blockbase;
        this.tall3 = tall3;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        return;
    }

    @Override
    protected void checkAndDestroy(Level level, BlockPos pos, BlockState blockState) {
        if (BlockDynamicUtil.isBlockPos(level, pos, blockbase, true, distanceBase)) {
            level.destroyBlock(pos.below(distanceBase), false);
            level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos.below(distanceBase));
        }
        if (tall3) /* Check for BlockTop*/{
            if (BlockDynamicUtil.isBlockPos(level, pos, blocktop, false, distanceTop)) {
                level.destroyBlock(pos.above(distanceTop), false);
                level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos.above(distanceTop));
            }
        }
        level.destroyBlock(pos, false);
        level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos);
    }
}