package com.example.examplemod.tag;

import com.example.examplemod.ExampleMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagFactory {
    public static final TagKey<Item> KETTLE_ALLOWED_FLUID_ITEMS = tag("kettle_allowed_fluid_items");
    public static final TagKey<Item> INGREDIENT = tag("ingredient");
    public static final TagKey<Block> VIALABLE_BLOCKS = blockTag("vialable_blocks");

    private static TagKey<Item> tag(String name){
        return ItemTags.create(new ResourceLocation(ExampleMod.MODID,name));
    }

    private static TagKey<Block> blockTag(String name){
        return BlockTags.create(new ResourceLocation(ExampleMod.MODID,name));
    }
}
