package com.example.examplemod.worldgen.tree;

import com.example.examplemod.block.BlockRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class CustomTrunkPlacer extends TrunkPlacer {

    public static final Codec<CustomTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return trunkPlacerParts(instance).apply(instance, CustomTrunkPlacer::new);
    });


    public CustomTrunkPlacer(int p_70268_, int p_70269_, int p_70270_) {
        super(p_70268_, p_70269_, p_70270_);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacer.CORE_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> placer,
            RandomSource random,
            int height,
            BlockPos pos,
            TreeConfiguration config
    ) {
        // Wähle zufällige Position für das 'A' (Kernblock)
        int aIndex = random.nextBoolean() ? 1 : 2; // Position A = 1 oder 2

        for (int y = 0; y < height; y++) {
            BlockPos current = pos.above(y);

            // Wenn wir am "A"-Index sind → Kernblock setzen
            if (y == aIndex) {
                placer.accept(current, BlockRegistry.MagicWoodCore.get().defaultBlockState());
            } else {
                placer.accept(current, config.trunkProvider.getState(random, current));
            }
        }

        // Foliage Ansatzpunkt ganz oben
        return List.of(new FoliagePlacer.FoliageAttachment(pos.above(height - 1), 0, false));
    }















}
