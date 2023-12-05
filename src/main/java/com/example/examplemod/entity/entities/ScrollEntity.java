package com.example.examplemod.entity.entities;

import com.example.examplemod.API.scroll.ScrollSpell;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Collections;
import java.util.Objects;


public class ScrollEntity extends LivingEntity {
    private ScrollSpell scrollSpell;
    public ScrollEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }
    @Override
    public void tick() {
        super.tick();


        if(!level().isClientSide() && Objects.nonNull(scrollSpell)){
            scrollSpell.tick(level(),blockPosition());
        }


    }
    public void setScrollEffect(ScrollSpell scrollSpell){
        this.scrollSpell = scrollSpell;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }
    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    public static AttributeSupplier.Builder createAttributes(){
        return LivingEntity.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE,10);
    }

    public static <T extends Entity> boolean canSpawn(EntityType<T> tEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return true;
    }

    @Override
    public void setNoGravity(boolean p_20243_) {
        super.setNoGravity(true);
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

}
