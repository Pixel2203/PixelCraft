package com.example.examplemod.API.kettle.ingredient;

import com.example.examplemod.API.kettle.KettleAPI;
import com.example.examplemod.API.kettle.records.KettleIngredient;
import net.minecraft.world.item.Items;

public enum KettleIngredients {
    BLAZE_ROD(new KettleIngredient(Items.BLAZE_ROD, "blaze_rod")),
    GLOWSTONE_DUST(new KettleIngredient(Items.GLOWSTONE_DUST, "glowstone_dust"));

    public KettleIngredient ingredient;

    KettleIngredients(KettleIngredient ingredient) {
        this.ingredient = ingredient;
        KettleAPI.registerIngredient(ingredient);
    }
}
