package com.example.examplemod.block.blocks.statue_blocks.PF;

import com.example.examplemod.block.template.MultiBlockMiddle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StatueBlockPFMiddle extends MultiBlockMiddle {
    private boolean tall3;
    protected static final VoxelShape BASE = Block.box(3.0D, 2.0D, 3.0D, 13.0D, 14.0D, 13.0D);
    protected static final VoxelShape PLATE1 = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    protected static final VoxelShape PLATE2 = Block.box(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape BOOL = Block.box(5.0D, 2, 3.0D, 11.0D, 14.0D, 13.0D);
    protected static final VoxelShape BOOL2 = Block.box(3.0D, 2, 5.0D, 13.0D, 14.0D, 11.0D);
    protected static final VoxelShape SHAPETEMP1 = Shapes.or(PLATE1, PLATE2, BASE);
    protected static final VoxelShape COMBINEDBOOL = Shapes.or(BOOL, BOOL2);
    protected static final VoxelShape SHAPETEMP2 = Shapes.join(SHAPETEMP1, COMBINEDBOOL, BooleanOp.ONLY_FIRST);
    protected static final VoxelShape SHAPE = Shapes.or(SHAPETEMP2);

    public StatueBlockPFMiddle(Properties properties, String blocktop, String blockmiddle, String blockbase, boolean tall3) {
        super(properties, blocktop, blockmiddle, blockbase, tall3);
        this.tall3 = tall3;
    }
    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}