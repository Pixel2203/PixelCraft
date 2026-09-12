package com.example.examplemod.api.circleMagic.rituals;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.circleMagic.ModRituals;
import com.example.examplemod.api.circleMagic.Ritual;
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
public class SimpleItemSpawnRitual extends Ritual {
    private final ModRituals uniqueRitualValue;
    private final ItemStack itemStackToSpawn;
    public SimpleItemSpawnRitual(int ritualProgress, ModRituals uniqueRitualValue, ItemStack itemStackToSpawn) {
        super(ritualProgress);
        this.uniqueRitualValue = uniqueRitualValue;
        this.itemStackToSpawn = itemStackToSpawn;
    }

    @Override
    public int tick(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance) {
        this.finish();
        return 0;
    }

    @Override
    public void onFinish(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance) {
        log.debug("SimpleItemSpawnRitual | onFinish | Spawning item {}", this.itemStackToSpawn.getItem());
        Vec3 itemSpawnPosition = blockPos.above().getCenter();
        APIHelper.spawnItemEntity(level, itemSpawnPosition, itemStackToSpawn.copy(), Vec3.ZERO);
        ModUtils.sendParticles(level, ParticleTypes.EXPLOSION, itemSpawnPosition, 1, 1, 1, 1, 1, 1);
        level.playSound(null, blockPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.25f, 1f);
    }

    @Override
    public ModRituals getType() {
        return this.uniqueRitualValue;
    }
}
