package com.example.examplemod.item.items.talisman;

import com.example.examplemod.item.items.talisman.EffectOverTime;
import com.example.examplemod.item.items.talisman.TalismanItem;
import com.example.examplemod.registry.MobEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class HungerRegenerationTalisman extends TalismanItem implements EffectOverTime {

    public HungerRegenerationTalisman(){
        super();
    }


    @Override
    public List<MobEffectInstance> effectsToApply() {
        return List.of(
                new MobEffectInstance(MobEffectRegistry.HUNGER_REGENERATION.get(),-1,0)
        );
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }
    @Override
    public SoundEvent getUnEquipSound() {
        return null;
    }
}
