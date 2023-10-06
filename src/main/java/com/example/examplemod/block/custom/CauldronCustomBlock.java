package com.example.examplemod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CauldronCustomBlock extends Block {
    public CauldronCustomBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, blockPos.getX() + 0.5D, blockPos.getY() + 1, blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        return InteractionResult.PASS;
    }
}
