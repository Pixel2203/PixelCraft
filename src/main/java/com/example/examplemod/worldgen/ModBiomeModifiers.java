package com.example.examplemod.worldgen;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {


    public static final ResourceKey<BiomeModifier> MAGICTREE_BIOME_MOD_KEY = registerKey("add_magic_tree");
    public static final ResourceKey<BiomeModifier> STATUES_PLAINS_BIOME_MOD = registerKey("add_statues_to_plains");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(MAGICTREE_BIOME_MOD_KEY, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MAGICTREE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));



        // --- STATUES: add all statue placed features to plains ---
        HolderSet<PlacedFeature> statueSet = HolderSet.direct(
                placedFeatures.getOrThrow(ModPlacedFeatures.FIRE_STATUE_PLACED),
                placedFeatures.getOrThrow(ModPlacedFeatures.WATER_STATUE_PLACED),
                placedFeatures.getOrThrow(ModPlacedFeatures.EARTH_STATUE_PLACED),
                placedFeatures.getOrThrow(ModPlacedFeatures.AIR_STATUE_PLACED),
                placedFeatures.getOrThrow(ModPlacedFeatures.LIGHT_STATUE_PLACED)
        );

        context.register(STATUES_PLAINS_BIOME_MOD, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_PLAINS),
                statueSet,
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(ExampleMod.MODID, name));
    }
}
