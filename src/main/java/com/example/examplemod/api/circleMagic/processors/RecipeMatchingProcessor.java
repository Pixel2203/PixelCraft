package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.circleMagic.CircleContext;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
@Slf4j
public class RecipeMatchingProcessor extends MagicCircleProcessor{
    @Override
    public RitualState process(CircleContext context, ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity blockEntity) {
        log.debug("RecipeMatchingProcessor | process | Evaluating Recipe...");
        Optional<ModRecipe<?>> recipeOptional = RecipeMatcher.findMatchingRecipe(RecipeOrigin.CHALK,blockEntity.getIngredients());
        if(recipeOptional.isEmpty()){
            log.debug("RecipeMatchingProcessor | process | No valid recipe found, aborting ritual");
            return cancelRitual(level,blockPos, blockEntity, context);
        }
        ModRecipe<?> recipe = recipeOptional.get();
        context.setRecipe(recipe);
        log.debug("RecipeMatchingProcessor | process | Evaluated Recipe to be of type {}", recipe.getResultType());
        return switch (recipe.getResultType()){
            case ITEM -> RitualState.SPAWN_ITEM;
            case RITUAL -> RitualState.RITUAL_SETUP;
            default -> cancelRitual(level, blockPos, blockEntity, context);
        };


    }
}
