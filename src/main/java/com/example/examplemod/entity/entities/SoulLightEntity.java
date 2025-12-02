package com.example.examplemod.entity.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.entity.entities.generalEntities.UntouchableEntity;
import joptsimple.internal.Strings;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.StringUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SoulLightEntity extends UntouchableEntity {

    public static EntityDataAccessor<String> ownerDataAccessor = SynchedEntityData.defineId(SoulLightEntity.class, EntityDataSerializers.STRING);

    public static AttributeSupplier.Builder createAttributes() {
        // Nutzen Sie LivingEntity.createLivingAttributes() als Basis, da Movement Speed ein LivingEntity-Attribut ist.
        return LivingEntity.createLivingAttributes()
                // Bewegung: Wir wollen langsame, kontrollierte Gleitbewegung
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                // Generelle Attribute, die Mob/LivingEntity benötigen (auch wenn nicht genutzt)
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ownerDataAccessor, Strings.EMPTY);
    }

    private LivingEntity owner;
    private final double idleRange = 4;

    private boolean isCatchingUp = false;
    private Vec3 desiredPosition;

    private BlockPos myLastBlockPos;

    private final int MAX_LIFESPAN = 20 * 60 ;
    private int ticksToLive = MAX_LIFESPAN;
    public SoulLightEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide) return;
        if(owner == null && !StringUtil.isNullOrEmpty(this.entityData.get(ownerDataAccessor))) {
            this.owner = this.level().getPlayerByUUID(UUID.fromString(this.entityData.get(ownerDataAccessor)));
        }
        this.handleLifSpan();
        this.handleLighting();

        if(this.owner == null) {
            return;
        }
        this.followOwner();


    }

    private void handleLifSpan() {
        this.ticksToLive--;
        if(this.ticksToLive <= 0) {
            this.hurt(this.damageSources().magic(), this.getHealth());
        }
    }

    @Override
    public void onRemovedFromWorld() {
        if(Objects.nonNull(this.myLastBlockPos)) {
            var b = this.level().getBlockState(this.myLastBlockPos);
            if(b.is(BlockRegistry.InvisibleLightBlock.get())) {
                level().setBlockAndUpdate(this.myLastBlockPos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.onRemovedFromWorld();
    }

    private void handleLighting() {
        if(myLastBlockPos == null) {
            myLastBlockPos = this.blockPosition();
            return;
        }
        if(this.blockPosition() == this.myLastBlockPos) return;
        boolean canPlaceNewLight = level().getBlockState(this.blockPosition()).is(Blocks.AIR);
        BlockState lastBlockState = level().getBlockState(myLastBlockPos);
        if(lastBlockState.is(BlockRegistry.InvisibleLightBlock.get())) {
            if(canPlaceNewLight || myLastBlockPos.getCenter().distanceTo(position()) > 15) {
                level().setBlockAndUpdate(this.myLastBlockPos, Blocks.AIR.defaultBlockState());
            }
        }
        if(canPlaceNewLight) {
            level().setBlockAndUpdate(this.blockPosition(), BlockRegistry.InvisibleLightBlock.get().defaultBlockState());
        }
        this.myLastBlockPos = this.blockPosition();

    }

    public void followOwner() {
        double distance = this.position().distanceTo(this.owner.position());

        if(distance > this.idleRange) {
            this.isCatchingUp = true;
            this.desiredPosition = owner.position().add(0,2,0);
        }else {
            if(this.isCatchingUp)  {
                this.isCatchingUp = false;
                this.desiredPosition = calculateRandomPositionAroundOwner();
            }
        }
        if(this.desiredPosition != null) {
            this.setDeltaMovement(calculateMovement());
        }

    }

    private Vec3 calculateMovement() {
        double distance = this.position().distanceTo(desiredPosition);
        if(distance <= 0.2f) return Vec3.ZERO;
        return this.position().vectorTo(desiredPosition).normalize().scale(this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
    }

    private Vec3 calculateRandomPositionAroundOwner() {
        if (this.owner == null) return this.position(); // Fallback

        Vec3 playerPos = this.owner.position().add(0,3,0);

        // Zufälliger Winkel
        double angle = this.random.nextDouble() * Math.PI * 2; // 0 - 2π
        // Zufällige Distanz im Kreis (gleichmäßig verteilt)
        double distance = this.random.nextDouble() * idleRange;

        // X/Z Koordinaten berechnen
        double offsetX = Math.cos(angle) * distance;
        double offsetY = random.nextDouble() - 0.5;
        double offsetZ = Math.sin(angle) * distance;

        // Y kann gleich bleiben wie Spieler oder leicht variieren (z.B. ±0.5)

        return playerPos.add(offsetX, offsetY, offsetZ);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        CompoundTag modCompound = compoundTag.getCompound(ExampleMod.MODID);
        String ownerUUID = modCompound.getString("Owner");
        if(!StringUtil.isNullOrEmpty(ownerUUID)) {
            this.entityData.set(ownerDataAccessor, ownerUUID);
        }

        this.myLastBlockPos = Optional.of(ModUtils.blockPosFromTag(modCompound.getCompound("MyLastBlockPos"))).orElse(this.blockPosition());
        this.desiredPosition = Optional.ofNullable(ModUtils.vec3FromTag(modCompound.getCompound("DesiredPosition"))).orElse(this.position());
        this.isCatchingUp = Optional.of(modCompound.getBoolean("CatchingUp")).orElse(false);
        this.ticksToLive = modCompound.getInt("TicksToLive");

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        CompoundTag modCompound = new CompoundTag();
        if(Objects.nonNull(this.owner)) {
            modCompound.putString("Owner", this.entityData.get(ownerDataAccessor));
            modCompound.putBoolean("CatchingUp", this.isCatchingUp);
        }

        if(Objects.nonNull(this.desiredPosition)) {
            CompoundTag tag = ModUtils.vec3ToTag(this.desiredPosition);
            modCompound.put("DesiredPosition", tag);
        }

        if(Objects.nonNull(this.myLastBlockPos)) {
            CompoundTag tag = ModUtils.vec3ToTag(this.myLastBlockPos);
            modCompound.put("MyLastBlockPos", tag);
        }
        modCompound.putInt("TicksToLive", this.ticksToLive);

        compoundTag.put(ExampleMod.MODID, modCompound);
    }

    @Override
    public boolean isNoGravity() {
        return true;

    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.entityData.set(ownerDataAccessor, owner.getStringUUID());
    }
}
