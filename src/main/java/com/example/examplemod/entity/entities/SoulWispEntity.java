package com.example.examplemod.entity.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.SoulFlower;
import com.example.examplemod.blockentity.entities.SoulFlowerBlockEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Slf4j
public class SoulWispEntity extends LivingEntity {
    public static final EntityDataAccessor<Integer> TEXTURE_INDEX = SynchedEntityData.defineId(SoulWispEntity.class, EntityDataSerializers.INT);

    private final float MOVEMENT_SPEED = 0.125f;
    private Integer wanderAroundTimeInTicks;

    @Setter
    private BlockPos targetFlowerPos; // Position der SoulFlower
    private int textureIndex;


    private Vec3 desiredPosition;
    private int movementTicker;
    private int wanderTicker;
    private int ticksToReachPosition;
    private Vec3 movementVector;

    public SoulWispEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
        if(p_20967_.isClientSide()) return;
        int id = this.level().random.nextInt(1,3);
        this.entityData.set(TEXTURE_INDEX, id);
        this.textureIndex = id;
        log.debug("Created SoulWispEntity with textureId {}", id);
    }


    public static AttributeSupplier.Builder createAttributes() {
        // Nutzen Sie LivingEntity.createLivingAttributes() als Basis, da Movement Speed ein LivingEntity-Attribut ist.
        return LivingEntity.createLivingAttributes()
                // Bewegung: Wir wollen langsame, kontrollierte Gleitbewegung
                .add(Attributes.MOVEMENT_SPEED, 0.05D)
                // Generelle Attribute, die Mob/LivingEntity benötigen (auch wenn nicht genutzt)
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TEXTURE_INDEX, 1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        CompoundTag nbt = tag.getCompound(ExampleMod.MODID);
        if(nbt.contains("TargetFlowerPos")) this.targetFlowerPos = ModUtils.blockPosFromTag( nbt.getCompound("TargetFlowerPos"));
        if(nbt.contains("TextureIndex")) {
            this.entityData.set(TEXTURE_INDEX, nbt.getInt("TextureIndex"));
            this.textureIndex = nbt.getInt("TextureIndex");
        }


        if(nbt.contains("WanderAroundTime")) this.wanderAroundTimeInTicks = nbt.getInt("WanderAroundTime");
        if(nbt.contains("MovementTicker")) this.movementTicker = nbt.getInt("MovementTicker");
        if(nbt.contains("WanderTicker")) this.wanderTicker = nbt.getInt("WanderTicker");
        if(nbt.contains("TicksToReachPosition")) this.ticksToReachPosition = nbt.getInt("TicksToReachPosition");
        if(nbt.contains("MovementVector")) this.movementVector = ModUtils.vec3FromTag(nbt.getCompound("MovementVector"));
        if(nbt.contains("DesiredPosition")) this.desiredPosition = ModUtils.vec3FromTag(nbt.getCompound("DesiredPosition"));

    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        CompoundTag wispData = new CompoundTag();
        if (this.targetFlowerPos != null) wispData.put("TargetFlowerPos", ModUtils.vec3ToTag(this.targetFlowerPos));
        wispData.putInt("TextureIndex", this.entityData.get(TEXTURE_INDEX));
        wispData.putInt("WanderAroundTime", this.wanderAroundTimeInTicks);
        wispData.putInt("MovementTicker", this.movementTicker);
        wispData.putInt("WanderTicker", this.wanderTicker);
        wispData.putInt("TicksToReachPosition", this.ticksToReachPosition);
        if(this.movementVector != null) wispData.put("MovementVector", ModUtils.vec3ToTag(movementVector));
        if(this.desiredPosition != null) wispData.put("DesiredPosition", ModUtils.vec3ToTag(this.desiredPosition));
        tag.put(ExampleMod.MODID, wispData);

    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide) return;
        if(this.targetFlowerPos == null || !this.level().getBlockState(this.targetFlowerPos).is(BlockRegistry.SoulFlower.get())) {
            this.discard();
            return;
        }
        if(Objects.isNull(wanderAroundTimeInTicks)) {
            this.wanderAroundTimeInTicks = this.getRandomWanderAroundTimeInTicks();
        }

        if(this.wanderTicker < wanderAroundTimeInTicks) {
            this.wander();
            this.wanderTicker++;
            this.movementTicker++;
        }else {
            this.moveToFlower();
            this.movementTicker++;
            if(this.movementTicker > this.ticksToReachPosition) {
                SoulFlower flowerBlock = (SoulFlower) this.level().getBlockState(this.targetFlowerPos).getBlock();
                flowerBlock.collectSoulFragment((ServerLevel) this.level(), this.level().getBlockState(this.targetFlowerPos), this.targetFlowerPos);
                this.discard();

            }
        }


    }

    private void wander() {
        if(this.desiredPosition == null || this.movementTicker >= this.ticksToReachPosition) {
            Vec3 travelPos = this.calculateRandomTravelPosition();
            int ticksToReachPos = this.calculateTimeToReachPosition(travelPos);
            this.desiredPosition = travelPos;
            this.ticksToReachPosition = ticksToReachPos;
            this.movementTicker = 0;
            Vec3 diffVec = this.desiredPosition.subtract(this.position());
            this.movementVector = diffVec.normalize().scale(MOVEMENT_SPEED);
        }
        this.handleMovement();
    }

    private void moveToFlower() {
        if(this.wanderTicker == wanderAroundTimeInTicks) {
            this.desiredPosition = this.targetFlowerPos.getCenter();
            this.ticksToReachPosition = this.calculateTimeToReachPosition(this.desiredPosition);
            this.movementTicker = 0;
            Vec3 diffVec = this.desiredPosition.subtract(this.position());
            this.movementVector = diffVec.normalize().scale(MOVEMENT_SPEED);
        }
        this.handleMovement();
    }

    private int getRandomWanderAroundTimeInTicks() {
        return this.level().getRandom().nextInt(20 * 30);
    }

    private void handleMovement() {
        this.setDeltaMovement(this.movementVector);
    }

    private int calculateTimeToReachPosition(Vec3 desiredPos) {
        double distance = this.position().distanceTo(desiredPos);
        return (int) (distance / MOVEMENT_SPEED);
    }

    private Vec3 calculateRandomTravelPosition() {
        RandomSource randomSource = this.level().random;
        int xOffset = randomSource.nextInt(-SoulFlowerBlockEntity.CLEAN_FOG_RADIUS, SoulFlowerBlockEntity.CLEAN_FOG_RADIUS);
        int yOffset = randomSource.nextInt(0, 3);
        int zOffset = randomSource.nextInt(-SoulFlowerBlockEntity.CLEAN_FOG_RADIUS, SoulFlowerBlockEntity.CLEAN_FOG_RADIUS);
        return this.targetFlowerPos.getCenter().add(xOffset, yOffset, zOffset);
    }

    public int getTextureIndex() {
        return this.entityData.get(TEXTURE_INDEX);
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity p_20303_) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
}