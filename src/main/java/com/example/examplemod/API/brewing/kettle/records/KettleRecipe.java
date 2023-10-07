package com.example.examplemod.API.brewing.kettle.records;

import net.minecraft.world.item.Item;

public record KettleRecipe(Item result, KettleIngredient ... ingredients) {
}
