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

    private static final RegistryObject<Item> EXAMPLE =
            ITEMS.register("example_item", () -> ExampleItem);
    private static final RegistryObject<Item> ZIRCON =
            ITEMS.register("zircon", () -> ZirconItem);
    private static final RegistryObject<Item> EXAMPLE_POTION =
            ITEMS.register("potion_diarrhea", () -> ExamplePotion);
    private static final RegistryObject<Item> FLORAPOTION =
            ITEMS.register("potion_flora" , () -> FloraPotion);
    private static final RegistryObject<Item> FREEZEPOTION =
            ITEMS.register("potion_freeze" , () -> FreezePotion);
    private static final RegistryObject<Item> HUNGERREGENERATIONPOTION =
            ITEMS.register("potion_hunger_regeneration" , () -> HungerRegenerationPotion);
    private static final RegistryObject<Item> WHITECHALK =
            ITEMS.register("white_chalk", () -> WhiteChalkItem);
    private static final RegistryObject<Item> GOLDENCHALK =
            ITEMS.register("golden_chalk", () -> GoldenChalkItem);
    private static final RegistryObject<Item> BLIZZARDSWORD =
            ITEMS.register("blizzard_sword", () -> BlizzardSwordItem);
    private static final RegistryObject<Item> HUNGERREGENERATIONTALISMAN =
            ITEMS.register("hunger_regeneration_talisman", () -> HungerRegenerationTalisman);
    private static final RegistryObject<Item> PROTECTIONOFPROJECTILESTALISMAN =
            ITEMS.register("protection_of_death_talisman", () -> ProtectionOfProjectilesTalisman);
    // BlockItems



    public static void registerItems(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
