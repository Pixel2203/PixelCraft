package com.example.examplemod.API.kettle.recipe;

import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.result.ResultTypes;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;

public class ModRecipe {
    private ItemStack _result;
    private ModIngredient[] _ingredients;
    private String _serializedRecipe;
    private ResultTypes _resultType;

    public ModRecipe(ResultTypes resultType, ItemStack result, ModIngredient... ingredients){
        this._result = result;
        this._ingredients = ingredients;
        this._serializedRecipe = this.serializeRecipe(ingredients);
        this._resultType = resultType;
    }
    public String serializedRecipe(){
        return _serializedRecipe;
    }
    public ModIngredient[] ingredients(){
        return _ingredients;
    }
    public ItemStack result(){
        return this._result;
    }
    public ResultTypes resultType(){
        return _resultType;
    }
    private String serializeRecipe(ModIngredient... ingredients){
        String recipe = "";
        for(ModIngredient ingredient: ingredients){
            if(StringUtil.isNullOrEmpty(recipe)){
                recipe = ingredient.id();
                continue;
            }
            recipe += "," + ingredient.id();
        }
        return recipe;
    }
}
