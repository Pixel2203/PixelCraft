package com.example.examplemod.api.recipe;

import com.example.examplemod.api.circleMagic.rituals.ModRituals;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RitualRecipe extends ModRecipe{
    @Getter
    private final ModRituals resultingRitual;

    public RitualRecipe(List<ItemStack> ingredients, ModRituals resultingRitual) {
        super(ingredients);
        this.resultingRitual = resultingRitual;
    }


    @Override
    public RecipeOrigin getOrigin() {
        return RecipeOrigin.CHALK;
    }
}
