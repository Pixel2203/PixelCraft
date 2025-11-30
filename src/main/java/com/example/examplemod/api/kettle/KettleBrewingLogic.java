package com.example.examplemod.api.kettle;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.block.blocks.KettleBlock;
import com.example.examplemod.blockentity.entities.KettleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class KettleBrewingLogic extends BlockEntityLogic<KettleBlockEntity> implements KettleBrewing {

    public KettleBrewingLogic(KettleBlockEntity kettleBlockEntity) {
        super(kettleBlockEntity);
    }


    public void onBrewingFinished() {
        if(this.blockEntity.getLevel().isClientSide) return;
        BlockState blockState = this.blockEntity.getBlockState();
        if(blockState.getValue(KettleBlock.fluid_level) == KettleBlock.NEEDED_FLUID_LEVEL_TO_BREW){
            Optional<ModRecipe<?>> recipeOptional = RecipeMatcher.findMatchingRecipe(RecipeOrigin.KETTLE, blockEntity.getKettleContent());
            recipeOptional.ifPresent(modRecipe -> onFinish((ModRecipe<ItemStack>) modRecipe));
        }
    }

    private void spawnResultOfRecipeOnKettle(@NotNull ModRecipe<ItemStack> recipe){
        BlockPos blockPos = blockEntity.getBlockPos();
        ServerLevel level = this.getServerLevel();
        BlockPos aboveBlock = blockPos.above();
        APIHelper.spawnItemEntity(level, aboveBlock.getCenter() ,recipe.getResult().get(), Vec3.ZERO);
    }

    private void onFinish(ModRecipe<ItemStack> recipe) {
        this.spawnResultOfRecipeOnKettle(recipe);
        this.blockEntity.resetKettle();
        this.playFinishEffects();
        blockEntity.setChanged();
    }

    private void playFinishEffects() {
        BlockPos aboveBlock = blockEntity.getBlockPos().above();
        ServerLevel level = this.getServerLevel();
        level.sendParticles(ParticleTypes.EXPLOSION, aboveBlock.getX() + 0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ() +0.5f,0,1,1,1,1);
    }


}
