package com.example.examplemod.item;

import com.example.examplemod.block.BlockFactory;
import com.example.examplemod.item.custom.BlizzardSword;
import com.example.examplemod.item.custom.ZirconItem;
import com.example.examplemod.item.custom.ritual.ChalkItem;
import com.example.examplemod.potion.flora.FloraSplashPotion;
import com.example.examplemod.potion.freezing.FreezingSplashPotion;
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
            new FloraSplashPotion(new Item.Properties());
    public static final SplashPotionItem FreezePotion =
            new FreezingSplashPotion(new Item.Properties());
    public static final Item WhiteChalkItem =
            new ChalkItem(new Item.Properties(), BlockFactory.WhiteChalkBlock_BLK);
    public static final Item GoldenChalkItem =
            new ChalkItem(new Item.Properties(), BlockFactory.GoldenChalkBlock_BLK);
    public static final Item BlizzardSwordItem =
            new BlizzardSword(Tiers.DIAMOND,3,-2.4F,new Item.Properties());
}
