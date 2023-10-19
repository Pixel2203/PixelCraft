package com.example.examplemod.API.kettle.result;

import com.example.examplemod.item.ItemFactory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public enum KettleResults {
    DIAMONDS_5(Items.DIAMOND,5),

    DIARRHEA_POTION(ItemFactory.ExamplePotion,1),
    FLORA_POTION_LEVEL1(ItemStackHelper.createFloraPotion(1,1)),
    FLORA_POTION_LEVEL2(ItemStackHelper.createFloraPotion(2,1)),
    FLORA_POTION_LEVEL3(ItemStackHelper.createFloraPotion(3,1));

    public ItemStack stack;
    KettleResults(Item item, int amount){
        this.stack = new ItemStack(item,amount);
    }
    KettleResults(ItemStack stack){
        this.stack = stack;
    }
}
