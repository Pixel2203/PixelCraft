package com.example.examplemod.block.blocks;

import com.example.examplemod.API.APIHelper;
import com.example.examplemod.API.ingredient.IngredientAPI;
import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.recipe.ModRecipe;
import com.example.examplemod.API.recipe.RecipeAPI;
import com.example.examplemod.API.result.ResultTypes;
import com.example.examplemod.blockentity.entities.KettleBlockEntity;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.tag.TagFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class KettleBlock extends Block implements EntityBlock {
    public static final int MIN_FLUID_LEVEL = 0;
    public static final int MAX_FLUID_LEVEL = 3;

    public static final IntegerProperty fluid_level = IntegerProperty.create("kettle_fluid_level",MIN_FLUID_LEVEL, MAX_FLUID_LEVEL);
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
        if(!level.isClientSide() && state.getValue(fluid_level) > MIN_FLUID_LEVEL && entity instanceof ItemEntity itemEntity){
            if(level.getBlockEntity(blockPos) instanceof KettleBlockEntity kettleBlockEntity && !kettleBlockEntity.isProgressing()) {
                if(!isFireBelow(level,blockPos)){
                    return;
                }
                if (IngredientAPI.isKettleIngredient(itemEntity.getItem())) {
                    handleIngredientFallOnKettle(itemEntity.getItem(), kettleBlockEntity);
                    return;
                }
            }

        }
        super.fallOn(level, state, blockPos, entity, v);
    }
    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && level.getBlockEntity(blockPos) instanceof KettleBlockEntity blockEntity){

            int currentKettleFluidLevel = blockState.getValue(fluid_level);
            //player.sendSystemMessage(Component.literal("Recipe: " + blockEntity.getSerializedKettleRecipe()));

            ItemStack itemStackInHand = player.getItemInHand(hand);

            if(itemStackInHand.is(TagFactory.KETTLE_ALLOWED_FLUID_ITEMS) && currentKettleFluidLevel < MAX_FLUID_LEVEL){
                handleFillKettleWithFluid(itemStackInHand,blockPos,blockState,level);
                return InteractionResult.SUCCESS;
            }

            boolean hasIngredients = !StringUtil.isNullOrEmpty(blockEntity.getSerializedKettleRecipe());
            if(itemStackInHand.is(Items.GLASS_BOTTLE) && hasIngredients){
                Optional<ModRecipe<?>> foundRecipeOptional = RecipeAPI.getRecipeBySerializedIngredients(RecipeAPI.KETTLE_RECIPES,blockEntity.getSerializedKettleRecipe());
                if(foundRecipeOptional.isEmpty()){return InteractionResult.FAIL;}
                ModRecipe<?> foundRecipe = foundRecipeOptional.get();
                if(foundRecipe.resultType() != ResultTypes.POTION){return InteractionResult.FAIL;}
                boolean worked = handleFillFluidInBottle(player,level,blockPos,blockState,hand,itemStackInHand, (ModRecipe<ItemStack>) foundRecipe,blockEntity);
                return worked ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            }


        }
        return InteractionResult.SUCCESS;
    }
    public boolean isFireBelow(Level level, BlockPos blockPos){
        return level.getBlockState(blockPos.below()).getBlock() == Blocks.FIRE;
    }
    private boolean handleFillFluidInBottle(Player player, Level level, BlockPos blockPos, BlockState blockState, InteractionHand hand, ItemStack bottleItemStack, @NotNull ModRecipe<ItemStack> foundRecipe, KettleBlockEntity blockEntity) {
        // Decrease Bottle ItemStack Count if > 0; IF == 0 Then replace it

        if(foundRecipe.resultType() != ResultTypes.POTION){
            return false;
        }
        bottleItemStack.shrink(1);
        player.addItem(foundRecipe.result().get().copy());


        // IF Fluid level is going to be 0 , remove all ingredients from the kettle entity
        int newKettleFluidLevel = blockState.getValue(fluid_level) - 1;
        if(newKettleFluidLevel == 0){
            blockEntity.resetContent();
            this.resetToDefault(level, blockPos);
        }else{
            level.setBlock(blockPos, blockState.setValue(fluid_level,newKettleFluidLevel),3);
        }
        return true;
    }
    private void resetToDefault(Level level, BlockPos blockPos){
        // Resets the water color
        level.setBlock(blockPos, this.defaultBlockState(),3);
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
                Math.min(MAX_FLUID_LEVEL, kettleBlockState.getValue(fluid_level) + additionalFluidLevel)),3);

    }
    public static void handleIngredientFallOnKettle(ItemStack itemStack, KettleBlockEntity entity) {

        if(!IngredientAPI.isKettleIngredient(itemStack)){
            return;
        }
        ModIngredient foundMatchingIngredient = IngredientAPI.getIngredientByItem(itemStack.getItem());
        String serializedRecipe = entity.getSerializedKettleRecipe();
        String nextRecipeString = APIHelper.getNextRecipeString(serializedRecipe, foundMatchingIngredient);
        acceptIngredient(itemStack, entity, foundMatchingIngredient);
        Optional<ModRecipe<?>> recipeOptional = RecipeAPI.getRecipeBySerializedIngredients(RecipeAPI.KETTLE_RECIPES,nextRecipeString);
        if(recipeOptional.isEmpty()){
            return;
        }
        if(recipeOptional.get().resultType() == ResultTypes.ITEM){
            entity.startBrewing();
        }


    }
    private static void acceptIngredient(ItemStack itemStack, KettleBlockEntity entity, ModIngredient ingredient){
        entity.add(ingredient);
        itemStack.shrink(1);
        entity.getLevel().playSound(null, entity.getBlockPos(), SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS,0.25f,1f);
        if(!entity.getBlockState().getValue(isMixture)){
            entity.getLevel().setBlock(entity.getBlockPos(),entity.getBlockState().setValue(isMixture,true),3);
        }

    }
}
