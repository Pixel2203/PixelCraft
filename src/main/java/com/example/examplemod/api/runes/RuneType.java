package com.example.examplemod.api.runes;

import com.example.examplemod.effect.MobEffectRegistry;
import lombok.Getter;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.util.Lazy;
@Getter
public enum RuneType {
    GLUTTONY(MobEffectRegistry.GLUTTONY::get,30 * 20, 0),
    CRYPTLIGHT(MobEffectRegistry.CRYPTLIGHT::get,60 * 20, 0),
    ANIMALFRIEND(MobEffectRegistry.ANIMALFRIEND::get,60 * 20 * 2, 0),
    HASTE(() -> MobEffects.DIG_SPEED, 60 * 20 * 3,0);

    private final Lazy<MobEffect> effect;
    private final int duration;
    private final int strength;


    RuneType(Lazy<MobEffect> effect, int duration, int strength) {
        this.effect = effect;
        this.duration = duration;
        this.strength = strength;
    }

}
