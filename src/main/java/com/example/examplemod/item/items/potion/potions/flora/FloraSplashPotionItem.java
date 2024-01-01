package com.example.examplemod.item.items.potion.potions.flora;

import com.example.examplemod.API.ModUtils;
import com.example.examplemod.API.nbt.CustomNBTTags;
import com.example.examplemod.API.nbt.NBTHelper;
import com.example.examplemod.API.translation.CustomTranslatable;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.potion.CustomSplashPotionItem;
import net.minecraft.nbt.CompoundTag;
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

    public static ItemStack createFloraPotion(int level, int amount){
        ItemStack potion = new ItemStack(ItemRegistry.POTION_FLORA.get(), amount);
        addFloraBoundsTag(potion,level);
        NBTHelper.addLevelNbtData(potion,level);
        ModUtils.setHoverName(potion,CustomTranslatable.POTION_FLORA_NAME);
        return potion;
    }
    private static void addFloraBoundsTag(ItemStack itemStack, int level){
        CompoundTag nbt = itemStack.hasTag() ? itemStack.getTag() : new CompoundTag();
        if(nbt == null){
            return;
        }
        int[] bounds = switch (level) {
            case 2 -> new int[]{3, 3};
            case 3 -> new int[]{6, 6};
            default -> new int[]{1, 1};
        };
        nbt.putIntArray(CustomNBTTags.POTION_BOUNDS,bounds);
        itemStack.setTag(nbt);
    }

}
