package com.example.examplemod.API.result;

import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.potion.potions.flora.FloraSplashPotionItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Lazy;


public interface ModResults {
    Lazy<ItemStack> DIAMONDS_5 = Lazy.of(() -> new ItemStack(Items.DIAMOND,5));
    Lazy<ItemStack> FLORA_POTION_LEVEL1 = Lazy.of(() -> FloraSplashPotionItem.createFloraPotion(1,1));
    Lazy<ItemStack> FLORA_POTION_LEVEL2 = Lazy.of(() ->FloraSplashPotionItem.createFloraPotion(2,1));
    Lazy<ItemStack> FLORA_POTION_LEVEL3 = Lazy.of(() ->FloraSplashPotionItem.createFloraPotion(3,1));

}
