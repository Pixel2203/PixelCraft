package com.example.examplemod.api.recipe.kettle;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.distilleryBowl.ModFluids;
import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.Vial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ChargedSoulLightCrystalKettleRecipe extends KettleRecipe {
    private static LinkedList<ItemStack> createIngredients() {
        LinkedList<ItemStack> ingredients = new LinkedList<>();
        ingredients.offer(Vial.createVialWithType(ModFluids.SHIMMER_ESSENCE));
        ingredients.offer(new ItemStack(ItemRegistry.SOUL_FRAGMENT.get()));
        ingredients.offer(new ItemStack(Items.AMETHYST_SHARD));
        return ingredients;
    }

    public ChargedSoulLightCrystalKettleRecipe() {
        super(createIngredients(), ResultTypes.ITEM);
    }


    @Override
    public @Nullable ItemStack getResult(List<ItemStack> ingredients) {
        ItemStack soulFragment = ingredients.get(ingredients.size() - 2);
        boolean isBound = ModUtils.isBound(soulFragment);
        if(!isBound) return null;

        CompoundTag modTag = soulFragment.getOrCreateTag().getCompound(ExampleMod.MODID);
        ItemStack result = new ItemStack(ItemRegistry.ZIRCON.get());
        CompoundTag tag = result.getOrCreateTag();
        tag.put(ExampleMod.MODID, modTag);
        result.setTag(tag);
        return result;
    }
}
