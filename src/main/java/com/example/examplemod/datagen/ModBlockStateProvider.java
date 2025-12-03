package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.DistilleryBowl;
import com.example.examplemod.block.blocks.GlimmerGras;
import com.example.examplemod.block.blocks.SoulFlower;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;


public class ModBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation CHALK_TEMPLATE = new ResourceLocation(ExampleMod.MODID, "block/templates/chalk_template");

    private static final String DISTILLERY_BOWL_TEMPLATE_PATH = "block/templates/distillery_bowl/";
    private static final String CAULDRON_TEMPLATE_PATH = "block/templates/cauldron_custom/";
    private static final String RUNE_PAGE_HOLDER_TEMPLATE_PATH = "block/templates/rune_page_holder/";
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExampleMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlockRegistry.ZirconBlock);
        blockWithItem(BlockRegistry.LimestoneBlock);
        simpleBlock(BlockRegistry.InvisibleLightBlock.get(), new ConfiguredModel(models().cubeAll(BlockRegistry.InvisibleLightBlock.getId().getPath(), modLoc("block/invisible_light")).renderType("minecraft:translucent")));
        this.simpleCrossBlockWithItem(BlockRegistry.LeafCloverBlock);

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
                new ResourceLocation(ExampleMod.MODID,CAULDRON_TEMPLATE_PATH + "cauldron_custom_template"),
                Map.of("outside", "cauldron_custom" , "inside","cauldron_custom_inner", "particle", "cauldron_custom"));

        simpleBlock(BlockRegistry.FogBlock.get(),
                new ConfiguredModel(models().cubeAll("fog_light", modLoc("block/fog_1")).renderType(mcLoc("translucent"))),
                new ConfiguredModel(models().cubeAll("fog_medium", modLoc("block/fog_2")).renderType(mcLoc("translucent"))),
                new ConfiguredModel(models().cubeAll("fog_strong", modLoc("block/fog_3")).renderType(mcLoc("translucent"))),
                new ConfiguredModel(models().cubeAll("fog_extreme", modLoc("block/fog_4")).renderType(mcLoc("translucent"))));


        registerIntegerBlockState(BlockRegistry.GlimmerGrasBlock, GlimmerGras.AGE, Map.of(
                0, new ConfiguredModel[]{ new ConfiguredModel(models().cross("glimmer_gras_0", modLoc("block/" + "glimmer_gras_0")).renderType(mcLoc("cutout"))) },
                1, new ConfiguredModel[]{ new ConfiguredModel(models().cross("glimmer_gras_1", modLoc("block/" + "glimmer_gras_1")).renderType(mcLoc("cutout"))) },
                2, new ConfiguredModel[]{ new ConfiguredModel(models().cross("glimmer_gras_2", modLoc("block/" + "glimmer_gras_2")).renderType(mcLoc("cutout"))) }
        ));

        registerBooleanBlockState(BlockRegistry.DistilleryBowlBlock, DistilleryBowl.isFilled, Map.of(
                false, new ConfiguredModel[] {
                        new ConfiguredModel(models().withExistingParent("distillery_bowl_empty", modLoc(DISTILLERY_BOWL_TEMPLATE_PATH +"distillery_bowl_empty_template")).renderType(mcLoc("translucent")))
                },
                true, new ConfiguredModel[] {
                        new ConfiguredModel(models().withExistingParent( "distillery_bowl_water", modLoc(DISTILLERY_BOWL_TEMPLATE_PATH + "distillery_bowl_filled_template")).renderType(mcLoc("translucent")).texture("water_texture", mcLoc("block/water_still")))
                }
        ));

        simpleBlockWithItem(BlockRegistry.MagicWood.get(), models().cubeAll("magic_wood", modLoc("block/magic_wood")));
        simpleBlockWithItem(BlockRegistry.MagicWoodCore.get(), models().cubeAll("magic_wood_core", modLoc("block/magic_wood_core")));



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
    private void registerIntegerBlockState(RegistryObject<Block> blockRegistryObject, Property<Integer> property,
                                           Map<Integer, ConfiguredModel[]> stateModels) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockRegistryObject.get());

        stateModels.forEach((value, models) -> {
            builder.partialState()
                    .with(property, value)
                    .setModels(models);
        });
         }

    // ModBlockStateProvider.java

    private void registerSoulFlowerStates() {
        String blockName = BlockRegistry.SoulFlower.getId().getPath();
        ModelBuilder<BlockModelBuilder> dayModel = models().cross(blockName + "_day", modLoc("block/soul_flower_day")).renderType(mcLoc("cutout"));
        ModelBuilder<BlockModelBuilder> nightModel = models().cross(blockName + "_night", modLoc("block/soul_flower_night")).renderType(mcLoc("cutout"));
        ModelBuilder<BlockModelBuilder> dewModel = models().cross(blockName + "_dew", modLoc("block/soul_flower_dew")).renderType(mcLoc("cutout"));

        VariantBlockStateBuilder builder = getVariantBuilder(BlockRegistry.SoulFlower.get());

        for (int dew = 0; dew <= 3; dew++) {
            // Tag
            builder.partialState()
                    .with(SoulFlower.NIGHT_ACTIVE, false)
                    .with(SoulFlower.DEW_COUNT, dew)
                    .setModels(new ConfiguredModel(dayModel));

            // Nacht
            builder.partialState()
                    .with(SoulFlower.NIGHT_ACTIVE, true)
                    .with(SoulFlower.DEW_COUNT, dew)
                    .setModels(dew == 0 ? new ConfiguredModel(nightModel) : new ConfiguredModel(dewModel));
        }
        this.itemModels().withExistingParent(blockName , "item/generated").texture("layer0", new ResourceLocation(ExampleMod.MODID, "block/" + blockName+"_day"));

    }




    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
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
    private ModelBuilder<BlockModelBuilder> simpleCustomBlock(String name , ResourceLocation parent, Map<String, String> textures){
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
        simpleCustomBlock(name, new ResourceLocation(ExampleMod.MODID, CAULDRON_TEMPLATE_PATH + templateName), Map.of("content", contentTexture) );
    }

    private void simpleCrossBlockWithItem(RegistryObject<Block> block) {
        String blockName = block.getId().getPath();
        registerBlockStateWithBlock(block, new ConfiguredModel(models().cross(blockName, modLoc("block/" + blockName)).renderType(mcLoc("cutout"))));
        this.simpleBlockItem(block, blockName);
    }

    private void simpleBlockItem(RegistryObject<Block> block, String textureName) {
        this.itemModels().withExistingParent(block.getId().getPath() , "item/generated").texture("layer0", modLoc("block/" + textureName));

    }

    private void saplingBlock(RegistryObject<Block> block) {
        String blockName = block.getId().getPath();
        registerBlockStateWithBlock(block, new ConfiguredModel(models().cross(blockName, modLoc("block/" + blockName)).renderType(mcLoc("cutout"))));

    }
}
