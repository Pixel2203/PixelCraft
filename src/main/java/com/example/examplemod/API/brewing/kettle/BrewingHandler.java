package com.example.examplemod.API.brewing.kettle;

import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredientRegistry;
import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipeRegistry;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.blockentity.custom.KettleBlockEntity;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class BrewingHandler {
    public static void handleIngredientFallOnKettle(ItemStack itemStack, BlockEntity blockEntity) {
        if(blockEntity instanceof KettleBlockEntity entity){
            Optional<KettleIngredient> ingredient = KettleIngredientRegistry.getIngredientByItem(itemStack.getItem());
            if(ingredient.isEmpty()){
                return;
            }
            String serializedRecipe = entity.getSerializedKettleRecipe();
            if(StringUtil.isNullOrEmpty(serializedRecipe)){
                serializedRecipe = ingredient.get().id();
            }else {
                serializedRecipe +=  "," +ingredient.get().id();
            }
            if(KettleRecipeRegistry.isPartOfOrCompleteRecipe(KettleRecipeRegistry.deserializeRecipeIngredientList(serializedRecipe))){
                entity.add(ingredient.get());
            }
        }
    }


}
