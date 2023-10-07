package com.example.examplemod.API.brewing.kettle;

import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredientRegistry;
import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipeFactory;
import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipeRegistry;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import com.example.examplemod.blockentity.custom.KettleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class BrewingHandler {
    public static void handleIngredientFallOnKettle(ItemStack itemStack, KettleBlockEntity entity) {

            KettleIngredient foundMatchingIngredient = KettleIngredientRegistry.getIngredientByItem(itemStack.getItem());
            if(foundMatchingIngredient == null){
                return;
            }
            String serializedRecipe = entity.getSerializedKettleRecipe();
            String nextRecipeString = KettleRecipeFactory.getNextRecipeString(serializedRecipe, foundMatchingIngredient);
            if(KettleRecipeRegistry.isPartOfOrCompleteRecipe(nextRecipeString)){
                acceptIngredient(itemStack, entity, foundMatchingIngredient);
            }

    }
    private static void acceptIngredient(ItemStack itemStack,KettleBlockEntity entity, KettleIngredient ingredient){
        entity.add(ingredient);
        itemStack.shrink(1);
    }
    public static void handleOnBottle(){

    }
    public static void finishRecipe(ItemStack thrown, BlockPos blockPos , Level level , KettleBlockEntity entity){
        String currentRecipe = entity.getSerializedKettleRecipe().toUpperCase();
        if(KettleRecipeRegistry.isValidRecipe(currentRecipe)){
            KettleRecipe kettleRecipe = KettleRecipeRegistry.getRecipeBySerializedIngredientList(currentRecipe);
            if(kettleRecipe == null){
                return;
            }
            Item resultItem = kettleRecipe.result();
            ItemEntity itemEntity = new ItemEntity(EntityType.ITEM,level);
            itemEntity.setItem(new ItemStack(resultItem));
            level.addFreshEntity(itemEntity);


        }
    }


}
