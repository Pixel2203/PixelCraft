package com.example.examplemod.item;

import com.example.examplemod.block.BlockFactory;
import com.example.examplemod.item.custom.*;
import com.example.examplemod.item.custom.talisman.impl.HungerRegenerationTalisman;
import com.example.examplemod.item.custom.talisman.impl.ProtectionOfDeathTalisman;
import com.example.examplemod.item.custom.talisman.impl.ProtectionOfFreezingTalisman;
import com.example.examplemod.potion.custom.flora.FloraSplashPotionItem;
import com.example.examplemod.potion.custom.freezing.FreezingSplashPotionItem;
import com.example.examplemod.potion.custom.hungerregeneration.HungerRegenerationSplashPotionItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.Tiers;

public class ItemFactory {

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final Item ExampleItem =
            new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEat().nutrition(1).saturationMod(2f).build()));

    public static final Item ZirconItem =
            new ZirconItem();
    public static final SplashPotionItem ExamplePotion =
            new SplashPotionItem(new Item.Properties());

    public static final SplashPotionItem FloraPotion =
            new FloraSplashPotionItem(new Item.Properties());
    public static final SplashPotionItem FreezePotion =
            new FreezingSplashPotionItem(new Item.Properties());
    public static final SplashPotionItem HungerRegenerationPotion =
            new HungerRegenerationSplashPotionItem(new Item.Properties());
    public static final Item WhiteChalkItem =
            new ChalkItem(new Item.Properties(), BlockFactory.WhiteChalkBlock_BLK);
    public static final Item GoldenChalkItem =
            new ChalkItem(new Item.Properties(), BlockFactory.GoldenChalkBlock_BLK);
    public static final Item BlizzardSwordItem =
            new BlizzardSword(Tiers.DIAMOND,3,-2.4F,new Item.Properties());
    public static final Item HungerRegenerationTalisman =
            new HungerRegenerationTalisman();
    public static final Item ProtectionOfDeathTalisman =
            new ProtectionOfDeathTalisman();
    public static final Item ProtectionOfFreezingTalisman =
            new ProtectionOfFreezingTalisman();
}
