package com.example.examplemod.event.datagen;


import com.example.examplemod.ExampleMod;
import com.example.examplemod.datagen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        // Loot tables
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput));

        // Block States
        generator.addProvider(event.includeServer(), new ModBlockStateProvider(packOutput,existingFileHelper));

        // Item Models
        generator.addProvider(event.includeServer(), new ModItemModelProvider(packOutput,existingFileHelper));

        // Tags
        ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(), new ModBlockTagGenerator(packOutput,lookupProvider,existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemTagGenerator(packOutput,lookupProvider,blockTagGenerator.contentsGetter(),existingFileHelper));

    }
}
