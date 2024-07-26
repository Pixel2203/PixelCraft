package com.example.examplemod.entity.entities;

import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.scroll.ScrollSpell;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.entities.generalEntities.GeneralScrollEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;


public class ScrollEntity extends GeneralScrollEntity {
    private ScrollSpell scrollSpell;

    protected static EntityDataAccessor<Integer> DATA_CURRENT_TICK = SynchedEntityData.defineId(ScrollEntity.class,EntityDataSerializers.INT);
    public ScrollEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }
    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide() && Objects.nonNull(scrollSpell)){
            scrollSpell.tick(this);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        CompoundTag scrollData = new CompoundTag();
        if(Objects.isNull(scrollSpell)){
            return;
        }
        scrollData.putString(CustomNBTTags.SCROLL_NAME, this.scrollSpell.getSpellName());
        scrollData.putInt(CustomNBTTags.TICKER, this.entityData.get(DATA_CURRENT_TICK));
        scrollData.putInt(CustomNBTTags.TICK_INTERVAL, this.scrollSpell.getTickInterval());
        nbt.put(ExampleMod.MODID,scrollData);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        CompoundTag scrollData = nbt.getCompound(ExampleMod.MODID);
        if(scrollData.isEmpty()){
            return;
        }
        ScrollSpell spell = ScrollSpell.getSpellByCompoundData(scrollData);
        if(Objects.isNull(spell)){
            return;
        }
        this.scrollSpell = spell;
        this.entityData.set(DATA_CURRENT_TICK,scrollData.getInt(CustomNBTTags.TICKER));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CURRENT_TICK, 0);
    }


    public void setScrollEffect(ScrollSpell scrollSpell){
        this.scrollSpell = scrollSpell;
    }

}
