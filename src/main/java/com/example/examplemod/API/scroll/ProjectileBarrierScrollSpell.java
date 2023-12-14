package com.example.examplemod.API.scroll;

import com.example.examplemod.API.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Objects;

public class ProjectileBarrierScrollSpell extends ScrollSpell{

    public ProjectileBarrierScrollSpell(int tickInterval, int ticks) {
        super(tickInterval,ticks);
    }
    private final int barrierSize = 5;

    @Override
    public void scheduledTick(LivingEntity scrollEntity) {
        ServerLevel level = (ServerLevel) scrollEntity.level();
        BlockPos entityPos = scrollEntity.getOnPos();
        AABB box = new AABB(scrollEntity.getOnPos()).inflate(barrierSize,barrierSize,barrierSize);
        List<Projectile> foundProjectileEntities = level.getEntitiesOfClass(Projectile.class,box);
        foundProjectileEntities.forEach(projectile -> {
            BlockPos projectileHitPos = projectile.getOnPos();
            Entity projectileOwner = projectile.getOwner();
            if(Objects.isNull(projectileOwner)){
                return;
            }
            BlockPos ownerPos = projectileOwner.getOnPos();
            if(ModUtils.calculateDistanceBetweenBlockPos(ownerPos, entityPos) < barrierSize - 0.2){
                return;
            }
            projectile.kill();
            level.playSound(null, projectileHitPos, SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL,1f,1f);
        });
    }

    @Override
    public String getSpellName() {
        return Scrolls.PROJECTILE_NULLIFIER_SCROLL;
    }



}
