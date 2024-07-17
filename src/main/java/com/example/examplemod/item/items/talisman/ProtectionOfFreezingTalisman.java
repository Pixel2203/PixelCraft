package com.example.examplemod.item.items.talisman;

import com.example.examplemod.effect.MobEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class  ProtectionOfFreezingTalisman extends TalismanItem implements EffectOverTime {
    @Override
    protected SoundEvent getEquipSound() {
        return null;
    }

    @Override
    protected SoundEvent getUnEquipSound() {
        return null;
    }

    @Override
    public @NotNull List<MobEffectInstance> effectsToApply() {
        return List.of(new MobEffectInstance(MobEffectRegistry.FREEZING_PROTECTION.get(),-1,0));
    }
}
