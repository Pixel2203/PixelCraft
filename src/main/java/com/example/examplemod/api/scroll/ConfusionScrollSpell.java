package com.example.examplemod.api.scroll;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;

public class ConfusionScrollSpell extends ScrollSpell{
    public ConfusionScrollSpell(int tickInterval, int ticks) {
        super(tickInterval, ticks);
    }
    private final int domain_size = 7;

    @Override
    public void scheduledTick(LivingEntity scrollEntity) {
        ServerLevel level = (ServerLevel) scrollEntity.level();
        AABB box = new AABB(scrollEntity.getOnPos()).inflate(domain_size,domain_size,domain_size);
        List<LivingEntity> allEntitiesInDomain = level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT,scrollEntity,box);
        List<LivingEntity> monsters = allEntitiesInDomain.stream()
                .filter(livingEntity -> livingEntity instanceof  Monster)
                .filter(livingEntity -> ((Monster) livingEntity).getTarget() instanceof Player)
                .toList();
        if(monsters.size() < 2){
            return;
        }
        for(int i = 0; i < monsters.size(); i++){
            Monster monster = (Monster) monsters.get(i);
            monster.setTarget(monsters.get(chooseRandomMonsterTarget(i,monsters.size())));
        }



    }
    private int chooseRandomMonsterTarget(int notThisValue ,int upperBound){
        Random random = new Random();
        int targetIndex = notThisValue;
        while(targetIndex == notThisValue){
            targetIndex = random.nextInt(upperBound);
        }
        return targetIndex;
    }

    @Override
    public String getSpellName() {
        return Scrolls.CONFUSION_SCROLL;
    }
}
