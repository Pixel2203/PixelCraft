package com.example.examplemod.block.blocks;

import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.entities.KettleBlockEntity;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.tag.TagFactory;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
@SuppressWarnings("deprecation")
public class KettleBlock extends Block implements EntityBlock {
    public static final int EMPTY_FLUID_LEVEL = 0;
    public static final int NEEDED_FLUID_LEVEL_TO_BREW = 3;

    public static final IntegerProperty fluid_level = IntegerProperty.create("kettle_fluid_level", EMPTY_FLUID_LEVEL, NEEDED_FLUID_LEVEL_TO_BREW);
    public static final BooleanProperty isMixture = BooleanProperty.create("kettle_fluid_ismixture");
    public static final BooleanProperty isBoiling = BooleanProperty.create("kettle_fluid_isboling");

    public KettleBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON).randomTicks());
        registerDefaultState(
                this.stateDefinition.any().setValue(fluid_level,0).setValue(isMixture,false).setValue(isBoiling,false)
        );
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return ITickableBlockEntity.getTickerHelper(level);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(fluid_level);
        builder.add(isMixture);
        builder.add(isBoiling);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityRegistry.KETTLE_BLOCK_ENTITY.get().create(blockPos,blockState);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos blockPos, Entity entity, float v) {
        if(level.isClientSide()) return;
        if(!(level.getBlockEntity(blockPos) instanceof KettleBlockEntity kettleBlockEntity)) return;
        kettleBlockEntity.fallOn(entity);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        if(hand != InteractionHand.MAIN_HAND) return InteractionResult.SUCCESS;
        if(!(level.getBlockEntity(blockPos) instanceof KettleBlockEntity blockEntity)) return InteractionResult.SUCCESS;
        ItemStack itemStackInHand = player.getItemInHand(hand);
        if(itemStackInHand.is(TagFactory.KETTLE_ALLOWED_FLUID_ITEMS)) return this.fillFromItem((ServerLevel) level, blockState, blockPos, (ServerPlayer) player);
        if(itemStackInHand.is(Items.GLASS_BOTTLE)) return blockEntity.onBottle((ServerPlayer) player);
        return InteractionResult.FAIL;

    }

    public void resetKettleBlockState(ServerLevel level, BlockState blockState, BlockPos blockPos){
        // Resets the water color
        level.setBlock(blockPos, blockState.setValue(KettleBlock.isMixture, false)
                .setValue(KettleBlock.isBoiling, false)
                .setValue(KettleBlock.fluid_level,0),3);
    }

    public void reduceFluidLevel(ServerLevel serverLevel, BlockState blockState, BlockPos blockPos, int reduceAmount) {
        int previousFluidLevel = blockState.getValue(fluid_level);

        if(previousFluidLevel == EMPTY_FLUID_LEVEL) {
            log.error("Cannot reduce fluid level from empty block");
            return;
        }
        if(previousFluidLevel - reduceAmount ==  EMPTY_FLUID_LEVEL) {
            this.resetKettleBlockState(serverLevel, blockState, blockPos);
            return;
        }
        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(KettleBlock.fluid_level, previousFluidLevel-reduceAmount));
    }


    public boolean isMixture(BlockState blockState) {
        return blockState.getValue(isMixture);
    }

    public void makeMixture(ServerLevel serverLevel, BlockState blockState, BlockPos blockPos) {
        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(KettleBlock.isMixture, true));
    }

    private boolean canFill(BlockState blockState) {
        return blockState.getValue(fluid_level) < NEEDED_FLUID_LEVEL_TO_BREW;
    }
    public boolean isFireBelow(Level level, BlockPos blockPos){
        return level.getBlockState(blockPos.below()).getBlock() == Blocks.FIRE || level.getBlockState(blockPos.below()).getBlock() == Blocks.LAVA;
    }

    private InteractionResult fillFromItem(ServerLevel level, BlockState blockState, BlockPos blockPos, ServerPlayer player) {
        if(!canFill(blockState)) return InteractionResult.FAIL;
        ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);

        int additionalFluidLevel = 0;
        if(itemInHand.is(Items.WATER_BUCKET)){
            additionalFluidLevel = 2;
            if(!player.isCreative()){ // Nur in Survival / Adventure das Item ändern
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
            }
        }else if(itemInHand.is(Items.POTION)){
            additionalFluidLevel = 1;
            itemInHand.shrink(1);
        }

        this.fill(blockPos,blockState,level, additionalFluidLevel);
        return InteractionResult.SUCCESS;
    }
    private void fill(BlockPos blockPos, BlockState blockState, Level level, int fillAmount) {
        int currentFluidLevel = blockState.getValue(fluid_level);
        level.setBlock(blockPos, blockState.setValue(fluid_level,
                Math.min(NEEDED_FLUID_LEVEL_TO_BREW, currentFluidLevel + fillAmount)),3);
    }


}
