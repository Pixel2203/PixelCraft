package com.example.examplemod.API.brewing.kettle.records;

import com.example.examplemod.API.brewing.kettle.KettleAPI;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class KettleRecipe {
    private ItemStack _result;
    private KettleIngredient[] _ingredients;
    private String _serializedRecipe;

    public KettleRecipe(ItemStack result, KettleIngredient ... ingredients){
        this._result = result;
        this._ingredients = ingredients;
        this._serializedRecipe = this.serializeRecipe(ingredients);
    }
    public String serializedRecipe(){
        return _serializedRecipe;
    }
    public KettleIngredient[] ingredients(){
        return _ingredients;
    }
    public ItemStack result(){
        return this._result;
    }
    private String serializeRecipe(KettleIngredient ... ingredients){
        String recipe = "";
        for(KettleIngredient ingredient: ingredients){
            if(StringUtil.isNullOrEmpty(recipe)){
                recipe = ingredient.id();
                continue;
            }
            recipe += "," + ingredient.id();
        }
        return recipe;
    }
}
