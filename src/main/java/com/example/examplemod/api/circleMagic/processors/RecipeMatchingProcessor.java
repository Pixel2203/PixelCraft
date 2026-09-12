package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.circleMagic.RitualContext;
import com.example.examplemod.api.circleMagic.RitualFactory;
import com.example.examplemod.api.circleMagic.RitualState;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RitualRecipe;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
@Slf4j
public class RecipeMatchingProcessor extends MagicCircleProcessor{
    @Override
    public RitualState process(RitualContext context, ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity blockEntity) {
        log.debug("RecipeMatchingProcessor | process | Evaluating Recipe...");
        Optional<RitualRecipe> recipeOptional = RecipeMatcher.matchRitualRecipe(blockEntity.getIngredients());
        if(recipeOptional.isEmpty()){
            log.debug("RecipeMatchingProcessor | process | No valid recipe found, aborting ritual");
            return cancelRitual(level,blockPos, blockEntity, context);
        }
        RitualRecipe recipe = recipeOptional.get();
        context.setRecipe(recipe);
        log.debug("RecipeMatchingProcessor | process | Evaluated Recipe to be of type {}", recipe.getResultingRitual());
        context.setRitualHandler(RitualFactory.build(recipe.getResultingRitual(), context.getRitualProgress()));
        return RitualState.RITUAL_PROCESSING;
    }
}
