package com.example.examplemod.API;

import com.example.examplemod.API.nbt.CustomNBTTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class ModUtils {
    public static Vec3 calcCenterOfBlock(BlockPos blockPos){
        return new Vec3(blockPos.getX() + 0.5d,blockPos.getY() + 0.5d,blockPos.getZ()+ 0.5d);
    }
    public static double calculateDistanceBetweenBlockPos(BlockPos firstPos, BlockPos secondPos){
        int dX = firstPos.getX() - secondPos.getX();
        int dZ = firstPos.getZ() - secondPos.getZ();
        int dY = firstPos.getY() - secondPos.getY();
        double a = Math.sqrt(Math.pow(dX,2) + Math.pow(dZ,2));
        return Math.sqrt(Math.pow(dY,2) + Math.pow(a,2));
    }

    public static void setHoverName(ItemStack itemStack, String translatable){
        itemStack.setHoverName(Component.translatable(translatable));
    }
}
