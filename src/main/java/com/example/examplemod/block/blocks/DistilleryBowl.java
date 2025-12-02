package com.example.examplemod.block.blocks;

import com.example.examplemod.api.distilleryBowl.BowlInteraction;
import com.example.examplemod.api.vial.IVialable;
import com.example.examplemod.api.vial.VialResult;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.entities.DistilleryBowlBlockEntity;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class DistilleryBowl extends Block implements EntityBlock, IVialable {
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.5D, 16.0D);
    public static final BooleanProperty isFilled = BooleanProperty.create("filled");

    public static BlockColor bowlBlockColor = (state, tintGetter, blockPos, tintIndex) -> {
        if(state.getValue(isFilled)) {
            if(tintGetter != null && tintGetter.getBlockEntity(blockPos) instanceof DistilleryBowlBlockEntity distilleryBowlBlockEntity) {
                return distilleryBowlBlockEntity.getColor();
            }
        }
        return VialType.WATER.getHexColor();
    };

    public DistilleryBowl() {
        super(BlockBehaviour.Properties.of().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(isFilled, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(isFilled);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        if(level.isClientSide()) return super.use(blockState, level, blockPos, player, hand, p_60508_);
        if(hand != InteractionHand.MAIN_HAND) return InteractionResult.FAIL;

        if(level.getBlockEntity(blockPos) instanceof BowlInteraction bowlInteraction) {
            return bowlInteraction.use(player);
        }

        return InteractionResult.FAIL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityRegistry.DISTILLERY_BOWL_BLOCK_ENTITY.get().create(p_153215_, p_153216_);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ITickableBlockEntity.getTickerHelper(p_153212_);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        if(level.isClientSide()){
            super.onRemove(blockState, level, blockPos, newState, p_60519_);
            return;
        }

        if(!newState.is(blockState.getBlock())) {
            DistilleryBowlBlockEntity blockEntity = (DistilleryBowlBlockEntity)level.getBlockEntity(blockPos);
            blockEntity.drops();
        }
        super.onRemove(blockState, level, blockPos, newState, p_60519_);

    }

    @Override
    public VialResult tap(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof IVialable blockEntity) {
            return blockEntity.tap(level, blockState, blockPos);
        }
        return VialResult.failed();
    }
}
