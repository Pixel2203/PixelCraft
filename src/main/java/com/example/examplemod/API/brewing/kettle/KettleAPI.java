package com.example.examplemod.API.brewing.kettle;

import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredients;
import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipes;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import com.example.examplemod.tag.TagRegistry;
import com.google.common.base.Objects;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class KettleAPI {
    private static final HashMap<String, KettleRecipe> KETTLE_RECIPES = new HashMap<>();
    private static final HashMap<String, Item> KETTLE_INGREDIENTS = new HashMap<>();

    // Registry
    private static KettleRecipe TestKettleRecipe = registerRecipe(KettleRecipes.TestKettleRecipe);
    private static KettleRecipe TestKettleRecipe2 = registerRecipe(KettleRecipes.TestKettleRecipe2);


    // Handler Methods
    private static KettleRecipe registerRecipe(KettleRecipe recipe){
        KettleRecipe kettleRecipe = new KettleRecipe(recipe.result(),recipe.ingredients());
        KETTLE_RECIPES.put(
                kettleRecipe.serializedRecipe(),
                kettleRecipe);
        return kettleRecipe;
    }
    public static KettleIngredient registerIngredient(KettleIngredient ingredient){
        KETTLE_INGREDIENTS.put(ingredient.id(), ingredient.item());
        return ingredient;
    }


    private static KettleIngredient[]  deserializeRecipeIngredientList (String recipe){
        if(recipe == null){return null;}
        String[] ingredientsSeperated = recipe.split(",");
        KettleIngredient[] ingredients = new KettleIngredient[ingredientsSeperated.length];
        for(int i = 0; i < ingredientsSeperated.length; i++){
            KettleIngredient ingredient1 = getIngredientByName(ingredientsSeperated[i]);
            if(ingredient1 == null){
                return null;
            }
            ingredients[i] = ingredient1;
        }
        return ingredients;
    }
    public static boolean isPartOfOrCompleteRecipe(String serializedIngredientList){
        if(serializedIngredientList == null){
            return false;
        }

        return  0 < KETTLE_RECIPES.keySet().stream().filter(key -> key.startsWith(serializedIngredientList.toUpperCase()) || key.equalsIgnoreCase(serializedIngredientList)).count();
/*
        for(KettleRecipe kettleRecipe : KETTLE_RECIPES){
            if(kettleRecipe.ingredients().length >= ingredients.length && ingredients.length > 0){
                boolean error = false;
                for(int i = 0; i < ingredients.length; i++ ){
                    if (!kettleRecipe.ingredients()[i].id().equalsIgnoreCase(ingredients[i].id())) {
                        error = true;
                        break;
                    }
                }
                if(!error){
                    return true;
                }
            }
        }

 */
        // return false;
    }
    public static boolean isValidRecipe(String serializedIngredientList){
        return KETTLE_RECIPES.get(serializedIngredientList) != null;
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
