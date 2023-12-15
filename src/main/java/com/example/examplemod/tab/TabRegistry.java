package com.example.examplemod.tab;

import com.example.examplemod.tab.TabFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MODID;


public class TabRegistry {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static final RegistryObject<CreativeModeTab> ExampleTab =
            CREATIVE_MODE_TABS.register("example_tab", () -> TabFactory.EXAMPLE_TAB);


    public static void registerTabs(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
