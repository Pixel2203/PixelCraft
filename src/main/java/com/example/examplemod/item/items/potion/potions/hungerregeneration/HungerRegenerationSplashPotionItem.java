package com.example.examplemod.item.items.potion.potions.hungerregeneration;

import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.NBTHelper;
import com.example.examplemod.api.translation.CustomTranslatable;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.potion.CustomSplashPotionItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HungerRegenerationSplashPotionItem extends CustomSplashPotionItem {
    public HungerRegenerationSplashPotionItem(Properties properties) {
        super(properties);
    }

    @Override
    protected ThrownHungerRegenerationPotion getThrownPotion(Level level, Player player) {
        return new ThrownHungerRegenerationPotion(level,player);
    }

    @Override
    protected Component getTranslatedDescription() {
        return Component.translatable(CustomTranslatable.POTION_HUNGER_REGENERATION_DESCRIPTION);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        String langKey = CustomTranslatable.POTION_LEVEL + NBTHelper.getPotionLevel(itemStack);
        components.add(Component.translatable(langKey));
        super.appendHoverText(itemStack, level, components, flag);
    }

    public static ItemStack createHungerRegenerationPotion(int level,int amount, int duration, int amplifier){
        ItemStack potion = new ItemStack(ItemRegistry.POTION_HUNGER_REGENERATION.get(),amount);
        NBTHelper.addPotionCommonNbtData(potion,duration,amplifier);
        NBTHelper.addPotionLevelNbtData(potion,level);
        ModUtils.setHoverName(potion,CustomTranslatable.POTION_HUNGER_REGENERATION_NAME);
        return potion;
    }

}
