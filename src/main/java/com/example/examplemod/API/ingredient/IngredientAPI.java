package com.example.examplemod.API.ingredient;

import net.minecraft.world.item.Item;

import java.util.*;

public class IngredientAPI {
    private static final HashMap<String, Item> INGREDIENTS = new HashMap<>();
    public static void register(){
        registerIngredient(ModIngredients.BLAZE_ROD);
        registerIngredient(ModIngredients.GLOWSTONE_DUST);
    }
    private static ModIngredient registerIngredient(ModIngredient ingredient){
        INGREDIENTS.put(ingredient.id(), ingredient.item());
        return ingredient;
    }
    public static ModIngredient getIngredientByName(String id){
        return new ModIngredient(INGREDIENTS.get(id),id);
    }
    public static ModIngredient getIngredientByItem(Item item){
        if(!INGREDIENTS.containsValue(item)){
            return null;
        }
        for(String key : INGREDIENTS.keySet()){
            if(Objects.equals(item, INGREDIENTS.get(key))){
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
