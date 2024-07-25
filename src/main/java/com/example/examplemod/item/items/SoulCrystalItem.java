package com.example.examplemod.item.items;

import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.translation.CustomTranslatable;
import com.example.examplemod.entity.entities.SoulEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulCrystalItem extends Item {
    private final float maxCharge;
    public SoulCrystalItem(Properties p_41383_, float maxCharge) {
        super(p_41383_);
        this.maxCharge = maxCharge;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(player.level().isClientSide() || hand == InteractionHand.OFF_HAND){
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        List<AreaEffectCloud> soulParticlesInRange = level.getEntitiesOfClass(AreaEffectCloud.class, player.getBoundingBox().inflate(2.0D), (effectCloud) -> effectCloud != null && effectCloud.isAlive() && effectCloud.getOwner() instanceof SoulEntity);
        if(soulParticlesInRange.isEmpty()){
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        AreaEffectCloud clickedEntity = soulParticlesInRange.get(0);
        return retrieveEnergyFromEffectCloud(level, player, hand, clickedEntity);

    }
    @NotNull
    public InteractionResultHolder<ItemStack> retrieveEnergyFromEffectCloud(Level level, Player player, InteractionHand hand,AreaEffectCloud entity) {

        ItemStack crystal = player.getItemInHand(hand);
        CompoundTag nbt = crystal.hasTag() ? crystal.getTag() : new CompoundTag();
        float chargedInItem = nbt.getFloat(CustomNBTTags.ENERGY_CHARGE);
        //float retrievedFromSoul = soulEntity.retrieveEnergyFromSoul(player.getOnPos());
        float retrieved = Math.min(entity.getRadius(), 0.05f);
        entity.setRadius(entity.getRadius() - retrieved);
        if(entity.getRadius() == 0){
            entity.discard();
        }
        float nextEnergyLevel= Math.min(maxCharge, chargedInItem + retrieved);
        nbt.putFloat(CustomNBTTags.ENERGY_CHARGE, nextEnergyLevel);
        crystal.setTag(nbt);
        return InteractionResultHolder.success(crystal);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        CompoundTag nbt = itemStack.hasTag() ? itemStack.getTag() : new CompoundTag();
        components.add(Component.translatable(CustomTranslatable.SOUL_CRYSTAL_INFO));
        components.add(Component.literal("%s / %s".formatted(nbt.getFloat(CustomNBTTags.ENERGY_CHARGE), maxCharge)));
        super.appendHoverText(itemStack, level, components, flag);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        if(!itemStack.hasTag()){
            return false;
        }
        CompoundTag nbt = itemStack.getTag();
        float currentCharge = nbt.getFloat(CustomNBTTags.ENERGY_CHARGE);
        return currentCharge == maxCharge;
    }
}
