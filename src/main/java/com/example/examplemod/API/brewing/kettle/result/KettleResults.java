package com.example.examplemod.API.brewing.kettle.result;

import com.example.examplemod.item.ItemFactory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public enum KettleResults {
    DIAMONDS_5(Items.DIAMOND,5),

    DIARRHEA_POTION(ItemFactory.ExamplePotion,1);

    public ItemStack stack;
    KettleResults(Item item, int amount){
        this.stack = new ItemStack(item,amount);
    }
}
