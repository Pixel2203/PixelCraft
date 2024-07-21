package com.example.examplemod.entity.entities;

import com.example.examplemod.api.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class SoulEntity extends LivingEntity {

    public SoulEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    public static AttributeSupplier.Builder createAttributes(){
        return LivingEntity.createLivingAttributes().add(Attributes.MOVEMENT_SPEED, 0.1D);
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
    public void tick() {
        super.tick();
        if(!level().isClientSide()) {
            this.setDeltaMovement(new Vec3(0, this.getAttributeValue(Attributes.MOVEMENT_SPEED), 0));
           // X Spread          // Z spread#
            ModUtils.sendParticles((ServerLevel) this.level(), ParticleTypes.SNOWFLAKE, this.getOnPos(), 0.2f ,1, 2, 2, 2,0);

        }



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
