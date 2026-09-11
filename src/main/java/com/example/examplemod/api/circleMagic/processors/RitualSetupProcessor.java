package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.circleMagic.rituals.ModRituals;
import com.example.examplemod.api.circleMagic.CircleContext;
import com.example.examplemod.api.circleMagic.rituals.RitualFactory;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

@Slf4j
public class RitualSetupProcessor extends MagicCircleProcessor{
    @Override
    public RitualState process(CircleContext context, ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity blockEntity) {
        return Optional.ofNullable((ModRecipe<ModRituals>)context.getRecipe()).map(recipe -> {
            log.debug("RitualSetupProcessor | process | Setting up Ritual - {}" , recipe.getResult().get().name());
            context.setRitualProgress(0);
            context.setRitualHandler(RitualFactory.build(recipe.getResult().get(), 0));
            return RitualState.RITUAL_TICKING;
        }).orElseGet(() -> {
            log.error("RitualSetupProcessor | process | Ritual not found in optional, aborting");
            return cancelRitual(level, blockPos, blockEntity,context);
        });
    }
}
