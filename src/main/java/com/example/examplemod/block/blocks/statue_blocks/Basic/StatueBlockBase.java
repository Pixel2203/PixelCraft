package com.example.examplemod.block.blocks.statue_blocks.Basic;

import com.example.examplemod.block.template.MultiBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StatueBlockBase extends MultiBlockBase {
        protected static final VoxelShape BASE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D);
        protected static final VoxelShape BASE2 = Block.box(3.0D, 3.0D, 3.0D, 13.0D, 5.0D, 13.0D);
        protected static final VoxelShape PILLER = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    protected static final VoxelShape SHAPE = Shapes.or(PILLER, BASE, BASE2);
    public StatueBlockBase(BlockBehaviour.Properties pProperties, String blocktop, String blockmiddle, String blockbase, Boolean tall3) {
        super(pProperties, blocktop, blockmiddle, blockbase, tall3);
    }
    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}
