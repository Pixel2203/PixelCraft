package com.example.examplemod.api.circleMagic.rituals;

import com.example.examplemod.api.circleMagic.ModRituals;
import com.example.examplemod.api.circleMagic.Ritual;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class UndeadCleanseRitual extends Ritual {
    public UndeadCleanseRitual(int ritualProgress) {
        super(ritualProgress);
    }

    @Override
    public int tick(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance) {
        finish();
        return this.ritualProgress;
    }

    @Override
    public void onFinish(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance) {
        AABB box = new AABB(executingInstance.getBlockPos()).inflate(4);
        level.getEntitiesOfClass(Zombie.class, box).forEach(zombie -> {
            Villager villager = EntityType.VILLAGER.create(level);
            if(villager!=null){
                villager.moveTo(zombie.position());
                zombie.remove(Entity.RemovalReason.DISCARDED);
                level.addFreshEntity(villager);
            }

        });

    }

    @Override
    public ModRituals getType() {
        return ModRituals.UNDEAD_CLEANSE;
    }
}
