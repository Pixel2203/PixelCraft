package com.example.examplemod.API;

import com.example.examplemod.API.ingredient.ModIngredient;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.stream.IntStream;

public class APIHelper {
    public static String getNextRecipeString(String currentRecipe, @NotNull ModIngredient ingredient){
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
    public static boolean hasCurioEquipped(LivingEntity entity, Item item){
        LazyOptional<ICuriosItemHandler> lazyItemHandler = CuriosApi.getCuriosInventory(entity);
        if(!lazyItemHandler.isPresent()){
            return false;
        }
        Optional<ICuriosItemHandler> itemHandlerOptional = lazyItemHandler.resolve();
        if(itemHandlerOptional.isEmpty()){
            return false;
        }
        ICuriosItemHandler itemHandler = itemHandlerOptional.get();
        IItemHandlerModifiable iItemHandlerModifiable = itemHandler.getEquippedCurios();
        int slotAmount = iItemHandlerModifiable.getSlots();
        return IntStream.range(0, slotAmount).mapToObj(iItemHandlerModifiable::getStackInSlot).anyMatch(itemStack -> itemStack.is(item));
    }
    public static void breakCurioOfEntity(LivingEntity entity, Item curioItem){
        CuriosApi.getCuriosInventory(entity).ifPresent(itemHandler -> itemHandler.findFirstCurio(curioItem).ifPresent(slotResult -> {
                       CuriosApi.broadcastCurioBreakEvent(slotResult.slotContext());
            itemHandler.getEquippedCurios().setStackInSlot( slotResult.slotContext().index(),new ItemStack(Items.AIR));
        }));

    }

}
