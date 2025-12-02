package com.example.examplemod.api.kettle;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.ModRecipes;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.block.blocks.KettleBlock;
import com.example.examplemod.blockentity.entities.KettleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class KettleBrewingLogic extends BlockEntityLogic<KettleBlockEntity> implements KettleBrewing {

    public KettleBrewingLogic(KettleBlockEntity kettleBlockEntity) {
        super(kettleBlockEntity);
    }


    public void onBrewingFinished() {
        if(this.blockEntity.getLevel().isClientSide) return;
        BlockState blockState = this.blockEntity.getBlockState();
        if(blockState.getValue(KettleBlock.fluid_level) == KettleBlock.NEEDED_FLUID_LEVEL_TO_BREW){
            Optional<ModRecipe<?>> recipeOptional = RecipeMatcher.findMatchingRecipe(RecipeOrigin.KETTLE, blockEntity.getKettleContent());
            recipeOptional.ifPresent(modRecipe -> onFinish(modRecipe));
        }
    }

    private void spawnResultOfRecipeOnKettle(@NotNull ItemStack result){
        BlockPos blockPos = blockEntity.getBlockPos();
        ServerLevel level = this.getServerLevel();
        BlockPos aboveBlock = blockPos.above();
        APIHelper.spawnItemEntity(level, aboveBlock.getCenter() ,result, Vec3.ZERO);
    }

    private void onFinish(ModRecipe<?> recipe) {
        if(recipe.getResultType() == ResultTypes.CUSTOM) {
            this.handleCustomRecipes(recipe);
        }else {
            this.spawnResultOfRecipeOnKettle((ItemStack) recipe.getResult().get());

        }

        this.blockEntity.resetKettle();
        this.playFinishEffects();
        blockEntity.setChanged();
    }

    private void handleCustomRecipes(ModRecipe<?> recipe) {
        @Nullable ItemStack result = recipe.getCrafterResult(blockEntity.getKettleContent());
        if(Objects.isNull(result)){
         failRecipe();
         return;
        }
        this.spawnResultOfRecipeOnKettle(result);
    }

    private void failRecipe() {
        blockEntity.dropContent();
        blockEntity.resetKettle();
        this.getServerLevel().playSound(null, blockEntity.getBlockPos(), SoundEvents.TOTEM_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void playFinishEffects() {
        BlockPos aboveBlock = blockEntity.getBlockPos().above();
        ServerLevel level = this.getServerLevel();
        level.sendParticles(ParticleTypes.EXPLOSION, aboveBlock.getX() + 0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ() +0.5f,0,1,1,1,1);
    }


}
