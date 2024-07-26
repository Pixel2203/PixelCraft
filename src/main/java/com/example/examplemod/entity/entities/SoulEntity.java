package com.example.examplemod.entity.entities;

import com.example.examplemod.api.BlockInfo;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.config.entity.SoulConfig;
import com.example.examplemod.entity.entities.generalEntities.GeneralSoulEntity;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SoulEntity extends GeneralSoulEntity {
    private static final EntityDataAccessor<Integer> SPAWNED_POS = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.FLOAT);



    private static final Logger log = LoggerFactory.getLogger(SoulEntity.class);
    @Nullable
    private Float highestEntityEnergy;
    private boolean alreadyGenerated = false;
    private Integer nextRandomTick;
    private Integer ticker;
    private final Random random = new Random();




    public SoulEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWNED_POS, 0);
        this.entityData.define(ENERGY,0f);
    }

    private float getHighestEntityEnergy() {
        if(Objects.isNull(highestEntityEnergy)){
            this.highestEntityEnergy = random.nextFloat(SoulConfig.DEFAULT_SOUL_ENTITY_ENERGY);
            return highestEntityEnergy;
        }
        return highestEntityEnergy;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(!alreadyGenerated) {
            entityData.set(ENERGY, getHighestEntityEnergy());
            entityData.set(SPAWNED_POS, this.getOnPos().getY());
            alreadyGenerated = true;
        }
    }

    public void summonSoulParticles(ServerLevel serverLevel) {
        float currentEnergy = this.entityData.get(ENERGY);
        float retrieved= random.nextFloat(currentEnergy);
        if(retrieved < 0.1f){
            retrieved = Math.min(currentEnergy, getHighestEntityEnergy() / 10);
        }
        float newEnergyLevel = currentEnergy - retrieved;
        this.entityData.set(ENERGY, newEnergyLevel);
        if(newEnergyLevel <= 0){
            this.discard();
        }


        // Get Spawn Position
        AABB boundingBox = this.getBoundingBox().inflate(3);
        List<BlockInfo> airBlocks = ModUtils.getBlocksInBoundingBox(serverLevel, boundingBox).stream()
                .filter(blockInfo -> blockInfo.blockState().isAir()).toList();


        if(airBlocks.isEmpty()){
            // Can not spawn particles becuase no air block is nearby
            return;
        }
        BlockPos spawnPos = airBlocks.get(random.nextInt(airBlocks.size())).blockPos();
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        areaeffectcloud.setRadius(retrieved);
        areaeffectcloud.setDuration(SoulConfig.SOUL_PARTICLE_LIFESPAN);
        areaeffectcloud.setOwner(this);
        serverLevel.addFreshEntity(areaeffectcloud);
    }
    public boolean renderToPlayer(LocalPlayer player) {
        return player.getInventory()
                .getArmor(ModUtils.ArmorSlots.HELMET)
                .is(Items.IRON_HELMET);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide()) {
            this.setDeltaMovement(new Vec3(0, this.getAttributeValue(Attributes.MOVEMENT_SPEED), 0));
            float probability = this.entityData.get(ENERGY) / getHighestEntityEnergy();
            sendParticlesToPlayers((ServerLevel) level(), probability);
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

    public void setEnergy(float energy) {
        this.highestEntityEnergy = energy;
    }



}
