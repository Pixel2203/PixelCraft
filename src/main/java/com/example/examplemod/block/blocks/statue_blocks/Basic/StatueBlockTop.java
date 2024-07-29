package com.example.examplemod.block.blocks.statue_blocks.Basic;

import com.example.examplemod.block.template.MultiBlockMiddle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StatueBlockTop extends MultiBlockMiddle {
    private boolean tall3;
    protected static final VoxelShape PILLER = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D);
    protected static final VoxelShape PLATE = Block.box(4.0D, 5.0D, 4.0D, 12.0D, 9.0D, 12.0D);
    protected static final VoxelShape SHAPE = Shapes.or(PLATE, PILLER);
    public StatueBlockTop(BlockBehaviour.Properties properties, String blocktop, String blockmiddle, String blockbase, boolean tall3) {
        super(properties, blocktop, blockmiddle, blockbase, tall3);
        this.tall3 = tall3;
    }
    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}