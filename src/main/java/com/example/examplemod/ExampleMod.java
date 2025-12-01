package com.example.examplemod;

import com.example.examplemod.api.recipe.ModRecipes;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.DistilleryBowl;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.effect.MobEffectRegistry;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.event.ModEventHandler;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.Vial;
import com.example.examplemod.loot.ModLootModifiers;
import com.example.examplemod.menus.ModMenuTypes;
import com.example.examplemod.networking.NetworkMessages;
import com.example.examplemod.particle.ParticleFactory;
import com.example.examplemod.screen.DistilleryBowlScreen;
import com.example.examplemod.sound.SoundRegistry;
import com.example.examplemod.tab.TabRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "pixelcraft";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace







    public ExampleMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BlockRegistry.registerBlocks(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ItemRegistry.registerItems(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        TabRegistry.registerTabs(modEventBus);
        BlockEntityRegistry.registerBlockEntityTypes(modEventBus);
        ParticleFactory.registerParticles(modEventBus);
        MobEffectRegistry.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        SoundRegistry.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().register(ModEventHandler.class);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    public void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(ItemRegistry.ZIRCON.get());
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        NetworkMessages.registerChannel();
        ModRecipes.register();
    }

    // Add the example block item to the building blocks tab


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WhiteChalkBlock.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.GoldenChalkBlock.get(), RenderType.translucent());

            MenuScreens.register(ModMenuTypes.DISTILLERY_BOWL_MENU.get(), DistilleryBowlScreen::new);
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
                event.register(DistilleryBowl.bowlBlockColor, BlockRegistry.DistilleryBowlBlock.get());

        }

        @SubscribeEvent
        public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
            event.register(Vial.itemColor, ItemRegistry.VIAL.get());

        }

    }
}
