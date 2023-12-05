package com.example.examplemod.item.items.talisman;

import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EffectOverTime {

    @NotNull
    List<MobEffectInstance> effectsToApply();
}
