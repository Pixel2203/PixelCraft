package com.example.examplemod.effect.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class AnimalFriend extends MobEffect {
    public AnimalFriend(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if(living.level().isClientSide) return;
        if(!(living instanceof Player p)) return;
        AABB bounds = living.getBoundingBox().inflate(8 + amplifier * 3);
        ServerLevel level = (ServerLevel) living.level();
        List<Animal> entities = getAttractableAnimals(level, p, bounds);
        for(Animal e : entities) {
            if(e.isInLove()) continue;
            charm(e,p);
        }

    }

    private void charm(Animal animal, Player p) {

        double speed = 1.0D;
        double distance = animal.distanceTo(p);

        // Minimaler Abstand: 2 Blöcke
        double stopDistance = 2.0D;

        if (distance > stopDistance) {
            animal.getNavigation().moveTo(p, speed);
        } else {
            animal.getNavigation().stop(); // Stehen bleiben, wenn nah genug
        }
    }

    private List<Animal> getAttractableAnimals(ServerLevel level, Player player, AABB area) {
        return level.getNearbyEntities(
                        LivingEntity.class,
                        TargetingConditions.DEFAULT,
                        player,
                        area
                ).stream()
                .filter(e -> e instanceof Animal)
                .map(e -> (Animal) e)
                .toList();
    }


}
