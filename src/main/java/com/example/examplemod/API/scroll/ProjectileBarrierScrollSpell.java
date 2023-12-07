package com.example.examplemod.API.scroll;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ProjectileBarrierScrollSpell extends ScrollSpell{

    public ProjectileBarrierScrollSpell(int tickInterval) {
        super(tickInterval);
    }

    @Override
    public void scheduledTick(ServerLevel level, BlockPos blockPos) {
        AABB box = new AABB(blockPos).inflate(5,5,5);
        List<Projectile> foundEntities = level.getEntitiesOfClass(Projectile.class,box);
        foundEntities.forEach(projectile -> {
            BlockPos projectileHitPos = projectile.getOnPos();
            projectile.kill();
            level.playSound(null, projectileHitPos, SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL,1f,1f);
        });
    }
}
