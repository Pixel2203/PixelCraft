package com.example.examplemod.item.items.talisman;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.nbt.CustomNBTTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Optional;
import java.util.UUID;

public class SoulboundTalisman extends TalismanItem{
    private static final Logger log = LoggerFactory.getLogger(SoulboundTalisman.class);

    @Override
    protected SoundEvent getEquipSound() {
        return null;
    }

    @Override
    protected SoundEvent getUnEquipSound() {
        return null;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand hand) {
        if(!player.level().isClientSide()) {
            String uuid = livingEntity.getUUID().toString();
            CompoundTag tag = itemStack.getOrCreateTag();
            tag.putString(CustomNBTTags.BOUND_TO, uuid);
            itemStack.save(tag);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, hand);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {

        CompoundTag tag = itemStack.getOrCreateTag();
        if(tag.contains(CustomNBTTags.BOUND_TO)) {
            return true;
        }
        return super.isFoil(itemStack);
    }

    public void triggerEffect(LivingHurtEvent event, ItemStack itemStack) {
        ServerLevel level = (ServerLevel) event.getEntity().level();

        Entity boundEntity = Optional.ofNullable(itemStack.getTag())
                .map(t -> t.getString(CustomNBTTags.BOUND_TO))
                .map(UUID::fromString)
                .map(level::getEntity)
                .orElse(null);


        if(!(boundEntity instanceof LivingEntity boundLivingEntity)) {
            log.error("Soul was bound to a not LivingEntity!");
            return;
        }


        float damageTaken = event.getAmount();
        float absorbed = Math.min(damageTaken, boundLivingEntity.getHealth());

        boundEntity.hurt(level.damageSources().magic(), absorbed);
        event.setAmount(damageTaken - absorbed);

        if(boundLivingEntity.getHealth() <= 0) APIHelper.breakCurioOfEntity(event.getEntity(), this );

    }

    @Override
    public void curioBreak(SlotContext slotContext, ItemStack stack) {
        super.curioBreak(slotContext, stack);
        LivingEntity entity = slotContext.entity();;
        entity.playSound(SoundEvents.TOTEM_USE);
    }
}
