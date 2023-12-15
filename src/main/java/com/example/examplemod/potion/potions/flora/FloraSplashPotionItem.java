package com.example.examplemod.potion.potions.flora;

import com.example.examplemod.API.nbt.NBTHelper;
import com.example.examplemod.API.translation.CustomTranslatable;
import com.example.examplemod.potion.CustomSplashPotionItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FloraSplashPotionItem extends CustomSplashPotionItem {


    public FloraSplashPotionItem(Properties p_43241_) {
        super(p_43241_);
    }


    @Override
    protected ThrownFloraPotion getThrownPotion(Level level, Player player) {
        return new ThrownFloraPotion(level, player);
    }

    @Override
    protected Component getTranslatedDescription() {
        return Component.translatable(CustomTranslatable.POTION_FLORA_DESCRIPTION);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        String langKey = CustomTranslatable.POTION_LEVEL + NBTHelper.getPotionLevel(itemStack);
        components.add(Component.translatable(langKey));
        super.appendHoverText(itemStack, level, components, flag);
    }
}
