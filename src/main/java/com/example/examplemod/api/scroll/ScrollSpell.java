package com.example.examplemod.api.scroll;

import com.example.examplemod.api.nbt.CustomNBTTags;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class ScrollSpell {

    @Setter
    private int ticks = 0;

    public void tick(LivingEntity scrollEntity){
        if(scrollEntity.level().isClientSide) return;
        ticks++;
        if(ticks < this.getTickInterval()) return;
        ticks = 0;
        scheduledTick(scrollEntity);
    }
    public abstract void scheduledTick(LivingEntity scrollEntity);
    public abstract String getSpellName();
    public abstract int getTickInterval();



}
