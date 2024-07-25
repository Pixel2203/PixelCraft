package com.example.examplemod.entity.entities;

import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.config.entity.SoulConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class SoulEntity extends LivingEntity {
    private static final EntityDataAccessor<Integer> SPAWNED_POS = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.FLOAT);

    private static final Logger log = LoggerFactory.getLogger(SoulEntity.class);




    private float highestEntityEnergy;
    private boolean alreadyGenerated = false;
    private Integer nextRandomTick;
    private Integer ticker;
    private final Random random = new Random();
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWNED_POS, 0);
        this.entityData.define(ENERGY,0f);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(!alreadyGenerated) {
            Random random = new Random();
            highestEntityEnergy = random.nextFloat(1f);
            entityData.set(ENERGY, highestEntityEnergy);
            entityData.set(SPAWNED_POS, this.getOnPos().getY());
            alreadyGenerated = true;
        }
    }


    public float summonSoulParticles(ServerLevel serverLevel) {
        float currentEnergy = this.entityData.get(ENERGY);
        float retrieved = Math.min(random.nextFloat(currentEnergy), currentEnergy / 10);
        float newEnergyLevel = currentEnergy - retrieved;
        this.entityData.set(ENERGY, newEnergyLevel);
        if(newEnergyLevel <= 0){
            this.discard();
        }
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        areaeffectcloud.setRadius(retrieved);
        //areaeffectcloud.setRadiusOnUse(-0.5f);
        areaeffectcloud.setDuration(SoulConfig.SOUL_PARTICLE_LIFESPAN);
        areaeffectcloud.setOwner(this);
        serverLevel.addFreshEntity(areaeffectcloud);
        return retrieved;
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide()) {
            this.setDeltaMovement(new Vec3(0, this.getAttributeValue(Attributes.MOVEMENT_SPEED), 0));
            float probability = this.entityData.get(ENERGY) / highestEntityEnergy;
            ModUtils.sendParticles((ServerLevel) this.level(), ParticleTypes.SNOWFLAKE, this.getOnPos(), probability ,1, 2, 2, 2,0);
            checkFlightDistanceReached();
            checkRandomTick();
        }
    }

    private void checkRandomTick() {
        if(Objects.isNull(ticker)){
            ticker = 0;
            nextRandomTick = random.nextInt(SoulConfig.MINIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN , SoulConfig.MAXIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN);
            return;
        }
        if(ticker >= nextRandomTick){
            ticker = 0;
            nextRandomTick = random.nextInt(SoulConfig.MINIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN , SoulConfig.MAXIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN);
            this.onRandomTick((ServerLevel) level());
        }
        ticker++;
    }

    private void onRandomTick(ServerLevel serverLevel) {
        summonSoulParticles(serverLevel);
    }

    private void checkFlightDistanceReached() {
        int heightDifference = this.getOnPos().getY() - this.entityData.get(SPAWNED_POS);
        if(heightDifference >= SoulConfig.SOUL_DISAPPEARANCE_HEIGHT) {
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt(CustomNBTTags.SPAWNED_HEIGHT, this.entityData.get(SPAWNED_POS));
        nbt.putFloat(CustomNBTTags.ENERGY_CHARGE, this.entityData.get(ENERGY));
        nbt.putBoolean(CustomNBTTags.ALREADY_GENERATED, alreadyGenerated);
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(SPAWNED_POS,nbt.getInt(CustomNBTTags.SPAWNED_HEIGHT));
        this.entityData.set(ENERGY,nbt.getFloat(CustomNBTTags.ENERGY_CHARGE));
        this.alreadyGenerated = nbt.getBoolean(CustomNBTTags.ALREADY_GENERATED);
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
