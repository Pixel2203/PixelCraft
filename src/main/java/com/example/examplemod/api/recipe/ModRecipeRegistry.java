package com.example.examplemod.api.recipe;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModRecipeRegistry {

    private static final List<ModRecipe> RECIPES = new ArrayList<>();

    public static void register(ModRecipe recipe) {
        log.info("ModRecipeRegistry | register | Registering recipe");
        RECIPES.add(recipe);

    }

    public static List<ModRecipe> getRecipes(RecipeOrigin origin) {
        return RECIPES.stream().filter(recipe -> recipe.getOrigin() == origin).toList();
    }
}
