package com.example.examplemod.API.kettle;

import com.example.examplemod.API.recipe.ModRecipe;
import com.example.examplemod.tag.TagFactory;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class KettleAPI {
    private static final HashMap<String, ModRecipe> KETTLE_RECIPES = new HashMap<>();

    private static final ModRecipe<?> TestKettleRecipe = registerRecipe(KettleRecipes.TestKettleRecipe);
    private static final ModRecipe<?> TestKettleRecipe2 = registerRecipe(KettleRecipes.TestKettleRecipe2);
    private static final ModRecipe<?> FloraPotionRecipe = registerRecipe(KettleRecipes.FloraPotionRecipe);
    // Registry
    // Handler Methods
    private static ModRecipe<?> registerRecipe(ModRecipe<?> recipe){
        KETTLE_RECIPES.put(
                recipe.serializedRecipe().toUpperCase(),
                recipe);
        return recipe;
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
    public static ModRecipe getRecipeBySerializedIngredientList(String serializedIngredientList){
        return KETTLE_RECIPES.get(serializedIngredientList.toUpperCase());
    }




    public static boolean hasIngredientTag(ItemStack itemStack){
        return itemStack.is(TagFactory.KETTLE_INGREDIENTS);
    }

}
