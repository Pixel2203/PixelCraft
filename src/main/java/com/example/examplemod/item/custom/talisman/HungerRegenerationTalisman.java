package com.example.examplemod.item.custom.talisman;

import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class HungerRegenerationTalisman extends Talisman implements EffectOverTime {

    public HungerRegenerationTalisman(){
        super();
    }


    @Override
    public List<MobEffectInstance> effectsToApply() {
        return List.of(
                new MobEffectInstance(MobEffectRegistry.HUNGER_REGENERATION.get(),200,0)
        );
    }

    @Override
    public SoundEvent effectsAppliedFirstTime() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }
}
