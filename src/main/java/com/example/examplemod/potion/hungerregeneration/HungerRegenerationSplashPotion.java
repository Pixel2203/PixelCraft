package com.example.examplemod.potion.hungerregeneration;

import com.example.examplemod.API.translation.CustomTranslatable;
import com.example.examplemod.potion.CustomSplashPotion;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
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
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        components.add(Component.translatable(CustomTranslatable.POTION_HUNGERREGENERATION_DESCRIPTION));
    }
}
