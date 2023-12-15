package com.example.examplemod.API;

import com.example.examplemod.API.nbt.CustomNBTTags;
import com.example.examplemod.API.translation.CustomTranslatable;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemStackHelper {

    public static ItemStack createFloraPotion(int level, int amount){
        ItemStack potion = new ItemStack(ItemRegistry.POTION_FLORA.get(), amount);
        addFloraBoundsTag(potion,level);
        addLevelNbtData(potion,level);
        setHoverName(potion,CustomTranslatable.POTION_FLORA_NAME);
        return potion;
    }
    private static void addFloraBoundsTag(ItemStack itemStack, int level){
        CompoundTag nbt;
        if(itemStack.hasTag()){
            nbt = itemStack.getTag();
        }else{
            nbt = new CompoundTag();
        }
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
        nbt.putIntArray(CustomNBTTags.POTION_BOUNDS,bounds);
        itemStack.setTag(nbt);
    }
    public static ItemStack createFreezePotion(int amount, int duration, int amplifier){
        ItemStack potion = new ItemStack(ItemRegistry.POTION_FREEZE.get(),amount);
        addCommonNbtData(potion,duration,amplifier);
        setHoverName(potion, CustomTranslatable.POTION_FREEZE_NAME);
        return potion;
    }
    public static ItemStack createHungerRegenerationPotion(int level,int amount, int duration, int amplifier){
        ItemStack potion = new ItemStack(ItemRegistry.POTION_HUNGER_REGENERATION.get(),amount);
        setHoverName(potion,CustomTranslatable.POTION_HUNGER_REGENERATION_NAME);
        addCommonNbtData(potion,duration,amplifier);
        addLevelNbtData(potion,level);
        return potion;
    }
    private static void addCommonNbtData(ItemStack stack, int duration, int amplifier){
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(CustomNBTTags.POTION_DURATION, duration);
        nbt.putInt(CustomNBTTags.POTION_AMPLIFIER, amplifier);
        stack.setTag(nbt);
    }
    private static void addLevelNbtData(ItemStack stack, int level){
        CompoundTag nbt;
        if(stack.hasTag()){
            nbt = stack.getTag();
        }else {
            nbt = new CompoundTag();
        }
        if(Objects.isNull(nbt)){
            return;
        }
        nbt.putInt(CustomNBTTags.POTION_LEVEL, level);
        stack.setTag(nbt);
    }
    private static void setHoverName(ItemStack itemStack, String translatable){
        itemStack.setHoverName(Component.translatable(translatable));
    }
}
