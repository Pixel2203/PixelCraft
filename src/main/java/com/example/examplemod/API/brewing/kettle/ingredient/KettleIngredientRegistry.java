package com.example.examplemod.API.brewing.kettle.ingredient;

import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.tag.TagRegistry;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Optional;

public class KettleIngredientRegistry {
    private static final HashMap<Item,String> REGISTERED_INGREDIENTS = new HashMap<>();
    public static final KettleIngredient BLAZE_ROD = registerIngredient(KettleIngredientFactory.BLAZE_ROD);
    private static final KettleIngredient GLOWSTONE = registerIngredient(KettleIngredientFactory.GLOWSTONE_DUST);



    private static KettleIngredient registerIngredient(KettleIngredient ingredient1){
        REGISTERED_INGREDIENTS.put(ingredient1.item(),ingredient1.id());
        return ingredient1;
    }

    public static KettleIngredient getIngredientByName(String name){
        if(!REGISTERED_INGREDIENTS.containsValue(name)){
           return null;
        }
        for(Item key : REGISTERED_INGREDIENTS.keySet()){
            if(REGISTERED_INGREDIENTS.get(key).equalsIgnoreCase(name)){
                return new KettleIngredient(key,name);
            }
        }
        return null;
    }
    public static Optional<KettleIngredient> getIngredientByItem(Item item){
        String found = REGISTERED_INGREDIENTS.get(item);
        if(StringUtil.isNullOrEmpty(found)){
            return Optional.empty();
        }
        return Optional.of(new KettleIngredient(item,REGISTERED_INGREDIENTS.get(item)));
    }
    public static boolean isIngredient(ItemStack itemStack){
        return itemStack.is(TagRegistry.KETTLE_INGREDIENTS);
    }
}
