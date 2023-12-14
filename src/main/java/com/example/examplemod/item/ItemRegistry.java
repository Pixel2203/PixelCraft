package com.example.examplemod.item;

import com.example.examplemod.API.scroll.HealingScrollSpell;
import com.example.examplemod.API.scroll.ProjectileBarrierScrollSpell;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.ChalkBlock;
import com.example.examplemod.item.items.BlizzardSword;
import com.example.examplemod.item.items.ChalkItem;
import com.example.examplemod.item.items.ZirconItem;
import com.example.examplemod.item.items.scrolls.ScrollItem;
import com.example.examplemod.item.items.talisman.HungerRegenerationTalisman;
import com.example.examplemod.item.items.talisman.ProtectionOfDeathTalisman;
import com.example.examplemod.item.items.talisman.ProtectionOfFreezingTalisman;
import com.example.examplemod.potion.custom.flora.FloraSplashPotionItem;
import com.example.examplemod.potion.custom.freezing.FreezingSplashPotionItem;
import com.example.examplemod.potion.custom.hungerregeneration.HungerRegenerationSplashPotionItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MODID;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> ZIRCON = ITEMS.register("zircon", () -> new ZirconItem());
    public static final RegistryObject<Item> EXAMPLE_POTION = ITEMS.register("potion_diarrhea", () -> new SplashPotionItem(new Item.Properties()));
    public static final RegistryObject<Item> POTION_FLORA = ITEMS.register("potion_flora" , () -> new FloraSplashPotionItem(new Item.Properties()));
    public static final RegistryObject<Item> POTION_FREEZE = ITEMS.register("potion_freeze" , () -> new FreezingSplashPotionItem(new Item.Properties()));
    public static final RegistryObject<Item> POTION_HUNGER_REGENERATION = ITEMS.register("potion_hunger_regeneration" , () ->  new HungerRegenerationSplashPotionItem(new Item.Properties()));
    public static final RegistryObject<Item> WHITE_CHALK = ITEMS.register("white_chalk", () ->  new ChalkItem(new Item.Properties(), (ChalkBlock) BlockRegistry.WhiteChalkBlock.get()));
    public static final RegistryObject<Item> GOLDEN_CHALK = ITEMS.register("golden_chalk", () -> new ChalkItem(new Item.Properties(), (ChalkBlock) BlockRegistry.GoldenChalkBlock.get()));
    public static final RegistryObject<Item> BLIZZARD_SWORD = ITEMS.register("blizzard_sword", () -> new BlizzardSword(Tiers.DIAMOND,3,-2.4F,new Item.Properties()));
    public static final RegistryObject<Item> HUNGER_REGENERATION_TALISMAN = ITEMS.register("hunger_regeneration_talisman", () -> new HungerRegenerationTalisman());
    public static final RegistryObject<Item> PROTECTION_OF_DEATH_TALISMAN = ITEMS.register("protection_of_death_talisman", () -> new ProtectionOfDeathTalisman());
    public static final RegistryObject<Item> PROTECTION_OF_FREEZING_TALISMAN = ITEMS.register("protection_of_freezing_talisman", () -> new ProtectionOfFreezingTalisman());
    public static final RegistryObject<Item> HEALING_SCROLL = ITEMS.register("healing_scroll", () -> new ScrollItem(new HealingScrollSpell(20,0)));
    public static final RegistryObject<Item> PROJECTILE_BARRIER_SCROLL = ITEMS.register("projectile_barrier_scroll", () -> new ScrollItem(new ProjectileBarrierScrollSpell(0,0)));


    public static void registerItems(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
