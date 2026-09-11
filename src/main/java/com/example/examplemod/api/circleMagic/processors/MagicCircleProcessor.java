package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.circleMagic.CircleContext;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

@Slf4j
public abstract class MagicCircleProcessor {


    /**
     *
     * @param level ServerLevel of the Server
     * @param blockState BlockState of the GoldenChalkBlock
     * @param blockPos BlockPos of the GoldenChalkBlock
     * @param blockEntity BlockEntity of the GoldenChalkBlock
     * @return Returns a boolean to decide if the process is finished `true` or if it still needs processing time `false`
     */
    public abstract RitualState process(CircleContext context, ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity blockEntity);



    protected RitualState cancelRitual(ServerLevel level, BlockPos blockPos, GoldenChalkBlockEntity blockEntity, CircleContext context) {
        if(Objects.isNull(level)){
            log.error("MagicCircleProcessor | cancelRitual | Unable to find level");
            return RitualState.FREE;
        }
        level.playSound(null, blockPos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS,0.25f,1f);
        dropContent(level, blockPos, blockEntity);
        return resetToDefault(blockEntity, context);

    }

    protected void dropContent(ServerLevel level, BlockPos blockPos, GoldenChalkBlockEntity blockEntity) {
        Vec3 itemSpawnPosition = blockPos.above().getCenter();
        blockEntity.getIngredients().forEach(itemStack -> APIHelper.spawnItemEntity(level,itemSpawnPosition,itemStack,Vec3.ZERO));
        blockEntity.getIngredients().clear();
    }

    protected RitualState resetToDefault(GoldenChalkBlockEntity blockEntity, CircleContext context) {
        context.setRitualHandler(null);
        context.setRitualProgress(0);
        blockEntity.getIngredients().clear();
        blockEntity.setTicker(0);
        return RitualState.FREE;
    }


}
