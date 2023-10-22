package com.example.examplemod.potion;

import com.example.examplemod.ExampleMod;
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
    private final int duration;
    private final int amplifier;

    public CustomSplashPotion(Properties p_43241_, int duration, int amplifier) {
        super(p_43241_);
        this.duration = duration;
        this.amplifier = amplifier;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            T thrownpotion = getThrownPotion(level,player, null);
            thrownpotion.setItem(itemstack);
            thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            level.addFreshEntity(thrownpotion);
            itemstack.shrink(1);
            return InteractionResultHolder.success(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }
    protected abstract T getThrownPotion(Level level, Player player, CustomSplashPotion potion );
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        CompoundTag nbt = itemStack.getTag();
        if(Objects.isNull(nbt)){
            return;
        }
        int potionLevel = nbt.getInt(ExampleMod.MODID + "_potionLevel");
        components.add(Component.translatable(ExampleMod.MODID+".potionLevel" + potionLevel));
    }

    public int getDuration(){
        return this.duration;
    }
    public int getAmplifier(){
        return this.amplifier;
    }
}
