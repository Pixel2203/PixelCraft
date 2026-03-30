package com.example.examplemod.api.scroll;

import com.example.examplemod.api.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class ProjectileBarrierScrollSpell extends ScrollSpell {

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
            Vec3 projectilePosition = projectile.position();
            projectile.kill();

            level.playSound(null, projectileHitPos, SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL,1f,1f);
            level.sendParticles(ParticleTypes.FIREWORK,
                    projectilePosition.x, projectilePosition.y, projectilePosition.z,
                    6,    // count: mehr Partikel für besseren Effekt
                    0.1, 0.1, 0.1, // kleine Streuung um den Punkt
                    0.05   // speed: fast null, damit sie nicht wegfliegen
            );
        });
    }

    @Override
    public String getSpellName() {
        return Scrolls.PROJECTILE_NULLIFIER_SCROLL;
    }

    @Override
    public int getTickInterval() {
        return 0;
    }


}
