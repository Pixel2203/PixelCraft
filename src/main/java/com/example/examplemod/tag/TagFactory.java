package com.example.examplemod.tag;

import com.example.examplemod.ExampleMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagFactory {
    public static final TagKey<Item> KETTLE_ALLOWED_FLUID_ITEMS = tag("kettle_allowed_fluid_items");
    public static final TagKey<Item> INGREDIENT = tag("ingredient");
    private static TagKey<Item> tag(String name){
        return ItemTags.create(new ResourceLocation(ExampleMod.MODID,name));
    }
}
