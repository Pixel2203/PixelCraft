package com.example.examplemod.API.scroll;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class HealingScrollSpell extends ScrollSpell{
    private final int range = 5;

    public HealingScrollSpell(int tickInterval, int ticks) {
        super(tickInterval,ticks);
    }

    @Override
    public void scheduledTick(LivingEntity scrollEntity) {
        AABB box = new AABB(scrollEntity.getOnPos()).inflate(range,range,range);
        List<LivingEntity> foundEntities = scrollEntity.level().getEntitiesOfClass(LivingEntity.class,box);

        for(LivingEntity entity : foundEntities){
            if(entity.isDeadOrDying()){
                continue;
            }
            entity.setHealth(Math.min(entity.getHealth() + 1, entity.getMaxHealth()));
        }
    }

    @Override
    public String getSpellName() {
        return Scrolls.HEALING_SCROLL;
    }

}
