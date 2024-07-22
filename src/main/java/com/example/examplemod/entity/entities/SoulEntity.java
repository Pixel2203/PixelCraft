package com.example.examplemod.entity.entities;

import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;

public class SoulEntity extends LivingEntity {
    private static final EntityDataAccessor<Integer> SPAWNED_POS = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);

    private static int DISAPPERANCE_HEIGHT = 10;


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWNED_POS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide()) {
            if(this.entityData.get(SPAWNED_POS) == 0){
                this.entityData.set(SPAWNED_POS, this.getOnPos().getY());
            }
            this.setDeltaMovement(new Vec3(0, this.getAttributeValue(Attributes.MOVEMENT_SPEED), 0));
           // X Spread          // Z spread#
            ModUtils.sendParticles((ServerLevel) this.level(), ParticleTypes.SNOWFLAKE, this.getOnPos(), 0.2f ,1, 2, 2, 2,0);
            checkFlightDistanceReached();
        }
    }

    private void checkFlightDistanceReached() {
        int heightDifference = this.getOnPos().getY() - this.entityData.get(SPAWNED_POS);
        if(heightDifference >= DISAPPERANCE_HEIGHT) {
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt(CustomNBTTags.SPAWNED_HEIGHT, this.entityData.get(SPAWNED_POS));
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(SPAWNED_POS,nbt.getInt(CustomNBTTags.SPAWNED_HEIGHT));
    }

    public SoulEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);

    }
    public static AttributeSupplier.Builder createAttributes(){
        return LivingEntity.createLivingAttributes().add(Attributes.MOVEMENT_SPEED, 0.05D);
    }
    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }
    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }
    @Override
    public void setItemSlot(@NotNull EquipmentSlot p_21036_, @NotNull ItemStack p_21037_) {

    }
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
    public static <T extends Entity> boolean canSpawn(EntityType<T> tEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return true;
    }
    @Override
    public boolean isInvulnerable() {
        return true;
    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

}
