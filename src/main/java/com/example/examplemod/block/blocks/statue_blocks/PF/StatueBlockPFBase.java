package com.example.examplemod.block.blocks.statue_blocks.PF;

import com.example.examplemod.block.template.MultiBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StatueBlockPFBase extends MultiBlockBase {
    protected static final VoxelShape PILLER = Block.box(3.0D, 3.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape PLATE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape SHAPE = Shapes.or(PILLER, PLATE);
    public StatueBlockPFBase(Properties pProperties, String blocktop, String blockmiddle, String blockbase, Boolean tall3) {
        super(pProperties, blocktop, blockmiddle, blockbase, tall3);
    }
    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}
