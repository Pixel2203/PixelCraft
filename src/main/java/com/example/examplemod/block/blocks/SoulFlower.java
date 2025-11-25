package com.example.examplemod.block.blocks;

import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.entities.SoulFlowerBlockEntity;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.SoulWispEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Slf4j
public class SoulFlower extends BushBlock implements EntityBlock {

    public static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
    public static final BooleanProperty NIGHT_ACTIVE = BooleanProperty.create("night_active");

    public SoulFlower() {
        super(BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(value -> {
            if(value.getValue(NIGHT_ACTIVE)) {
                return 10;
            }
            return 0;
        }
        ));
        this.registerDefaultState(this.stateDefinition.any().setValue(NIGHT_ACTIVE, false));
    }

    @Override
    public VoxelShape getShape(BlockState p_53517_, BlockGetter p_53518_, BlockPos p_53519_, CollisionContext p_53520_) {
        return SHAPE;
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_49921_) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos blockPos, RandomSource p_222957_) {
        boolean isNightTransformed = state.getValue(NIGHT_ACTIVE);
        if(level.isNight() && isNightTransformed) {

        }

        if(level.isNight()) {
            if(isNightTransformed) {
                this.spawnSoulFragment(level, blockPos);
            }else {
                level.setBlockAndUpdate(blockPos, state.setValue(NIGHT_ACTIVE, true));
                if(level.getBlockEntity(blockPos) instanceof SoulFlowerBlockEntity soulFlowerBlockEntity) {
                    soulFlowerBlockEntity.nightActivationStatusChanged();
                }
            }
        }else {
            if(isNightTransformed) {
                level.setBlockAndUpdate(blockPos, state.setValue(NIGHT_ACTIVE, false));
                if(level.getBlockEntity(blockPos) instanceof SoulFlowerBlockEntity soulFlowerBlockEntity) {
                    soulFlowerBlockEntity.nightActivationStatusChanged();
                }
            }
        }




        super.randomTick(state, level, blockPos, p_222957_);


    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(NIGHT_ACTIVE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityRegistry.SOUL_FLOWER_BLOCK_ENTITY.get().create(p_153215_, p_153216_);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ITickableBlockEntity.getTickerHelper(p_153212_);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(level.isClientSide()) return;
        BlockEntity blockEntity = level.getBlockEntity(p_60517_);
        if(blockEntity instanceof SoulFlowerBlockEntity soulFlowerBlockEntity) {
            soulFlowerBlockEntity.notifyFogBlocksAboutFlowerRemoval();
        }
        super.onRemove(blockState, level, p_60517_, p_60518_, p_60519_);
    }

    private void spawnSoulFragment(ServerLevel level, BlockPos pos) {
        SoulWispEntity wisp = EntityRegistry.SOUL_WISP_ENTITY.get().create(level);
        if(Objects.isNull(wisp)) {
            log.error("Could not create soul wisp entity!");
            return;
        }

        double spawnX = pos.getX() + 0.5 + level.random.nextDouble() * 5.0 ;
        double spawnY = pos.getY() + 1.5 + level.random.nextDouble() * 5.0 ;
        double spawnZ = pos.getZ() + 0.5 + level.random.nextDouble() * 5.0 ;
        wisp.moveTo(spawnX, spawnY, spawnZ, level.random.nextFloat() * 360.0F, 0.0F);
        wisp.setTargetFlowerPos(pos);
        level.addFreshEntity(wisp);
    }
}
