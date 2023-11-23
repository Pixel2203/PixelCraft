package com.example.examplemod.API;

import com.example.examplemod.API.ingredient.ModIngredient;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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
    public static void spawnItemEntity(Level level, double x, double y , double z, ItemStack itemStack, Vec3 deltaMovement){
        ItemEntity itemEntity = new ItemEntity(level,x ,y,z,itemStack);
        itemEntity.setDeltaMovement(deltaMovement);
        level.addFreshEntity(itemEntity);
    }
    public static void spawnItemEntity(Level level, Vec3 spawnPos, ItemStack itemStack, Vec3 deltaMovement){
        ItemEntity itemEntity = new ItemEntity(level,spawnPos.x ,spawnPos.y,spawnPos.z,itemStack);
        itemEntity.setDeltaMovement(deltaMovement);
        level.addFreshEntity(itemEntity);
    }
    public static int findItemInInventory(Player player, ItemStack itemStack) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slotStack = player.getInventory().getItem(i);
            if (ItemStack.isSameItem(slotStack, itemStack) && ItemStack.isSameItemSameTags(slotStack, itemStack)) {
                return i; // Found the item in this slot
            }
        }

        return -1; // Item not found in the player's inventory
    }

}
