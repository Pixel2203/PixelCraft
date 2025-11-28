package com.example.examplemod.block.blocks;

import com.example.examplemod.item.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
public class GlimmerGras extends BushBlock {

    private static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);
    private static final VoxelShape SHAPE_AGE_0 = Block.box(0,0,0, 16, 4,16);
    private static final VoxelShape SHAPE_AGE_1 = Block.box(0,0,0, 16, 8,16);
    private static final VoxelShape SHAPE_AGE_2 = Block.box(0,0,0, 16, 12,16);
    private final int NEEDED_LIGHT_LEVEL = 15;

    public GlimmerGras() {
        super(BlockBehaviour.Properties.copy(Blocks.GRASS).lightLevel(v -> 7));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        int age = blockState.getValue(AGE);
        return switch (age) {
            case 0 -> SHAPE_AGE_0;
            case 1 -> SHAPE_AGE_1;
            case 2 -> SHAPE_AGE_2;
            default -> SHAPE_AGE_0;
        };
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_49921_) {
        return true;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(!level.isClientSide) {
            ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(!itemInHand.is(Items.SHEARS)) return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
            itemInHand.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            popResource(level,pos, new ItemStack(ItemRegistry.GLIMMER_LEAF.get()));

        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);


    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource p_222957_) {
        super.randomTick(blockState, level, blockPos, p_222957_);
        if(this.isFullyGrown(blockState)) {
            this.spread(level, blockPos, p_222957_);
            return;
        }

        if(this.canGrow(level, blockState, blockPos)) {
            this.grow(level, blockState, blockPos);
        }

    }

    private boolean isFullyGrown(BlockState blockState) {
        int currentAge = blockState.getValue(AGE);
        return currentAge == MAX_AGE;
    }

    private boolean canGrow(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        return hasEnoughLight(level, blockPos) && !isFullyGrown(blockState);
    }

    private void grow(ServerLevel level, BlockState  state, BlockPos pos) {
        level.setBlockAndUpdate(pos, state.setValue(AGE, state.getValue(AGE) + 1));
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return this.canPlantExist(levelReader, blockPos, blockState);
    }


    private boolean hasEnoughLight(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) >= NEEDED_LIGHT_LEVEL;
    }

    private boolean canPlantExist(LevelReader level, BlockPos pos, BlockState blockState) {
        return hasEnoughLight(level, pos) && super.canSurvive(blockState, level, pos);
    }

    private boolean canSpreadTo(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).isAir()
                && hasEnoughLight(level, pos)
                && level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK);
    }


    private void spread(ServerLevel level, BlockPos pos, RandomSource random) {
        int dx = random.nextInt(3) - 1; // -1, 0, 1
        int dz = random.nextInt(3) - 1; // -1, 0, 1
        if (dx == 0 && dz == 0) return; // eigene Position überspringen

        BlockPos targetPos = pos.offset(dx, 0, dz);
        if(!canSpreadTo(level, targetPos)) return;
        level.setBlockAndUpdate(targetPos, this.defaultBlockState());
    }



}
