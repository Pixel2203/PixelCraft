package com.example.examplemod.worldgen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> MAGICTREE_PLACED_KEY = registerKey("magic_tree_placed");

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ExampleMod.MODID, name));
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, MAGICTREE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MAGICTREE_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
                        BlockRegistry.MagicSapling.get()));
    }


    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<PlacedFeature> context,
                                                                                          ResourceKey<PlacedFeature> key,
                                                                                          Holder<ConfiguredFeature<?, ?>> featureHolderGetter,
                                                                                          List<PlacementModifier> placementModifiers) {
        context.register(key, new PlacedFeature(featureHolderGetter, List.copyOf(placementModifiers)));
    }
}
