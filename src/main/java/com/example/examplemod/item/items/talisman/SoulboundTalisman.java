package com.example.examplemod.item.items.talisman;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
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
        if(player.level().isClientSide()) return InteractionResult.PASS;
        ModUtils.bind(itemStack, livingEntity.getUUID());
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return ModUtils.isBound(itemStack);
    }


    public void triggerEffect(LivingHurtEvent event, ItemStack talisman) {
        if(!ModUtils.isBound(talisman)) return;

        ServerLevel level = (ServerLevel) event.getEntity().level();
        Optional<Entity> boundEntity = Optional.ofNullable(talisman.getTag())
                .map(t -> t.getCompound(ExampleMod.MODID))
                .map(t -> t.getString(CustomNBTTags.BOUND_TO))
                .map(s ->  this.resolveEntity(level, s));

        if(boundEntity.isEmpty()) return;

        if(!(boundEntity.get() instanceof LivingEntity boundLivingEntity)) {
            log.error("Soul was bound to a not LivingEntity!");
            return;
        }


        float damageTaken = event.getAmount();
        float absorbed = Math.min(damageTaken, boundLivingEntity.getHealth());

        boundEntity.get().hurt(level.damageSources().magic(), absorbed);
        event.setAmount(damageTaken - absorbed);

        if(boundLivingEntity.getHealth() <= 0) APIHelper.breakCurioOfEntity(event.getEntity(), this );

    }

    @Override
    public void curioBreak(SlotContext slotContext, ItemStack stack) {
        super.curioBreak(slotContext, stack);
        LivingEntity entity = slotContext.entity();;
        entity.playSound(SoundEvents.TOTEM_USE);
    }
    @Nullable
    private Entity resolveEntity(ServerLevel level, String uuid) {
        try {
            UUID entityUUID = UUID.fromString(uuid);
            return level.getEntity(entityUUID);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID was provided in boundTo Tag!");
            return null;
        }
    }
}
