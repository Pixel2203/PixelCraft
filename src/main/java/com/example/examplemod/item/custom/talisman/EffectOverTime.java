package com.example.examplemod.item.custom.talisman;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EffectOverTime {

    @NotNull
    List<MobEffectInstance> effectsToApply();

    @NotNull
    SoundEvent effectsAppliedFirstTime();
}
