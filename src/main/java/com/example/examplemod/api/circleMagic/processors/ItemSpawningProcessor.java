package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.circleMagic.CircleContext;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

@Slf4j
public class ItemSpawningProcessor extends MagicCircleProcessor{
    @Override
    public RitualState process(CircleContext context, ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity blockEntity) {
        log.debug("ItemSpawningProcessor | process | Spawning item...");
        return Optional.ofNullable((ModRecipe<ItemStack>) context.getRecipe())
                .map(recipe -> {
                    Vec3 itemSpawnPosition = blockPos.above().getCenter();
                    APIHelper.spawnItemEntity(level, itemSpawnPosition, recipe.getResult().get(), Vec3.ZERO);
                    ModUtils.sendParticles(level, ParticleTypes.EXPLOSION, itemSpawnPosition, 1, 1, 1, 1, 1, 1);
                    level.playSound(null, blockPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.25f, 1f);
                    log.debug("ItemSpawningProcessor | process | Successfully spawned item");
                    return resetToDefault(blockEntity, context);
                })
                .orElseGet(() -> {
                    log.error("ItemSpawningProcessor | process | Recipe not found when executing spawn item");
                    return cancelRitual(level, blockPos, blockEntity, context); // Return für den Fehlerfall
                });

    }
}
