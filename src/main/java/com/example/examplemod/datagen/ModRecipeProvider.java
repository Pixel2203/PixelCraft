package com.example.examplemod.datagen;

import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        //ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.)
    }
}
