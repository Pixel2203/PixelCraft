package com.example.examplemod.tab;

import com.example.examplemod.block.BlockFactory;
import com.example.examplemod.item.ItemFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.function.Supplier;

import static com.example.examplemod.item.ItemRegistry.EXAMPLE;

public class TabFactory {

    public static final CreativeModeTab EXAMPLE_TAB =
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemFactory.ZirconItem))
                    .title(Component.translatable("creativetab.example_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ItemFactory.ZirconItem); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                        output.accept(ItemFactory.ExampleItem);
                        output.accept(BlockFactory.ExampleBlock_BLK);
                        output.accept(BlockFactory.CauldronCustomBlock_BLK);
                        output.accept(BlockFactory.ZirconBlock_BLK);
                        output.accept(ItemFactory.ExamplePotion);
                    }).build();
}
