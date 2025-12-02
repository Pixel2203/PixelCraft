package com.example.examplemod.api.recipe;

import com.example.examplemod.item.ItemRegistry;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeMatcher {

    public static Optional<ModRecipe<?>> findMatchingRecipe(RecipeOrigin origin, List<ItemStack> provided) {

        var recipes = ModRecipeRegistry.getRecipes(origin);
        outer:
        for (var recipe : recipes) {

            if (recipe.getIngredients().size() != provided.size()) continue;

            // Wir kopieren beide Listen und vergleichen die Stacks 1:1
            var target = new ArrayList<>(recipe.getIngredients());
            var offered = new ArrayList<>(provided);

            for(int i = 0; i < target.size(); i++) {
                // Ignoriere Seelenfragmente
                if(target.get(i).is(ItemRegistry.SOUL_FRAGMENT.get()) && ItemStack.isSameItem(target.get(i), provided.get(i))) continue;
                if(!ItemStack.isSameItemSameTags(target.get(i), offered.get(i))) continue outer;
            }

            return Optional.of(recipe);
        }
        return Optional.empty();

    }
}
