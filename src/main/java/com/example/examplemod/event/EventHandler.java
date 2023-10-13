package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
        System.out.println("Item picked up!");
    }

}
