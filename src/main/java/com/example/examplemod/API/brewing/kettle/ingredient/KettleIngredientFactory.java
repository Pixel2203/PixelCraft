package com.example.examplemod.API.brewing.kettle.ingredient;

import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import net.minecraft.world.item.Items;

public class KettleIngredientFactory {
    public static KettleIngredient BLAZE_ROD = new KettleIngredient(Items.BLAZE_ROD, "blaze_rod");
    public static KettleIngredient GLOWSTONE_DUST = new KettleIngredient(Items.GLOWSTONE_DUST, "glowstone_dust");
}
