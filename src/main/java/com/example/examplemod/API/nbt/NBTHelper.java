package com.example.examplemod.API.nbt;

import com.example.examplemod.potion.CustomSplashPotion;
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
}
