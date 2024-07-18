package com.example.examplemod.item.items.potion;

import com.example.examplemod.api.nbt.CustomNBTTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class CustomThrownPotion extends ThrownPotion implements ItemSupplier {

    public CustomThrownPotion(Level p_37535_, LivingEntity p_37536_) {
        super(p_37535_, p_37536_);

    }

    /**
     * If wanted, the returned effects will be applied to the entities in Splash area!
     * If not needed, just return null or List.of()
     * @param duration Time in seconds how long the effect will be applied to living entities
     * @param amplifier Makes the effect stronger if wanted
     * @return List of MobEffectInstances / Effects that will be applied to the player
     */
    protected abstract List<MobEffectInstance> getPotionEffects(int duration, int amplifier);
    @Override
    protected void onHit(@NotNull HitResult p_37543_) {
        super.onHit(p_37543_);
        if (!this.level().isClientSide) {
            ItemStack itemstack = this.getItem();
            Potion potion = PotionUtils.getPotion(itemstack);
            List<MobEffectInstance> list = Objects.isNull(getPotionEffects(getEffectDuration(),getEffectAmplifier())) ? List.of() : getPotionEffects(getEffectDuration(),getEffectAmplifier());
            this.applySplash(list, p_37543_.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)p_37543_).getEntity() : null);
            int i = potion.hasInstantEffects() ? 2007 : 2002;
            this.level().levelEvent(i, this.blockPosition(), PotionUtils.getColor(itemstack));
            this.discard();
        }
    }
    private void applySplash(List<MobEffectInstance> p_37548_, @Nullable Entity p_37549_) {
        AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            Entity entity = this.getEffectSource();

            for(LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double d0 = this.distanceToSqr(livingentity);
                    if (d0 < 16.0D) {
                        double d1;
                        if (livingentity == p_37549_) {
                            d1 = 1.0D;
                        } else {
                            d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                        }

                        for(MobEffectInstance mobeffectinstance : p_37548_) {
                            MobEffect mobeffect = mobeffectinstance.getEffect();
                            if (mobeffect.isInstantenous()) {
                                mobeffect.applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), d1);
                            } else {
                                int i = mobeffectinstance.mapDuration((p_267930_) -> {
                                    return (int)(d1 * (double)p_267930_ + 0.5D);
                                });
                                MobEffectInstance mobeffectinstance1 = new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible());
                                if (!mobeffectinstance1.endsWithin(20)) {
                                    livingentity.addEffect(mobeffectinstance1, entity);
                                }
                            }
                        }
                    }
                }
            }
        }

    }
    private int getEffectDuration() {
        ItemStack itemStack = getItem();
        if(itemStack.hasTag()){
            return itemStack.getTag().getInt(CustomNBTTags.POTION_DURATION);
        }
        return 0;
    }
    private int getEffectAmplifier(){
        ItemStack itemStack = getItem();
        if(itemStack.hasTag()){
            return itemStack.getTag().getInt(CustomNBTTags.POTION_AMPLIFIER);
        }
        return 0;
    }

}
