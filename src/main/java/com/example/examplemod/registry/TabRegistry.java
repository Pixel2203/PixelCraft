package com.example.examplemod.registry;

import com.example.examplemod.tab.TabFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MODID;


public class TabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> ExampleTab =
            CREATIVE_MODE_TABS.register("example_tab", () -> TabFactory.EXAMPLE_TAB);


    public static void registerTabs(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
