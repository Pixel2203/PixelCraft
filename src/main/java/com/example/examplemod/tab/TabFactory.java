package com.example.examplemod.tab;

import com.example.examplemod.api.distilleryBowl.ModFluids;
import com.example.examplemod.api.runes.RuneType;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.Vial;
import com.example.examplemod.item.items.WoodenRune;
import com.example.examplemod.item.items.potion.CustomSplashPotionItem;
import com.example.examplemod.item.items.potion.potions.flora.FloraSplashPotionItem;
import com.example.examplemod.item.items.potion.potions.freezing.FreezingSplashPotionItem;
import com.example.examplemod.item.items.potion.potions.hungerregeneration.HungerRegenerationSplashPotionItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class TabFactory {
    public static final CreativeModeTab WITCHERY_TAB =
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
                        output.accept(FloraSplashPotionItem.create(1,1));
                        output.accept(FloraSplashPotionItem.create(2,1));
                        output.accept(FloraSplashPotionItem.create(3,1));
                        output.accept(HungerRegenerationSplashPotionItem.create(1,1,100,0));
                        output.accept(HungerRegenerationSplashPotionItem.create(2,1,200,1));
                        output.accept(HungerRegenerationSplashPotionItem.create(3,1,300,2));
                        output.accept(FreezingSplashPotionItem.createFreezePotion(1,200,0));

                        for(ModFluids fluid : ModFluids.values()) {
                            output.accept(Vial.createVialWithType(fluid));
                        }

                        for(RuneType runeType : RuneType.values()) {
                            output.accept(WoodenRune.create(runeType));
                        }

                    }).build();

    public static final CreativeModeTab MAGICAL_TAB =
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.HEALING_SCROLL.get()))
                    .title(Component.translatable("creativetab.magical_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ItemRegistry.CONFUSION_SCROLL.get());
                        output.accept(ItemRegistry.HEALING_SCROLL.get());
                        output.accept(ItemRegistry.PROJECTILE_BARRIER_SCROLL.get());


                        output.accept(ItemRegistry.HUNGER_REGENERATION_TALISMAN.get());
                        output.accept(ItemRegistry.PROTECTION_OF_DEATH_TALISMAN.get());
                        output.accept(ItemRegistry.PROTECTION_OF_FREEZING_TALISMAN.get());
                        output.accept(ItemRegistry.SOULBOUND_TALISMAN.get());
                    })
                    .build();
}
