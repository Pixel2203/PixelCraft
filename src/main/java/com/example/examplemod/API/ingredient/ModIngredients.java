package com.example.examplemod.API.ingredient;

import net.minecraft.world.item.Items;

public enum ModIngredients {
    BLAZE_ROD(new ModIngredient(Items.BLAZE_ROD, "blaze_rod")),
    GLOWSTONE_DUST(new ModIngredient(Items.GLOWSTONE_DUST, "glowstone_dust"));

    public ModIngredient ingredient;

    ModIngredients(ModIngredient ingredient) {
        this.ingredient = ingredient;
        IngredientAPI.registerIngredient(ingredient);
    }
}
