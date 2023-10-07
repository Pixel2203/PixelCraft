package com.example.examplemod.API.brewing.kettle.recipe;

import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredientFactory;
import com.example.examplemod.API.brewing.kettle.ingredient.KettleIngredientRegistry;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class KettleRecipeRegistry {
    public static List<KettleRecipe> KETTLE_RECIPES = new ArrayList<>();

    private static KettleRecipe TestKettleRecipe = registerRecipe(Items.DIAMOND,
            KettleIngredientFactory.GLOWSTONE_DUST,KettleIngredientFactory.BLAZE_ROD);

    private static KettleRecipe TestKettleRecipe2 = registerRecipe(Items.DIRT,
            KettleIngredientFactory.BLAZE_ROD, KettleIngredientFactory.GLOWSTONE_DUST);






    private static KettleRecipe registerRecipe(Item result, KettleIngredient... ingredients){
        String kettleRecipeString = serializeRecipeIngredientList(ingredients);
        KettleRecipe kettleRecipe = new KettleRecipe(result,ingredients);
        KETTLE_RECIPES.add(kettleRecipe);
        return kettleRecipe;
    }
    public static String serializeRecipeIngredientList(KettleIngredient... ingredients){
        String recipe = "";
        for(KettleIngredient ingredient: ingredients){
            if(StringUtil.isNullOrEmpty(recipe)){
                recipe = ingredient.id();
                continue;
            }
            recipe += "," + ingredient;
        }
        return recipe;
    }
    public static KettleIngredient[]  deserializeRecipeIngredientList (String recipe){
        if(recipe == null){return null;}
        String[] ingredientsSeperated = recipe.split(",");
        KettleIngredient[] ingredients = new KettleIngredient[ingredientsSeperated.length];
        for(int i = 0; i < ingredientsSeperated.length; i++){
            KettleIngredient ingredient1 = KettleIngredientRegistry.getIngredientByName(ingredientsSeperated[i]);
            if(ingredient1 == null){
                return null;
            }
            ingredients[i] = ingredient1;
        }
        return ingredients;
    }
    public static boolean isPartOfOrCompleteRecipe(KettleIngredient ... ingredients){
        if(ingredients == null){
            return false;
        }
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
        return false;
    }
}
