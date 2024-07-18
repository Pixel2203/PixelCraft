package com.example.examplemod.api.recipe;

import com.example.examplemod.api.ingredient.ModIngredient;
import com.example.examplemod.api.result.ResultTypes;
import net.minecraftforge.common.util.Lazy;

import java.util.Arrays;
import java.util.StringJoiner;

public class ModRecipe<T> {
    private final Lazy<T> _result;
    private final ModIngredient[] _ingredients;
    private final String _serializedRecipe;
    private final ResultTypes _resultType;

    public ModRecipe(ResultTypes resultType, Lazy<T> result, ModIngredient... ingredients){
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
    public Lazy<T> result(){
        return this._result;
    }
    public ResultTypes resultType(){
        return _resultType;
    }

    private String serializeRecipe(ModIngredient... ingredients){
        StringJoiner recipeJoiner = new StringJoiner(",");
        Arrays.stream(ingredients).map(ModIngredient::id).forEach(recipeJoiner::add);
        return recipeJoiner.toString();
    }
}
