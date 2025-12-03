package com.example.examplemod.worldgen.tree;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTrunkPlacer {

    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, ExampleMod.MODID);


    public static final RegistryObject<TrunkPlacerType<CustomTrunkPlacer>> CORE_TRUNK_PLACER =
            TRUNK_PLACER_TYPES.register("core_trunk_placer",
                    () -> new TrunkPlacerType<>(CustomTrunkPlacer.CODEC));


    public static void register(IEventBus bus) {
        TRUNK_PLACER_TYPES.register(bus);
    }
}
