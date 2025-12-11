package com.example.examplemod.api.goldenChalk.rituals;

import com.example.examplemod.api.goldenChalk.rituals.util.ModRitual;
import com.example.examplemod.api.goldenChalk.rituals.util.ModRituals;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import com.example.examplemod.entity.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;

public class UndeadCleanseRitual extends ModRitual {
    public UndeadCleanseRitual(int ritualProgress) {
        super(ritualProgress);
    }

    @Override
    public int tick(ServerLevel level, BlockState blockState, BlockPos blockPos, GoldenChalkBlockEntity executingInstance) {
        isFinished = true;
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
