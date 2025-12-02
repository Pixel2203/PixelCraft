package com.example.examplemod.api.recipe;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Function;

public interface CustomResultBuilders {
    Function<List<ItemStack>, ItemStack> SOUL_LIGHT_BUILDER = (itemStacks -> {
        ItemStack soulFragment = itemStacks.get(itemStacks.size() - 2);
        boolean isBound = ModUtils.isBound(soulFragment);
        if(!isBound) return null;

        CompoundTag modTag = soulFragment.getOrCreateTag().getCompound(ExampleMod.MODID);
        ItemStack result = new ItemStack(ItemRegistry.ZIRCON.get());
        CompoundTag tag = result.getOrCreateTag();
        tag.put(ExampleMod.MODID, modTag);
        result.setTag(tag);
        return result;
    });
}
