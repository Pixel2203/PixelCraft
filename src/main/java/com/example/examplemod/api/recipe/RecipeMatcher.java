package com.example.examplemod.api.recipe;

import com.example.examplemod.api.distilleryBowl.ModFluids;
import com.example.examplemod.api.recipe.kettle.KettleRecipe;
import com.example.examplemod.item.ItemRegistry;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RecipeMatcher {

    public static Optional<ModRecipe> findMatchingRecipe(RecipeOrigin origin, List<ItemStack> provided) {

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

    public static Optional<RitualRecipe> matchRitualRecipe(List<ItemStack> ingredients) {
        var recipeOpt = findMatchingRecipe(RecipeOrigin.CHALK, ingredients);
        if(recipeOpt.isEmpty()) return Optional.empty();

        if(!(recipeOpt.get() instanceof RitualRecipe recipe)) {
            log.error("RecipeMatcher | matchRitualRecipe | Found Ritual type did not match CHALK");
            return Optional.empty();
        }
        return Optional.of(recipe);
    }

    public static Optional<KettleRecipe> matchKettleRecipe(List<ItemStack> ingredients) {
        var recipeOpt = findMatchingRecipe(RecipeOrigin.KETTLE, ingredients);
        if(recipeOpt.isEmpty()) return Optional.empty();

        if(!(recipeOpt.get() instanceof KettleRecipe recipe)) {
            log.error("RecipeMatcher | matchRitualRecipe | Found Kettle Recipe type did not match {}", RecipeOrigin.KETTLE.name());
            return Optional.empty();
        }
        return Optional.of(recipe);
    }

    public static Optional<BowlRecipe> matchBowlRecipe(@NotNull ModFluids fluid, @NotNull ItemStack ingredient) {
        return BowlRecipeRegistry.getRecipes().stream()
                .filter(bowlRecipe -> bowlRecipe.baseFluid() == fluid)
                .filter(bowlRecipe -> bowlRecipe.in() == ingredient)
                .findFirst();
    }
}
