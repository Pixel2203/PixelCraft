package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, ExampleMod.MODID);
    }

    @Override
    protected void start() {
        add("glimmer_gras_seed_from_gras", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                LootItemRandomChanceCondition.randomChance(0.125f).build()
        }, ItemRegistry.GLIMMER_GRAS_SEED.get()));

        add("soul_fragment_from_zombie", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("entities/zombie")).build(),
                LootItemRandomChanceCondition.randomChance(0.125f).build()
        }, ItemRegistry.SOUL_FRAGMENT.get()));
        add("soul_fragment_from_skeleton", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("entities/skeleton")).build(),
                LootItemRandomChanceCondition.randomChance(0.125f).build()
        }, ItemRegistry.SOUL_FRAGMENT.get()));
    }
}
