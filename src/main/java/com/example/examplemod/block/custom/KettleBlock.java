package com.example.examplemod.block.custom;

import com.example.examplemod.blockentity.BlockEntityInit;
import com.example.examplemod.blockentity.custom.KettleBlockEntity;
import com.example.examplemod.particle.ParticleFactory;
import com.example.examplemod.tag.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class KettleBlock extends Block implements EntityBlock {
    private static int MIN_FLUID_LEVEL = 0;
    private static int MAX_FLUID_LEVEL = 3;
    public static final IntegerProperty fluid_level = IntegerProperty.create("kettle_fluid_level",MIN_FLUID_LEVEL, MAX_FLUID_LEVEL);
    public KettleBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON));
        registerDefaultState(
                this.stateDefinition.any().setValue(fluid_level,0)
        );
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        if(isFireBelow(pos,neighbor,level.getBlockState(neighbor).getBlock())){

        }
        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(fluid_level);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos blockPos, Entity entity, float v) {
        if(entity instanceof ItemEntity itemEntity){
            ItemStack itemStack = itemEntity.getItem();
            Item item = itemStack.getItem();
            itemStack.setCount(0);
            BlockEntity be = level.getBlockEntity(blockPos);
            if(be instanceof KettleBlockEntity blockEntity){
                blockEntity.saveNewItem(itemStack);
            }

        }
        super.fallOn(level, state, blockPos, entity, v);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND){
            BlockEntity be = level.getBlockEntity(blockPos);
            if(be instanceof KettleBlockEntity blockEntity){
                    ItemStack currentItem = blockEntity.getCurrentSave();
                    player.sendSystemMessage(Component.literal("Item abgerufen %d".formatted(currentItem.getItem().getMaxStackSize())));
                }





            ItemStack itemStackInHand = player.getItemInHand(hand);
            if(!itemStackInHand.is(TagRegistry.KETTLE_ALLOWED_FLUID_ITEMS)){
                return InteractionResult.FAIL;
            }
            level.setBlock(blockPos,blockState.setValue(fluid_level,Math.min(blockState.getValue(fluid_level) + 1, 3)),3);
            level.addParticle(ParticleFactory.CustomBubbleParticle.get(), blockPos.getX() + 0.5D, blockPos.getY() + 1, blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
            return InteractionResult.SUCCESS;
        }
        return super.use(blockState,level,blockPos,player,hand,p_60508_);
    }
    private boolean isFireBelow(BlockPos kettle, BlockPos neighborBlockPos, Block neighborBlock){
        return kettle.below() == neighborBlockPos && neighborBlock == Blocks.FIRE;

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityInit.KETTLE_BLOCK_ENTITY.get().create(p_153215_,p_153216_);
    }

}
