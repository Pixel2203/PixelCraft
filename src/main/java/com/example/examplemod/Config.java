package com.example.examplemod;

import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        ItemRegistry.ITEMS.getEntries().stream().map(registeredItem -> registeredItem.getId().toString()).forEach(s -> log.info("Registered Item: {}", s));
        EntityRegistry.ENTITIES.getEntries().stream().map(registeredItem -> registeredItem.getId().toString()).forEach(s -> log.info("Registered Entity: {}", s));
        BlockRegistry.BLOCKS.getEntries().stream().map(registeredItem -> registeredItem.getId().toString()).forEach(s -> log.info("Registered Block: {}", s));
    }
}
