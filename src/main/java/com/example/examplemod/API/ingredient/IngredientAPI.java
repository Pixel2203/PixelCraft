package com.example.examplemod.API.ingredient;

import net.minecraft.world.item.Item;

import java.util.*;

public class IngredientAPI {
    private static final HashMap<String, Item> KETTLE_INGREDIENTS = new HashMap<>();
    public static ModIngredient registerIngredient(ModIngredient ingredient){
        KETTLE_INGREDIENTS.put(ingredient.id(), ingredient.item());
        return ingredient;
    }
    public static ModIngredient getIngredientByName(String id){
        return new ModIngredient(KETTLE_INGREDIENTS.get(id),id);
    }
    public static ModIngredient getIngredientByItem(Item item){
        if(!KETTLE_INGREDIENTS.containsValue(item)){
            return null;
        }
        for(String key : KETTLE_INGREDIENTS.keySet()){
            if(Objects.equals(item, KETTLE_INGREDIENTS.get(key))){
                return new ModIngredient(item,key);
            }
        }
        return null;
    }
    public static List<ModIngredient> deserializeIngredientList(String serializedIngredients){
        String[] ingredients = serializedIngredients.split(",");
        List<ModIngredient> deserialized = new ArrayList<>();
        for(String ingredientID : ingredients){
            ModIngredient ingredient = getIngredientByName(ingredientID);
            if(Objects.isNull(ingredient)){
                return null;
            }
            deserialized.add(ingredient);
        }
        return deserialized;
    }
}
