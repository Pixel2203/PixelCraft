package com.example.examplemod.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MODID;
import static com.example.examplemod.item.ItemFactory.*;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> EXAMPLE =
            ITEMS.register("example_item", () -> ExampleItem);
    public static final RegistryObject<Item> ZIRCON =
            ITEMS.register("zircon", () -> ZirconItem);
    public static final RegistryObject<Item> EXAMPLE_POTION =
            ITEMS.register("potion_diarrhea", () -> ExamplePotion);
    public static final RegistryObject<Item> FLORAPOTION =
            ITEMS.register("potion_flora" , () -> FloraPotion);
    public static final RegistryObject<Item> FREEZEPOTION =
            ITEMS.register("potion_freeze" , () -> FreezePotion);
    public static final RegistryObject<Item> WHITECHALK =
            ITEMS.register("white_chalk", () -> WhiteChalkItem);
    public static final RegistryObject<Item> GOLDENCHALK =
            ITEMS.register("golden_chalk", () -> GoldenChalkItem);
    // BlockItems



    public static void registerItems(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
