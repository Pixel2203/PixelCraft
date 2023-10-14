package com.example.examplemod.block.custom;

import com.example.examplemod.API.brewing.kettle.KettleAPI;
import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipeFactory;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import com.example.examplemod.API.brewing.kettle.result.ResultTypes;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.custom.KettleBlockEntity;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.particle.ParticleFactory;
import com.example.examplemod.tag.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class KettleBlock extends Block implements EntityBlock {
    public static int MIN_FLUID_LEVEL = 0;
    public static int MAX_FLUID_LEVEL = 3;
    public static final IntegerProperty fluid_level = IntegerProperty.create("kettle_fluid_level",MIN_FLUID_LEVEL, MAX_FLUID_LEVEL);
    public KettleBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON).randomTicks());
        registerDefaultState(
                this.stateDefinition.any().setValue(fluid_level,0)
        );
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource p_222957_) { //randomness dX dZ -- -- dY
        serverLevel.sendParticles(ParticleFactory.CustomBubbleParticle.get(), blockPos.getX(),blockPos.getY(),blockPos.getZ(),0,2,1,1,1);
        super.randomTick(blockState, serverLevel, blockPos, p_222957_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return ITickableBlockEntity.getTickerHelper(level);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(fluid_level);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityRegistry.KETTLE_BLOCK_ENTITY.get().create(blockPos,blockState);
    }
    @Override
    public void fallOn(Level level, BlockState state, BlockPos blockPos, Entity entity, float v) {
        if(!level.isClientSide() && state.getValue(fluid_level) > MIN_FLUID_LEVEL && entity instanceof ItemEntity itemEntity){
            if(level.getBlockEntity(blockPos) instanceof KettleBlockEntity kettleBlockEntity && !kettleBlockEntity.isProgressing()) {
                if(!isFireBelow(level,blockPos)){
                    return;
                }
                if (KettleAPI.hasIngredientTag(itemEntity.getItem())) {
                    handleIngredientFallOnKettle(itemEntity.getItem(), kettleBlockEntity);
                    return;
                }
            }

        }
        super.fallOn(level, state, blockPos, entity, v);
    }
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && level.getBlockEntity(blockPos) instanceof KettleBlockEntity blockEntity){

            int currentKettleFluidLevel = blockState.getValue(fluid_level);
            player.sendSystemMessage(Component.literal("Recipe: " + blockEntity.getSerializedKettleRecipe()));

            ItemStack itemStackInHand = player.getItemInHand(hand);

            if(itemStackInHand.is(TagRegistry.KETTLE_ALLOWED_FLUID_ITEMS) && currentKettleFluidLevel < MAX_FLUID_LEVEL){
                handleFillKettleWithFluid(itemStackInHand,blockPos,blockState,level);
                return InteractionResult.SUCCESS;
            }

            boolean hasIngredients = !StringUtil.isNullOrEmpty(blockEntity.getSerializedKettleRecipe());
            if(itemStackInHand.is(Items.GLASS_BOTTLE) && hasIngredients){
                KettleRecipe foundRecipe = KettleAPI.getRecipeBySerializedIngredientList(blockEntity.getSerializedKettleRecipe());
                if(Objects.isNull(foundRecipe)){return InteractionResult.FAIL;}
                if(foundRecipe.resultType() != ResultTypes.POTION){
                    return InteractionResult.FAIL;
                }
                boolean worked = handleFillFluidInBottle(player,level,blockPos,blockState,hand,itemStackInHand,foundRecipe,blockEntity);
                return worked ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            }
            //level.addParticle(ParticleFactory.CustomBubbleParticle.get(), blockPos.getX() + 0.5D, blockPos.getY() + 1, blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);

        }
        return InteractionResult.SUCCESS;
    }


    private boolean handleFillFluidInBottle(
            Player player,
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            InteractionHand hand,
            ItemStack bottleItemStack,
            KettleRecipe foundRecipe,
            KettleBlockEntity blockEntity) {
        // Decrease Bottle ItemStack Count if > 0; IF == 0 Then replace it

        if(foundRecipe == null){
            return false;
        }


        int bottleCount = bottleItemStack.getCount();
        if(bottleCount == 0){
            player.setItemInHand(hand, foundRecipe.result());
        }
        if(bottleCount > 0){
            bottleItemStack.shrink(1);
            player.addItem(foundRecipe.result());
        }

        // IF Fluid level is going to be 0 , remove all ingredients from the kettle entity
        int newKettleFluidLevel = blockState.getValue(fluid_level)-1;
        if(newKettleFluidLevel == 0){
            blockEntity.resetContent();
        }
        level.setBlock(blockPos, blockState.setValue(fluid_level,newKettleFluidLevel),3);
        return true;
    }


    private void handleFillKettleWithFluid(ItemStack itemInHand, BlockPos blockPos, BlockState kettleBlockState, Level level){
        int currentKettleWaterLevel = kettleBlockState.getValue(fluid_level);
        if(currentKettleWaterLevel == MAX_FLUID_LEVEL){
            return;
        }
        int additionalFluidLevel = 0;
        if(itemInHand.is(Items.WATER_BUCKET)){
           additionalFluidLevel = 2;
        }else if(itemInHand.is(Items.POTION)){
            additionalFluidLevel = 1;
        }
        level.setBlock(blockPos, kettleBlockState.setValue(fluid_level,
                Math.min(MAX_FLUID_LEVEL, kettleBlockState.getValue(fluid_level)+additionalFluidLevel)),3);

    }
    public static void handleIngredientFallOnKettle(ItemStack itemStack, KettleBlockEntity entity) {

        KettleIngredient foundMatchingIngredient = KettleAPI.getIngredientByItem(itemStack.getItem());
        if(foundMatchingIngredient == null){
            return;
        }
        String serializedRecipe = entity.getSerializedKettleRecipe();
        String nextRecipeString = KettleRecipeFactory.getNextRecipeString(serializedRecipe, foundMatchingIngredient);
        //if(KettleAPI.isPartOfOrCompleteRecipe(nextRecipeString)){
            acceptIngredient(itemStack, entity, foundMatchingIngredient);
        //}
        if(KettleAPI.isValidRecipe(nextRecipeString)){
            KettleRecipe recipe = KettleAPI.getRecipeBySerializedIngredientList(nextRecipeString);
            if(recipe.resultType() == ResultTypes.ITEM){
                entity.startBrewing();
            }

        }
    }
    private static void acceptIngredient(ItemStack itemStack, KettleBlockEntity entity, KettleIngredient ingredient){
        entity.add(ingredient);
        itemStack.shrink(1);
        entity.getLevel().playSound(null, entity.getBlockPos(), SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS,0.25f,1f);
    }
    public boolean isFireBelow(Level level, BlockPos blockPos){
        return level.getBlockState(blockPos.below()).getBlock() == Blocks.FIRE;
    }
}
