package com.example.examplemod.API.result;

import com.example.examplemod.API.ItemStackHelper;
import com.example.examplemod.item.ItemFactory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public interface ModResults {
    ItemStack DIAMONDS_5 = new ItemStack(Items.DIAMOND,5);
    ItemStack DIARRHEA_POTION = new ItemStack(ItemFactory.ExamplePotion,1);
    ItemStack FLORA_POTION_LEVEL1 = ItemStackHelper.createFloraPotion(1,1);
    ItemStack FLORA_POTION_LEVEL2 = ItemStackHelper.createFloraPotion(2,1);
    ItemStack FLORA_POTION_LEVEL3 = ItemStackHelper.createFloraPotion(3,1);

}
