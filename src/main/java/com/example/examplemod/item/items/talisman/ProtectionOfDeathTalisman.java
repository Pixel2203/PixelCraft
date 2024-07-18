package com.example.examplemod.item.items.talisman;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.effect.MobEffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class ProtectionOfDeathTalisman extends TalismanItem implements EffectOverTime {
    private final MobEffectInstance deathProtectionEffect = new MobEffectInstance(MobEffectRegistry.GUARDIAN_ANGLE.get(),-1,0);

    @Override
    protected SoundEvent getEquipSound() {
        return SoundEvents.AMETHYST_BLOCK_FALL;
    }

    @Override
    protected SoundEvent getUnEquipSound() {
        return null;
    }



    @Override
    public @NotNull List<MobEffectInstance> effectsToApply() {
        return List.of(deathProtectionEffect);
    }

    public void triggerEffect(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        entity.setHealth(0.5f);
        event.setCanceled(true);
        APIHelper.breakCurioOfEntity(entity, ItemRegistry.PROTECTION_OF_DEATH_TALISMAN.get());
        //TODO Do not remove all effects, only the Protection of Death Effect
        removeAllAppliedEffects(entity);
    }

    @Override
    public void curioBreak(SlotContext slotContext, ItemStack stack) {
        super.curioBreak(slotContext, stack);
        LivingEntity entity = slotContext.entity();
        entity.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER);

    }
}
