package com.example.examplemod.API.kettle.records;

import com.example.examplemod.API.kettle.result.ResultTypes;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;

public class KettleRecipe {
    private ItemStack _result;
    private KettleIngredient[] _ingredients;
    private String _serializedRecipe;
    private ResultTypes _resultType;

    public KettleRecipe(ResultTypes resultType,ItemStack result, KettleIngredient ... ingredients){
        this._result = result;
        this._ingredients = ingredients;
        this._serializedRecipe = this.serializeRecipe(ingredients);
        this._resultType = resultType;
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
    public ResultTypes resultType(){
        return _resultType;
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
