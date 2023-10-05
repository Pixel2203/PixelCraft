package com.example.examplemod.tab;

import com.example.examplemod.item.ItemFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.function.Supplier;

import static com.example.examplemod.item.ItemRegistry.EXAMPLE;

public class TabFactory {

    private static final CreativeModeTab EXAMPLE_TAB =
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemFactory.ZirconItem))
                    .title(Component.translatable("creativetab.example_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ItemFactory.ZirconItem); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                    }).build();
    public static final Supplier<CreativeModeTab> ExampleTab_TAB =
            () -> EXAMPLE_TAB;
}
