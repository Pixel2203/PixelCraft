package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.SoulFlower;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;


public class ModBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation CHALK_TEMPLATE = new ResourceLocation(ExampleMod.MODID, "chalk_template");

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExampleMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlockRegistry.ZirconBlock);



        registerBlockStateWithBlock(BlockRegistry.LeafCloverBlock,
                new ConfiguredModel(models()
                        .cross(BlockRegistry.LeafCloverBlock.getId().getPath(),
                                new ResourceLocation(ExampleMod.MODID,"block/" + BlockRegistry.LeafCloverBlock.getId().getPath()))
                        .renderType(new ResourceLocation("cutout")))
        );

        this.registerSoulFlowerStates();


        registerBlockStateWithBlock(BlockRegistry.GoldenChalkBlock,
                new ConfiguredModel(chalkBlock(BlockRegistry.GoldenChalkBlock.getId().getPath())));


        registerBlockStateWithBlock(BlockRegistry.WhiteChalkBlock,
                new ConfiguredModel(chalkBlock("white_chalk_variant1")),
                new ConfiguredModel(chalkBlock("white_chalk_variant2")),
                new ConfiguredModel(chalkBlock("white_chalk_variant3")));


        // Cauldron
        for(int i = 1; i <= 3; i++){
            cauldronCustomModel(i, true, false);
            cauldronCustomModel(i, false, false);
            if(i == 3){
                cauldronCustomModel(i,true,true);
            }
        }

        simpleItemWithCustomBlock(
                BlockRegistry.CauldronCustomBlock,
                new ResourceLocation(ExampleMod.MODID,"cauldron_custom_template"),
                Map.of("outside", "cauldron_custom" , "inside","cauldron_custom_inner", "particle", "cauldron_custom"));

        simpleBlock(BlockRegistry.FogBlock.get(),
                new ConfiguredModel(models().cubeAll("fog_light", modLoc("block/fog_1"))),
                new ConfiguredModel(models().cubeAll("fog_medium", modLoc("block/fog_2"))),
                new ConfiguredModel(models().cubeAll("fog_strong", modLoc("block/fog_3"))),
                new ConfiguredModel(models().cubeAll("fog_extreme", modLoc("block/fog_4"))));

    }
    private VariantBlockStateBuilder registerBlockStateWithBlock(RegistryObject<Block> blockRegistryObject, ConfiguredModel ... models){
        return getVariantBuilder(blockRegistryObject.get()).partialState().setModels(models);
    }

    private void registerBooleanBlockState(RegistryObject<Block> blockRegistryObject, Property<Boolean> property,
                                           Map<Boolean, ConfiguredModel[]> stateModels) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockRegistryObject.get());

        stateModels.forEach((value, models) -> {
            builder.partialState()
                    .with(property, value)
                    .setModels(models);
        });
    }

    // ModBlockStateProvider.java

    private void registerSoulFlowerStates() {

        // 1. Definiere die benötigten Modelle einmal
        ModelBuilder<BlockModelBuilder> soulFlowerDayModel =
                models().cross("soul_flower_day", modLoc("block/soul_flower_day")).renderType(new ResourceLocation("cutout"));
        ModelBuilder<BlockModelBuilder> soulFlowerNightModel =
                models().cross("soul_flower_night", modLoc("block/soul_flower_night")).renderType(new ResourceLocation("cutout"));
        ModelBuilder<BlockModelBuilder> soulFlowerDewModel =
                models().cross("soul_flower_dew", modLoc("block/soul_flower_dew")).renderType(new ResourceLocation("cutout"));

        // 2. Erstelle den Builder für die SoulFlower
        VariantBlockStateBuilder builder = getVariantBuilder(BlockRegistry.SoulFlower.get());

        // 3. Iteriere über alle DEW_COUNT Werte (0 bis 3)
        for (int dewCount = 0; dewCount <= 3; dewCount++) {

            // ZUSTAND A: Tag (NIGHT_ACTIVE=false)
            builder.partialState()
                    .with(SoulFlower.NIGHT_ACTIVE, false)
                    .with(SoulFlower.DEW_COUNT, dewCount)
                    // Immer das Tag-Modell verwenden, unabhängig vom Tau-Zähler
                    .setModels(new ConfiguredModel(soulFlowerDayModel));

            // ZUSTAND B: Nacht (NIGHT_ACTIVE=true)
            builder.partialState()
                    .with(SoulFlower.NIGHT_ACTIVE, true)
                    .with(SoulFlower.DEW_COUNT, dewCount)
                    .setModels(
                            // Wenn dewCount 0 ist, verwende das leere Nacht-Modell
                            dewCount == 0
                                    ? new ConfiguredModel(soulFlowerNightModel)
                                    // Wenn dewCount > 0 ist, verwende das Tau-Modell
                                    : new ConfiguredModel(soulFlowerDewModel)
                    );
        }
    }



    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private ModelBuilder<BlockModelBuilder> chalkBlock(RegistryObject<Block> block, String name) {
        return simpleCustomBlock(block.getId().getPath(), CHALK_TEMPLATE, Map.of("1", name));
    }

    private ModelBuilder<BlockModelBuilder> chalkBlock(String variantName) {
        return simpleCustomBlock(variantName, CHALK_TEMPLATE, Map.of("1",variantName)).texture("particle", new ResourceLocation(ExampleMod.MODID, "block/" + variantName));
    }

    private ModelBuilder<BlockModelBuilder> simpleItemWithCustomBlock(RegistryObject<Block> block, ResourceLocation parent, Map<String, String> textures){
        itemModels().withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExampleMod.MODID, "item/" + block.getId().getPath()));
        return  simpleCustomBlock(block.getId().getPath(), parent,textures);
    }
    private  ModelBuilder<BlockModelBuilder> simpleCustomBlock(String name , ResourceLocation parent, Map<String, String> textures){
        ModelBuilder<BlockModelBuilder> builder = models().withExistingParent(name, parent );
        textures.keySet().forEach(key -> {
            builder.texture(key, new ResourceLocation(ExampleMod.MODID, "block/" + textures.get(key)));
        });
        return builder;
    }


    private void cauldronCustomModel(int level, boolean mixture, boolean boiling){
        final String prefix = "cauldron_custom";
        final String levelModifier = "_level" + level;

        final String templateName = prefix + "_template" + levelModifier;
        String name = prefix + levelModifier;

        name += mixture ? "_mixture" : "_default";
        if(boiling){name += "_boiling";}
        final String contentTexture = mixture ? boiling ? "mixture_boiling" : "mixture_still" : "water_default";
        simpleCustomBlock(name, new ResourceLocation(ExampleMod.MODID, "block/" + templateName), Map.of("content", contentTexture) );
    }

}
