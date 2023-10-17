package com.example.examplemod.item;

import com.example.examplemod.item.custom.ZirconItem;
import com.example.examplemod.potion.flora.FloraPotion;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SplashPotionItem;

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
            new FloraPotion(new Item.Properties());

}
