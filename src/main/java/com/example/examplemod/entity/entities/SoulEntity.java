package com.example.examplemod.entity.entities;

import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.networking.NetworkMessages;
import com.example.examplemod.networking.packets.CustomParticlePackage;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Random;

public class SoulEntity extends LivingEntity {
    private static final EntityDataAccessor<Integer> SPAWNED_POS = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.FLOAT);
    //private static final EntityDataAccessor<Boolean> ALREADY_GENERATED_VALUES = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.BOOLEAN);
    private static final Logger log = LoggerFactory.getLogger(SoulEntity.class);
    private static final int DISAPPEARANCE_HEIGHT = 10;
    private static float MAX_CHARGE = 0.5f;
    private float highestEntityEnergy;
    private boolean alreadyGenerated = false;
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
            highestEntityEnergy = random.nextFloat(MAX_CHARGE);
            entityData.set(ENERGY, highestEntityEnergy);
            entityData.set(SPAWNED_POS, this.getOnPos().getY());
            alreadyGenerated = true;
        }
    }

    public float retrieveEnergyFromSoul(BlockPos playerPos) {
        float currentEnergy = this.entityData.get(ENERGY);
        Random random = new Random();
        float retrieved = Math.max(random.nextFloat(currentEnergy),MAX_CHARGE / 10);
        float newEnergyLevel = currentEnergy - retrieved;
        this.entityData.set(ENERGY, newEnergyLevel);
        if(newEnergyLevel <= 0){
            log.warn("Du hast eine grenze Überschritten!");
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level(), playerPos, MobSpawnType.MOB_SUMMONED);
            this.discard();
        }

        return retrieved;
    }
    public boolean renderToPlayer(LocalPlayer player) {
        return player.getInventory()
                .getArmor(ModUtils.ArmorSlots.HELMET)
                .is(Items.IRON_HELMET);
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
            sendParticlesToPlayers((ServerLevel) level(), probability);
            checkFlightDistanceReached();
        }
    }

    private void sendParticlesToPlayers(ServerLevel serverLevel, float probability) {
        //ModUtils.sendParticles((ServerLevel) this.level(), ParticleTypes.SNOWFLAKE, this.getOnPos(), probability ,1, 2, 2, 2,0);
        var players = serverLevel.getPlayers(serverPlayer -> true);

        for(ServerPlayer player : players) {
            CustomParticlePackage particlePackage = new CustomParticlePackage();
            NetworkMessages.sendToClient(new CustomParticlePackage(), player);
        }

    }

    private void checkFlightDistanceReached() {
        int heightDifference = this.getOnPos().getY() - this.entityData.get(SPAWNED_POS);
        if(heightDifference >= DISAPPEARANCE_HEIGHT) {
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
