package com.example.examplemod.potion;

import com.example.examplemod.API.nbt.CustomNBTTags;
import com.example.examplemod.API.translation.CustomTranslatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class CustomSplashPotion<T extends ThrownPotion> extends SplashPotionItem {

    private String langTag = "";

    public CustomSplashPotion(Properties p_43241_) {
        super(p_43241_);

    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            T thrownPotion = getThrownPotion(level,player);
            thrownPotion.setItem(itemstack);
            thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            level.addFreshEntity(thrownPotion);
            itemstack.shrink(1);
            return InteractionResultHolder.success(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }
    protected abstract T getThrownPotion(Level level, Player player);
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {

        CompoundTag nbt = itemStack.getTag();
        if(Objects.isNull(nbt)){
            return;
        }
        int potionLevel = nbt.getInt(CustomNBTTags.POTION_LEVEL);
        components.add(Component.translatable(CustomTranslatable.POTION_LEVEL + potionLevel));
        components.add(Component.translatable(CustomTranslatable.POTION_DESCRIPTION_DEFAULT));
    }

}
