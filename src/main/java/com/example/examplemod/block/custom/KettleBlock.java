package com.example.examplemod.block.custom;

import com.example.examplemod.particle.ParticleFactory;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class KettleBlock extends Block {
    private int MIN_FLUID_LEVEL = 0;
    private int MAX_FLUID_LEVEL = 3;
    public IntegerProperty fluid_level = IntegerProperty.create("kettle_fluid_level",0, 3);
    public KettleBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON));
        this.defaultBlockState().setValue(fluid_level,0);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos blockPos, Entity entity, float v) {
        if(entity instanceof ItemEntity itemEntity){
            ItemStack itemStack = itemEntity.getItem();
            Item item = itemStack.getItem();
            itemStack.setCount(0);


        }
        super.fallOn(level, state, blockPos, entity, v);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        Item clickedItem = player.getItemInHand(hand).getItem();
        int currentFluidLevel = blockState.getValue(fluid_level);
        int additionalFluidLevel = 0;
        boolean update = false;
        if(clickedItem == Items.WATER_BUCKET){
            additionalFluidLevel = 3;
            update = true;
        }
        if(clickedItem == Items.POTION){
            additionalFluidLevel = 1;
            update = true;
        }

        if(currentFluidLevel == 3){
            return InteractionResult.FAIL;
        }
        if(update){
            BlockState blockState1 = blockState.setValue(fluid_level,Math.min(3,currentFluidLevel + additionalFluidLevel));
            if(blockState.getValue(fluid_level) < MAX_FLUID_LEVEL) {
                level.setBlock(blockPos, blockState.setValue(fluid_level, Math.min(3,currentFluidLevel + additionalFluidLevel)), 3);
            }
        }



        level.addParticle(ParticleFactory.CustomBubbleParticle.get(), blockPos.getX() + 0.5D, blockPos.getY() + 1, blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        return InteractionResult.PASS;
    }
}
