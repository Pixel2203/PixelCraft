package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.circleMagic.CircleContext;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

@Slf4j
public class RitualInitiatingProcessor extends MagicCircleProcessor{
    @Override
    public RitualState process(CircleContext context, ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity blockEntity) {
        log.debug("RitualInitiatingProcessor | process | Starting Circle Magic");
        blockEntity.getIngredients().clear();
        return RitualState.COLLECTING;
    }
}
