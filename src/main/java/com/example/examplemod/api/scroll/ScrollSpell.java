package com.example.examplemod.api.scroll;

import com.example.examplemod.api.nbt.CustomNBTTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class ScrollSpell {
    private final int tickInterval;
    private int ticks;
    public ScrollSpell(int tickInterval, int ticks){
        this.tickInterval = tickInterval;
        this.ticks = ticks;
    }
    public void tick(LivingEntity scrollEntity){
        if(scrollEntity.level().isClientSide){
            return;
        }
        ticks++;
        if(ticks < tickInterval){
            return;
        }
        ticks = 0;
        scheduledTick(scrollEntity);
    }
    public abstract void scheduledTick(LivingEntity scrollEntity);
    public abstract String getSpellName();
    @Nullable
    public static ScrollSpell getSpellByCompoundData(CompoundTag scrollData){
        if(Objects.isNull(scrollData)){
            return null;
        }
        final String spellName = scrollData.getString(CustomNBTTags.SCROLL_NAME);
        final int tickInterval = scrollData.getInt(CustomNBTTags.TICK_INTERVAL);
        final int ticks = scrollData.getInt(CustomNBTTags.TICKER);
        return switch (spellName) {
            case Scrolls.PROJECTILE_NULLIFIER_SCROLL -> new ProjectileBarrierScrollSpell(tickInterval, ticks);
            case Scrolls.HEALING_SCROLL -> new HealingScrollSpell(tickInterval, ticks);
            case Scrolls.CONFUSION_SCROLL -> new ConfusionScrollSpell(tickInterval, ticks);
            default -> null;
        };
    }

    public int getTicks() {
        return this.ticks;
    }
    public int getTickInterval() {
        return this.tickInterval;
    }
}
