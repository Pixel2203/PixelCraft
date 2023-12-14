package com.example.examplemod.API.ingredient;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class IngredientAPI {
    private static final HashMap<String, Item> INGREDIENTS = new HashMap<>();
    public static void register(){
        registerIngredient(ModIngredients.BLAZE_ROD);
        registerIngredient(ModIngredients.GLOWSTONE_DUST);
        registerIngredient(ModIngredients.GREEN_DYE);
        registerIngredient(ModIngredients.REDSTONE_DUST);
    }


    private static void registerIngredient(ModIngredient ingredient){
        INGREDIENTS.put(ingredient.id(), ingredient.item());
    }
    public static @NotNull ModIngredient getIngredientByName(String id){
        return new ModIngredient(INGREDIENTS.get(id),id);
    }
    public static @Nullable ModIngredient getIngredientByItem(Item item){
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
            deserialized.add(ingredient);
        }
        return deserialized;
    }
}
