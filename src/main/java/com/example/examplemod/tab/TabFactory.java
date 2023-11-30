package com.example.examplemod.tab;

import com.example.examplemod.API.ItemStackHelper;
import com.example.examplemod.block.BlockFactory;
import com.example.examplemod.item.ItemFactory;
import com.example.examplemod.registry.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemFrameItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

public class TabFactory {

    public static final CreativeModeTab EXAMPLE_TAB =
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemFactory.ZirconItem))
                    .title(Component.translatable("creativetab.example_tab"))
                    .displayItems((parameters, output) -> {
                        ItemRegistry.ITEMS.getEntries().stream().map(itemRegistryObject -> itemRegistryObject.get()).forEach(output::accept);
                        /*
                        output.accept(ItemFactory.ZirconItem); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                        output.accept(ItemFactory.ExampleItem);
                        output.accept(BlockFactory.ExampleBlock_BLK);
                        output.accept(BlockFactory.CauldronCustomBlock_BLK);
                        output.accept(BlockFactory.ZirconBlock_BLK);
                        output.accept(ItemFactory.ExamplePotion);
                        output.accept(ItemFactory.BlizzardSwordItem);
                        output.accept(ItemFactory.GoldenChalkItem);
                        output.accept(ItemFactory.WhiteChalkItem);

                         */
                        output.accept(ItemStackHelper.createFloraPotion(1,1));
                        output.accept(ItemStackHelper.createFloraPotion(2,1));
                        output.accept(ItemStackHelper.createFloraPotion(3,1));
                        output.accept(ItemStackHelper.createHungerRegenerationPotion(1,1,100,0));
                        output.accept(ItemStackHelper.createHungerRegenerationPotion(2,1,200,1));
                        output.accept(ItemStackHelper.createHungerRegenerationPotion(3,1,300,2));
                        output.accept(ItemStackHelper.createFreezePotion(1,200,0));
                        /*
                        output.accept(ItemFactory.ProtectionOfDeathTalisman);
                        output.accept(ItemFactory.HungerRegenerationTalisman);
                        output.accept(ItemFactory.ProtectionOfFreezingTalisman);

                         */
                    }).build();
}
