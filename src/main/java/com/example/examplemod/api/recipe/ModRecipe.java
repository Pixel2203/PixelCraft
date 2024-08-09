package com.example.examplemod.api.recipe;

import com.example.examplemod.api.result.ResultTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class ModRecipe<T> {
    private final Lazy<T> _result;
    private final Item[] _ingredients;
    private final String _serializedRecipe;
    private final ResultTypes _resultType;

    public ModRecipe(ResultTypes resultType, Lazy<T> result, Item... ingredients){
        this._result = result;
        this._ingredients = ingredients;
        this._serializedRecipe = this.serializeRecipe(ingredients);
        this._resultType = resultType;
    }
    public String serializedRecipe(){
        return _serializedRecipe;
    }
    public Item[] ingredients(){
        return _ingredients;
    }
    public Lazy<T> result(){
        return this._result;
    }
    public ResultTypes resultType(){
        return _resultType;
    }

    private String serializeRecipe(Item... ingredients){
        StringJoiner recipeJoiner = new StringJoiner(",");
        Arrays.stream(ingredients)
                .map(ForgeRegistries.ITEMS::getKey)
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .forEach(recipeJoiner::add);
        return recipeJoiner.toString();
    }
}
