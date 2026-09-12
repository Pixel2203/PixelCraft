package com.example.examplemod.api.recipe.kettle;

import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.api.result.ResultTypes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public abstract class KettleRecipe extends ModRecipe {
    @Getter
    private final ResultTypes resultType;

    public KettleRecipe(LinkedList<ItemStack> ingredients, ResultTypes resultType) {
        super(ingredients);
        this.resultType = resultType;
    }

    @NotNull
    public ItemStack getResult(List<ItemStack> ingredients) {
        log.warn("KettleRecipe | getResult | Not getting any result, did you forget to override this method?");
        return ItemStack.EMPTY;

    }




    @Override
    public RecipeOrigin getOrigin() {
        return RecipeOrigin.KETTLE;
    }
}
