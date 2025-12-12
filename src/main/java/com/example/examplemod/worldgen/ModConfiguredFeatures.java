package com.example.examplemod.worldgen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.worldgen.tree.CustomTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGICTREE_KEY = registerKey("magic_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_STATUE_CONFIGURED = registerKey("statue_fire_configured");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WATER_STATUE_CONFIGURED = registerKey("statue_water_configured");
    public static final ResourceKey<ConfiguredFeature<?, ?>> EARTH_STATUE_CONFIGURED = registerKey("statue_earth_configured");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AIR_STATUE_CONFIGURED = registerKey("statue_air_configured");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHT_STATUE_CONFIGURED = registerKey("statue_light_configured");


    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ExampleMod.MODID, name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context,
                MAGICTREE_KEY,
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(BlockRegistry.MagicWood.get()),
                    new CustomTrunkPlacer(3, 2, 0),
                    BlockStateProvider.simple(Blocks.ACACIA_LEAVES),
                    new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(3),2),
                    new TwoLayersFeatureSize(1,0,2)
                ).build()
        );

        // --- STATUE CONFIGURED FEATURES ---
        // each configured feature simply places a single block state (the statue)
        register(context, FIRE_STATUE_CONFIGURED, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.FireStatueBlock.get().defaultBlockState()))
        );

        register(context, WATER_STATUE_CONFIGURED, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.WaterStatueBlock.get().defaultBlockState()))
        );

        register(context, EARTH_STATUE_CONFIGURED, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.EarthStatueBlock.get().defaultBlockState()))
        );

        register(context, AIR_STATUE_CONFIGURED, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.AirStatueBlock.get().defaultBlockState()))
        );

        register(context, LIGHT_STATUE_CONFIGURED, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.LightStatueBlock.get().defaultBlockState()))
        );
    }


    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
