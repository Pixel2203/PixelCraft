package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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

        simpleItemFromTexture(ItemRegistry.HEALING_SCROLL, "sealed_scroll");
        simpleItemFromTexture(ItemRegistry.PROJECTILE_BARRIER_SCROLL,"sealed_scroll");

        simpleItemFromTexture(ItemRegistry.PROTECTION_OF_DEATH_TALISMAN, "necklace");
        simpleItemFromTexture(ItemRegistry.HUNGER_REGENERATION_TALISMAN,"necklace");
        simpleItemFromTexture(ItemRegistry.PROTECTION_OF_FREEZING_TALISMAN,"necklace");

        simplePotion(ItemRegistry.POTION_FLORA,"potion_overlay_2",true);
        simplePotion(ItemRegistry.POTION_FREEZE,"potion_overlay_3",true);
        simplePotion(ItemRegistry.POTION_HUNGER_REGENERATION,"potion_overlay_4",true);
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
                    .texture("layer0", new ResourceLocation("minecraft", "item/" + baseTexture ))
                    .texture("layer1", new ResourceLocation(ExampleMod.MODID,"item/" + textureName));
    }

}
