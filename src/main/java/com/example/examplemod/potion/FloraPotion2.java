package com.example.examplemod.potion;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.potion.thrown.ThrownFloraPotion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
/*
public class FloraPotion2 extends SplashPotionItem {
    public FloraPotion(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            ThrownFloraPotion thrownpotion = new ThrownFloraPotion(level, player);
            thrownpotion.setItem(itemstack);
            thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            level.addFreshEntity(thrownpotion);
            itemstack.shrink(1);
            return InteractionResultHolder.success(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        CompoundTag nbt = itemStack.getTag();
        if(Objects.isNull(nbt)){
            return;
        }
        int potionLevel = nbt.getInt(ExampleMod.MODID + "_potionLevel");
        components.add(Component.translatable(ExampleMod.MODID+".potionLevel" + potionLevel));
    }

}
*/