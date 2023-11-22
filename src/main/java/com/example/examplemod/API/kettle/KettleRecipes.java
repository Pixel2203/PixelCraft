package com.example.examplemod.API.kettle;

import com.example.examplemod.API.ingredient.ModIngredients;
import com.example.examplemod.API.recipe.ModRecipe;
import com.example.examplemod.API.result.ModResults;
import com.example.examplemod.API.result.ResultTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class KettleRecipes {

    public static ModRecipe TestKettleRecipe = new ModRecipe(ResultTypes.ITEM, ModResults.DIAMONDS_5, ModIngredients.BLAZE_ROD, ModIngredients.GLOWSTONE_DUST);
    public static ModRecipe TestKettleRecipe2 = new ModRecipe(ResultTypes.POTION, ModResults.DIARRHEA_POTION, ModIngredients.GLOWSTONE_DUST, ModIngredients.BLAZE_ROD);
    public static ModRecipe FloraPotionRecipe = new ModRecipe(ResultTypes.POTION, ModResults.FLORA_POTION_LEVEL3, ModIngredients.GLOWSTONE_DUST, ModIngredients.GLOWSTONE_DUST, ModIngredients.BLAZE_ROD);




    private static Potion FireResistancePotionPotion = new Potion(new MobEffectInstance(MobEffects.FIRE_RESISTANCE));




}
