package com.example.examplemod.item.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.runes.RuneType;
import com.example.examplemod.item.ItemRegistry;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Slf4j
public class WoodenRune extends Item {
    public WoodenRune() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide()) return super.use(level, player, hand);
        if(hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(player.getItemInHand(hand));
        this.activate(player);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private void activate(Player player) {
        ItemStack runeItem = player.getMainHandItem();
        MobEffectInstance effect = getEffect(runeItem);
        runeItem.shrink(1);
        if(Objects.nonNull(effect)) {
            player.addEffect(effect);
            player.playSound(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
        }
    }

    @Nullable
    private MobEffectInstance getEffect(ItemStack runeItem) {
        RuneType runeType = getRuneType(runeItem);
        if(Objects.isNull(runeType)) return null;
        MobEffect effect = runeType.getEffect().get();

        return new MobEffectInstance(effect, runeType.getDuration(), runeType.getStrength());

    }

    @Nullable
    private RuneType getRuneType(ItemStack runeItem) {
        CompoundTag tag = runeItem.getOrCreateTag();
        if(!tag.contains(ExampleMod.MODID)) return null;
        CompoundTag modTag = tag.getCompound(ExampleMod.MODID);
        if(!modTag.contains("runeType")) {
            log.warn("Missing runeType tag for rune item {}, although mod tag existed", runeItem);
            return null;
        }
        return RuneType.valueOf(modTag.getString("runeType"));
    }

    public static ItemStack create(RuneType runeType) {
        ItemStack stack = new ItemStack(ItemRegistry.WOODEN_RUNE.get());
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag modTag = new CompoundTag();
        modTag.putString("runeType", runeType.name());
        tag.put(ExampleMod.MODID, modTag);
        stack.setTag(tag);
        return stack;
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        RuneType runeType = getRuneType(p_41458_);
        if(Objects.isNull(runeType)) return super.getName(p_41458_);
        return Component.translatable("item.pixelcraft.wooden_rune." + runeType.name().toLowerCase());
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return getRuneType(p_41453_) != null;
    }
}
