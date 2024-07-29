package com.example.examplemod.block.blocks.statue_blocks.PF;

import com.example.examplemod.block.template.MultiBlockTop;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StatueBlockPFTop extends MultiBlockTop {
    protected static final VoxelShape PILLER = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 13.0D, 12.0D);
    protected static final VoxelShape PLATE = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE = Shapes.or(PILLER, PLATE);

    public StatueBlockPFTop(BlockBehaviour.Properties properties, String blocktop, String blockmiddle, String blockbase, boolean tall3) {
        super(properties, blocktop, blockmiddle, blockbase, tall3);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}