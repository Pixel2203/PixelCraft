package com.example.examplemod.api.ingredient;

import com.example.examplemod.tag.TagFactory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        registerIngredient(ModIngredients.PURPLE_DYE);
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
    public static @NotNull List<ModIngredient> deserializeIngredientList(String serializedIngredients){
        String[] ingredients = serializedIngredients.split(",");
        List<ModIngredient> deserialized = new ArrayList<>();
        for(String ingredientID : ingredients){
            ModIngredient ingredient = getIngredientByName(ingredientID);
            deserialized.add(ingredient);
        }
        return deserialized;
    }
    public static boolean isRitualIngredient(ItemStack itemStack){
        return itemStack.is(TagFactory.RITUAL_INGREDIENT);
    }
    public static boolean isKettleIngredient(ItemStack itemStack){
        return itemStack.is(TagFactory.KETTLE_INGREDIENT);
    }
}
