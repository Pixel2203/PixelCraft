package com.example.examplemod.API;

import com.example.examplemod.API.ingredient.ModIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class APIHelper {
    public static String getNextRecipeString(String currentRecipe, ModIngredient ingredient){
        if(StringUtil.isNullOrEmpty(currentRecipe)){
            currentRecipe = ingredient.id();
        }else {
            currentRecipe +=  "," +ingredient.id();
        }
        return currentRecipe;
    }
    public static void spawnItemEntity(Level level, BlockPos spawnPos, ItemStack itemStack, Vec3 deltaMovement){
        ItemEntity itemEntity = new ItemEntity(level,spawnPos.getX() ,spawnPos.getY(),spawnPos.getZ(),itemStack);
        itemEntity.setDeltaMovement(deltaMovement);
        level.addFreshEntity(itemEntity);
    }
}
