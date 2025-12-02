package com.example.examplemod.api.kettle;

import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.block.blocks.KettleBlock;
import com.example.examplemod.blockentity.entities.KettleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class KettleInteractionLogic extends BlockEntityLogic<KettleBlockEntity> implements KettleInteraction{
    public KettleInteractionLogic(KettleBlockEntity kettleBlockEntity) {
        super(kettleBlockEntity);
    }

    public void fallOn(Entity entity) {
        if(this.blockEntity.isBrewing()) return;
        if(!blockEntity.getKettleBlock().isFireBelow(this.getServerLevel(), blockEntity.getBlockPos())) return;
        if(blockEntity.getBlockState().getValue(KettleBlock.fluid_level) < KettleBlock.NEEDED_FLUID_LEVEL_TO_BREW) return;
        if(!(entity instanceof ItemEntity itemEntity)) return;

        ItemStack fellItem = itemEntity.getItem();
        if (!ModUtils.isIngredient(fellItem)) return;
        handleIngredientFallOnKettle(fellItem);
    }

    /**
     * This method is being called, when the kettle is being right clicked with an empty glass bottle
     */
    public InteractionResult onBottle(ServerPlayer player){

        Optional<ModRecipe<?>> searchResult = blockEntity.getRecipe();
        if(searchResult.isEmpty()) return InteractionResult.FAIL;
        ModRecipe<?> foundRecipe = searchResult.get();
        if(foundRecipe.getResultType() != ResultTypes.POTION){return InteractionResult.FAIL;}
        return bottleContent(player, (ModRecipe<ItemStack>) foundRecipe);
    }


    private void handleIngredientFallOnKettle(ItemStack itemStack) {
        acceptIngredient(itemStack);
        Optional<ModRecipe<?>> recipeOptional = RecipeMatcher.findMatchingRecipe(RecipeOrigin.KETTLE, blockEntity.getKettleContent());
        if(recipeOptional.isEmpty()) return;
        blockEntity.startBrewing();
    }

    /**
     * Adds the given ItemStack to the ingredient list without further checks.
     * @param itemStack - ItemStack which will be consumed and therefore be shrunk by 1
     */
    private void acceptIngredient(ItemStack itemStack){
        KettleBlock block = blockEntity.getKettleBlock();
        BlockState blockState = blockEntity.getBlockState();
        BlockPos blockPos = blockEntity.getBlockPos();
        blockEntity.add(itemStack.copy());

        itemStack.shrink(1);
        this.getServerLevel().playSound(null, blockPos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS,0.25f,1f);

        if(!block.isMixture(blockState)){
            block.makeMixture(this.getServerLevel(),blockState, blockPos);
        }

    }

    private InteractionResult bottleContent(ServerPlayer player, @NotNull ModRecipe<ItemStack> foundRecipe) {

        ItemStack emptyBottleItemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        emptyBottleItemStack.shrink(1);
        player.addItem(foundRecipe.getResult().get().copy());

        BlockState blockState = blockEntity.getBlockState();
        BlockPos blockPos = blockEntity.getBlockPos();
        KettleBlock kettleBlock = (KettleBlock) blockState.getBlock();
        kettleBlock.reduceFluidLevel(this.getServerLevel(), blockState, blockPos, 1);
        return InteractionResult.SUCCESS;
    }



}
