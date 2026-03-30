package com.example.examplemod.item.items.talisman;

import com.example.examplemod.effect.MobEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UndeadProtectionTalisman extends TalismanItem implements EffectOverTime{
    private final MobEffectInstance undeadProtectionEffect = new MobEffectInstance(MobEffectRegistry.CRYPTLIGHT.get(),-1,0);
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
        return List.of(undeadProtectionEffect);
    }
}
