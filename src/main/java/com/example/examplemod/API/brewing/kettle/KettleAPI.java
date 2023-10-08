package com.example.examplemod.API.brewing.kettle;

import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredientFactory;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import com.example.examplemod.blockentity.custom.KettleBlockEntity;
import com.example.examplemod.tag.TagRegistry;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class KettleAPI {
    private static final HashMap<String, KettleRecipe> KETTLE_RECIPES = new HashMap<>();
    private static final HashMap<Item,String> KETTLE_INGREDIENTS = new HashMap<>();

    // Registry
    private static KettleRecipe TestKettleRecipe =
            registerRecipe(Items.DIAMOND,
            KettleIngredientFactory.GLOWSTONE_DUST,KettleIngredientFactory.BLAZE_ROD);
    private static KettleRecipe TestKettleRecipe2 =
            registerRecipe(Items.DIRT,
            KettleIngredientFactory.BLAZE_ROD, KettleIngredientFactory.GLOWSTONE_DUST);
    private static final KettleIngredient BLAZE_ROD =
            registerIngredient(KettleIngredientFactory.BLAZE_ROD);
    private static final KettleIngredient GLOWSTONE =
            registerIngredient(KettleIngredientFactory.GLOWSTONE_DUST);



    // Handler Methods
    private static KettleRecipe registerRecipe(Item result, KettleIngredient... ingredients){
        String kettleRecipeString = serializeRecipeIngredientList(ingredients);
        KettleRecipe kettleRecipe = new KettleRecipe(result,ingredients);
        KETTLE_RECIPES.put(
                serializeRecipeIngredientList(kettleRecipe.ingredients()).toUpperCase(),
                kettleRecipe);
        return kettleRecipe;
    }
    private static String serializeRecipeIngredientList(KettleIngredient... ingredients){
        String recipe = "";
        for(KettleIngredient ingredient: ingredients){
            if(StringUtil.isNullOrEmpty(recipe)){
                recipe = ingredient.id();
                continue;
            }
            recipe += "," + ingredient.id();
        }
        return recipe.toUpperCase();
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


    private static KettleIngredient registerIngredient(KettleIngredient ingredient1){
        KETTLE_INGREDIENTS.put(ingredient1.item(),ingredient1.id());
        return ingredient1;
    }

    public static KettleIngredient getIngredientByName(String name){
        if(!KETTLE_INGREDIENTS.containsValue(name.toUpperCase())){
            return null;
        }
        for(Item key : KETTLE_INGREDIENTS.keySet()){
            if(KETTLE_INGREDIENTS.get(key).equalsIgnoreCase(name)){
                return new KettleIngredient(key,name);
            }
        }
        return null;
    }
    public static KettleIngredient getIngredientByItem(Item item){
        String found = KETTLE_INGREDIENTS.get(item);
        if(StringUtil.isNullOrEmpty(found)){
            return null;
        }
        return new KettleIngredient(item, KETTLE_INGREDIENTS.get(item));
    }
    public static boolean hasIngredientTag(ItemStack itemStack){
        return itemStack.is(TagRegistry.KETTLE_INGREDIENTS);
    }

}
