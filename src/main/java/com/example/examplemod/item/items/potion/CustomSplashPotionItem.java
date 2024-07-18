package com.example.examplemod.item.items.potion;

import com.example.examplemod.API.translation.CustomTranslatable;
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

public abstract class CustomSplashPotionItem extends SplashPotionItem {


    public CustomSplashPotionItem(Properties p_43241_) {
        super(p_43241_);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(level.isClientSide()){
            return InteractionResultHolder.pass(itemstack);
        }
        if(Objects.isNull(getThrownPotion(level,player))){
            return InteractionResultHolder.fail(itemstack);
        }
        ThrownPotion thrownPotion = getThrownPotion(level,player);
        thrownPotion.setItem(itemstack);
        thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
        level.addFreshEntity(thrownPotion);
        itemstack.shrink(1);
        return InteractionResultHolder.success(itemstack);


    }
    protected abstract ThrownPotion getThrownPotion(Level level, Player player);
    protected Component getTranslatedDescription(){
        return Component.translatable(CustomTranslatable.POTION_DESCRIPTION_DEFAULT);
    };
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if(Objects.nonNull(getTranslatedDescription())){
            components.add(getTranslatedDescription());
        }
    }


}
