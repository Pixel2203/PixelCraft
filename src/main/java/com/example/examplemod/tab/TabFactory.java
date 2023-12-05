package com.example.examplemod.tab;

import com.example.examplemod.API.ItemStackHelper;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TabFactory {

    public static final CreativeModeTab EXAMPLE_TAB =
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.ZIRCON.get()))
                    .title(Component.translatable("creativetab.example_tab"))
                    .displayItems((parameters, output) -> {
                        ItemRegistry.ITEMS.getEntries().stream().map(itemRegistryObject -> itemRegistryObject.get()).forEach(output::accept);
                        output.accept(ItemStackHelper.createFloraPotion(1,1));
                        output.accept(ItemStackHelper.createFloraPotion(2,1));
                        output.accept(ItemStackHelper.createFloraPotion(3,1));
                        output.accept(ItemStackHelper.createHungerRegenerationPotion(1,1,100,0));
                        output.accept(ItemStackHelper.createHungerRegenerationPotion(2,1,200,1));
                        output.accept(ItemStackHelper.createHungerRegenerationPotion(3,1,300,2));
                        output.accept(ItemStackHelper.createFreezePotion(1,200,0));
                    }).build();
}
