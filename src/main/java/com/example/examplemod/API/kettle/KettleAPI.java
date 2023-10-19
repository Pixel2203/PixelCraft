package com.example.examplemod.API.kettle;

import com.example.examplemod.API.kettle.recipe.KettleRecipes;
import com.example.examplemod.API.kettle.records.KettleIngredient;
import com.example.examplemod.API.kettle.records.KettleRecipe;
import com.example.examplemod.tag.TagRegistry;
import com.google.common.base.Objects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class KettleAPI {
    private static final HashMap<String, KettleRecipe> KETTLE_RECIPES = new HashMap<>();
    private static final HashMap<String, Item> KETTLE_INGREDIENTS = new HashMap<>();

    // Registry
    private static KettleRecipe TestKettleRecipe = registerRecipe(KettleRecipes.TestKettleRecipe);
    private static KettleRecipe TestKettleRecipe2 = registerRecipe(KettleRecipes.TestKettleRecipe2);
    private static KettleRecipe FloraPotionRecipe = registerRecipe(KettleRecipes.FloraPotionRecipe);
    // Handler Methods
    private static KettleRecipe registerRecipe(KettleRecipe recipe){
        KETTLE_RECIPES.put(
                recipe.serializedRecipe().toUpperCase(),
                recipe);
        return recipe;
    }
    public static KettleIngredient registerIngredient(KettleIngredient ingredient){
        KETTLE_INGREDIENTS.put(ingredient.id(), ingredient.item());
        return ingredient;
    }

    public static boolean isPartOfOrCompleteRecipe(String serializedIngredientList){
        if(serializedIngredientList == null){
            return false;
        }

        return  0 < KETTLE_RECIPES.keySet().stream().filter(key -> key.startsWith(serializedIngredientList.toUpperCase()) || key.equalsIgnoreCase(serializedIngredientList)).count();
    }
    public static boolean isValidRecipe(String serializedIngredientList){
        return KETTLE_RECIPES.get(serializedIngredientList.toUpperCase()) != null;
    }
    public static KettleRecipe getRecipeBySerializedIngredientList(String serializedIngredientList){
        return KETTLE_RECIPES.get(serializedIngredientList.toUpperCase());
    }



    public static KettleIngredient getIngredientByName(String id){
        return new KettleIngredient(KETTLE_INGREDIENTS.get(id),id);
    }
    public static KettleIngredient getIngredientByItem(Item item){
        if(!KETTLE_INGREDIENTS.containsValue(item)){
            return null;
        }
        for(String key : KETTLE_INGREDIENTS.keySet()){
            if(Objects.equal(item, KETTLE_INGREDIENTS.get(key))){
                return new KettleIngredient(item,key);
            }
        }
        return null;
    }
    public static boolean hasIngredientTag(ItemStack itemStack){
        return itemStack.is(TagRegistry.KETTLE_INGREDIENTS);
    }

}
