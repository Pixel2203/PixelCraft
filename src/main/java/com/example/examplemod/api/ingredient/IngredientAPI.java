package com.example.examplemod.api.ingredient;

import com.example.examplemod.tag.TagFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.annotation.concurrent.Immutable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientAPI {
    /**
     * Returns an Immutable List of the deserialized Ingredients
     * Immutable --> List cannot be changed
     * @param serializedIngredients
     * @return
     */
    @Immutable
    public static @NotNull List<Item> deserializeIngredientList(String serializedIngredients){
        return Arrays.stream(serializedIngredients.split(","))
                .map(ResourceLocation::new)
                .map(ForgeRegistries.ITEMS::getValue)
                .toList();
    }
    public static @NotNull String serializeIngredients(List<Item> ingredients){
        StringJoiner joiner = new StringJoiner(",");
        ingredients.stream()
                .map(ForgeRegistries.ITEMS::getKey)
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .forEach(joiner::add);
        return joiner.toString();
    }
    public static boolean isRitualIngredient(ItemStack itemStack){
        return itemStack.is(TagFactory.RITUAL_INGREDIENT);
    }
    public static boolean isKettleIngredient(ItemStack itemStack){
        return itemStack.is(TagFactory.KETTLE_INGREDIENT);
    }
}
