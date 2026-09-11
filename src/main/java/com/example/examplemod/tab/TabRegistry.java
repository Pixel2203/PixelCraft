package com.example.examplemod.tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MODID;


public class TabRegistry {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static final RegistryObject<CreativeModeTab> WitcheryTab =
            CREATIVE_MODE_TABS.register("example_tab", () -> TabFactory.WITCHERY_TAB);


    private static final RegistryObject<CreativeModeTab> MagicalTab =
            CREATIVE_MODE_TABS.register("magical_tab", () -> TabFactory.MAGICAL_TAB);


    public static void registerTabs(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
