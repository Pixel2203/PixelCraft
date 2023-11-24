package com.example.examplemod.item.custom.talisman;

import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public interface InstantaneousEffect {

    @NotNull
    SoundEvent effectAppliedSound();

}
