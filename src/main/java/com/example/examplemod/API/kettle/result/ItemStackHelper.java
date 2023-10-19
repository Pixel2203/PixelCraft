package com.example.examplemod.API.kettle.result;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ItemFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemStackHelper {

    public static ItemStack createFloraPotion(int level, int amount){
        ItemStack potion = new ItemStack(ItemFactory.FloraPotion, amount);
        CompoundTag nbt = new CompoundTag();
        int[] bounds;
        switch (level){
            case 1:
                bounds = new int[]{1, 1};
                break;
            case 2:
                bounds = new int[]{3,3};
                break;
            case 3:
                bounds = new int[]{6,6};
                break;
            default:
                bounds = new int[]{1,1};
        }
        nbt.putInt(ExampleMod.MODID + "_potionLevel", level);
        nbt.putIntArray(ExampleMod.MODID + "_potionbounds",bounds);
        potion.setTag(nbt);
        potion.setHoverName(Component.translatable(ExampleMod.MODID+".potion_flora.level" +level));
        return potion;
    }
}
