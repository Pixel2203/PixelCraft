package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.circleMagic.CircleContext;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

@Slf4j
public class RitualExecutionProcessor extends MagicCircleProcessor {
    @Override
    public RitualState process(CircleContext context, ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity blockEntity) {
        log.debug("RitualExecutionProcessor | process | Ritual tick");
        var ritualHandlerOpt = Optional.ofNullable(context.getRitualHandler());

        if(ritualHandlerOpt.isEmpty()) {
            log.error("RitualExecutionProcessor | process | Unable to find RitualHandler");
            return cancelRitual(level, blockPos,blockEntity, context);
        }

        context.setRitualProgress(context.getRitualHandler().tick(level, blockState, blockPos, blockEntity));

        if(context.getRitualHandler().isFinished()){
            log.debug("RitualExecutionProcessor | process | Ritual Finished, resetting to default");
            context.getRitualHandler().onFinish(level, blockState, blockPos, blockEntity);
            return resetToDefault(blockEntity, context);
        }

        return RitualState.RITUAL_PROCESSING;
    }
}
