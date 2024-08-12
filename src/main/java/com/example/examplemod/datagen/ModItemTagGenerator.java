package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.tag.TagFactory;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, ExampleMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(TagFactory.INGREDIENT)
                .add(Items.PURPLE_DYE)
                .add(Items.GREEN_DYE)
                .add(Items.BLAZE_ROD)
                .add(Items.GLOWSTONE_DUST)
                .add(Items.REDSTONE);

        this.tag(TagFactory.KETTLE_ALLOWED_FLUID_ITEMS)
                .add(Items.WATER_BUCKET)
                .add(Items.POTION);
    }
}
