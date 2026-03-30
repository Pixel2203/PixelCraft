package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.runes.RuneType;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import oshi.util.tuples.Pair;

import java.util.Locale;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExampleMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemRegistry.BLIZZARD_SWORD);
        simpleItem(ItemRegistry.GOLDEN_CHALK);
        simpleItem(ItemRegistry.WHITE_CHALK);
        simpleItem(ItemRegistry.ZIRCON);
        simpleItem(ItemRegistry.LIMESTONE);
        simpleItem(ItemRegistry.GLIMMER_GRAS_SEED);
        simpleItem(ItemRegistry.SOUL_FRAGMENT);
        simpleItem(ItemRegistry.GLIMMER_LEAF);
        simpleItemFromTexture(ItemRegistry.RUNE_PAGE, "rune_page_1");

        simpleItemFromTexture(ItemRegistry.HEALING_SCROLL, "scrolls/sealed_scroll");
        simpleItemFromTexture(ItemRegistry.PROJECTILE_BARRIER_SCROLL,"scrolls/sealed_scroll");
        simpleItemFromTexture(ItemRegistry.CONFUSION_SCROLL,  "scrolls/sealed_scroll");

        simpleItemFromTexture(ItemRegistry.PROTECTION_OF_DEATH_TALISMAN, "necklaces/necklace_1");
        simpleItemFromTexture(ItemRegistry.HUNGER_REGENERATION_TALISMAN,"necklaces/necklace_3");
        simpleItemFromTexture(ItemRegistry.PROTECTION_OF_FREEZING_TALISMAN,"necklaces/necklace_4");
        simpleItemFromTexture(ItemRegistry.SOULBOUND_TALISMAN, "necklaces/necklace_7");
        simpleItemFromTexture(ItemRegistry.UNDEAD_PROTECTION_TALISMAN, "necklaces/necklace_6");

        simplePotion(ItemRegistry.POTION_FLORA,"potion_overlay_2",true);
        simplePotion(ItemRegistry.POTION_FREEZE,"potion_overlay_3",true);
        simplePotion(ItemRegistry.POTION_HUNGER_REGENERATION,"potion_overlay_4",true);

        simpleTextureForPredicate("item/crystals/crystal_1", "crystals/crystal_1");
        simpleTextureForPredicate("item/crystals/crystal_2", "crystals/crystal_2");
        simpleTextureForPredicate("item/crystals/crystal_3", "crystals/crystal_3");
        simpleTextureForPredicate("item/crystals/crystal_4", "crystals/crystal_4");


        this.generateWoodenRunePredicates();
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExampleMod.MODID, "item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleItemFromTexture(RegistryObject<Item> item, String texture){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExampleMod.MODID, "item/" + texture));
    }
    private ItemModelBuilder simplePotion(RegistryObject<Item> item, String textureName, boolean throwable){
        String baseTexture = throwable ? "splash_potion" : "potion";
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated"))
                    .texture("layer0", mcLoc("item/" + baseTexture ))
                    .texture("layer1", new ResourceLocation(ExampleMod.MODID,"item/potions/" + textureName));
    }

    private ItemModelBuilder simpleTextureForPredicate(String state, String texture){
        return withExistingParent(state,
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ExampleMod.MODID, "item/" + texture));
    }

    private void generateWoodenRunePredicates() {
        String mainModel = ItemRegistry.WOODEN_RUNE.getId().getPath();

        // Basis Holzrune ohne Overlay (layer0)
        ItemModelBuilder parent = withExistingParent(mainModel,
                new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(ExampleMod.MODID, "item/wooden_rune_base"));

        // Für jede RuneType einen Override erzeugen
        for (RuneType type : RuneType.values()) {
            int ord = type.ordinal();
            String modelName = mainModel + "_" + ord;

            // Child Model generieren
            withExistingParent(modelName, new ResourceLocation("item/generated"))
                    .texture("layer0", new ResourceLocation(ExampleMod.MODID, "item/wooden_rune_base"))
                    .texture("layer1", new ResourceLocation(ExampleMod.MODID, "item/runes/rune_" + ord));

            // Predicate hinzufügen
            parent.override()
                    .predicate(new ResourceLocation(ExampleMod.MODID, "rune_type"), ord)
                    .model(new ModelFile.UncheckedModelFile(ExampleMod.MODID + ":item/" + modelName));
        }
    }








}
