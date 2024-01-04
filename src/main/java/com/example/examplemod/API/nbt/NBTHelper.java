package com.example.examplemod.API.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class NBTHelper {
    public static int getPotionLevel(ItemStack itemStack){
        if(!hasPotionLevel(itemStack)){
            return 0;
        }
        CompoundTag nbt = itemStack.getTag();
        if(Objects.isNull(nbt)){
            return 0;
        }
        return nbt.getInt(CustomNBTTags.POTION_LEVEL);
    }
    public static boolean hasPotionLevel(ItemStack itemStack){
        if(!itemStack.hasTag()){
            return false;
        }
        CompoundTag nbt = itemStack.getTag();
        if(Objects.isNull(nbt)){
            return false;
        }
        int level = nbt.getInt(CustomNBTTags.POTION_LEVEL);
        return level > 0;
    }
    public static void addPotionCommonNbtData(ItemStack stack, int duration, int amplifier){
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(CustomNBTTags.POTION_DURATION, duration);
        nbt.putInt(CustomNBTTags.POTION_AMPLIFIER, amplifier);
        stack.setTag(nbt);
    }
    public static void addPotionLevelNbtData(ItemStack stack, int level){
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
}
