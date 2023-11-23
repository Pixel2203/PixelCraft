package com.example.examplemod.event;

import com.example.examplemod.API.APIHelper;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ItemFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
        System.out.println("Item picked up!");
    }

    @SubscribeEvent
    public static void onEntityDamageByEntityEvent(LivingHurtEvent event){
        if(event.getEntity() instanceof Player player){
            float currentPlayerHealth = player.getHealth();
            if(currentPlayerHealth - event.getAmount() < 0.5){
                player.setHealth(0.5f);
                ItemStack protectionOfDeathTalisman = new ItemStack(ItemFactory.ProtectionOfProjectilesTalisman);
                if(!player.getInventory().contains(protectionOfDeathTalisman)){
                    return;
                }
                int slot = APIHelper.findItemInInventory(player,protectionOfDeathTalisman);
                if(slot == -1){
                    return;
                }
                player.getInventory().getItem(slot).shrink(1);
                event.setCanceled(true);
            }
        }
    }


}
