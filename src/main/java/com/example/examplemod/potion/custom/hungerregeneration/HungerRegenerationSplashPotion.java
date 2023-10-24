package com.example.examplemod.potion.custom.hungerregeneration;

import com.example.examplemod.API.nbt.NBTHelper;
import com.example.examplemod.API.translation.CustomTranslatable;
import com.example.examplemod.potion.CustomSplashPotion;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HungerRegenerationSplashPotion extends CustomSplashPotion<ThrownHungerRegenerationPotion> {
    public HungerRegenerationSplashPotion(Properties properties) {
        super(properties);
    }

    @Override
    protected ThrownHungerRegenerationPotion getThrownPotion(Level level, Player player) {
        return new ThrownHungerRegenerationPotion(level,player);
    }

    @Override
    protected Component getTranslatedDescription() {
        return Component.translatable(CustomTranslatable.POTION_HUNGERREGENERATION_DESCRIPTION);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        String langKey = CustomTranslatable.POTION_LEVEL + NBTHelper.getPotionLevel(itemStack);
        components.add(Component.translatable(langKey));
        super.appendHoverText(itemStack, level, components, flag);
    }
}
