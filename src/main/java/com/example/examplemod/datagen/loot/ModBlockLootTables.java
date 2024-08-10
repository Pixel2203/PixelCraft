package com.example.examplemod.datagen.loot;

import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistry.LeafCloverBlock.get());
        this.dropSelf(BlockRegistry.ZirconBlock.get());
        this.dropSelf(BlockRegistry.CauldronCustomBlock.get());
        this.dropNothing(BlockRegistry.GoldenChalkBlock.get());
        this.dropNothing(BlockRegistry.WhiteChalkBlock.get());
        this.dropOther(BlockRegistry.StatueStoneBase.get(), () -> ItemRegistry.STATUE_STONE.get().asItem());
//        this.dropSelf(BlockRegistry.StatuePolishedTuffBlock.get());
        this.dropNothing(BlockRegistry.StatueStoneTop.get());
        this.dropNothing(BlockRegistry.StatuePowerfulStoneMiddle.get());
        this.dropNothing(BlockRegistry.StatuePowerfulStoneBase.get());
        this.dropNothing(BlockRegistry.StatuePowerfulStoneTop.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
    private void dropNothing(Block block){
        this.dropOther(block, Items.AIR);
    }
}
