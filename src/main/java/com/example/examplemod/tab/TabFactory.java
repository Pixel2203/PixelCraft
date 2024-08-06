package com.example.examplemod.tab;

import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.potion.CustomSplashPotionItem;
import com.example.examplemod.item.items.potion.potions.flora.FloraSplashPotionItem;
import com.example.examplemod.item.items.potion.potions.freezing.FreezingSplashPotionItem;
import com.example.examplemod.item.items.potion.potions.hungerregeneration.HungerRegenerationSplashPotionItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class TabFactory {
    public static final CreativeModeTab EXAMPLE_TAB =
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.ZIRCON.get()))
                    .title(Component.translatable("creativetab.example_tab"))
                    .displayItems((parameters, output) -> {
                        ItemRegistry.ITEMS.getEntries()
                                .stream()
                                .map(RegistryObject::get)
                                .filter(item -> !(item instanceof CustomSplashPotionItem))
                                .forEach(output::accept);
                        output.accept(ItemRegistry.STATUE_STONE.get());
                        output.accept(ItemRegistry.STATUE_POWERFUL_STONE.get());
                        output.accept(FloraSplashPotionItem.createFloraPotion(1,1));
                        output.accept(FloraSplashPotionItem.createFloraPotion(2,1));
                        output.accept(FloraSplashPotionItem.createFloraPotion(3,1));
                        output.accept(HungerRegenerationSplashPotionItem.createHungerRegenerationPotion(1,1,100,0));
                        output.accept(HungerRegenerationSplashPotionItem.createHungerRegenerationPotion(2,1,200,1));
                        output.accept(HungerRegenerationSplashPotionItem.createHungerRegenerationPotion(3,1,300,2));
                        output.accept(FreezingSplashPotionItem.createFreezePotion(1,200,0));
                    }).build();
}
