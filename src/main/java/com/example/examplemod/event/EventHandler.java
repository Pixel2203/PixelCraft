package com.example.examplemod.event;

import com.example.examplemod.API.APIHelper;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ItemFactory;
import com.example.examplemod.item.custom.talisman.impl.ProtectionOfDeathTalisman;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void onEntityDamageEvent(LivingHurtEvent event){
        float currentEntityHealth = event.getEntity().getHealth();

        // Protection of Death Talisman
        if(currentEntityHealth - event.getAmount() < 0.5){
            if(APIHelper.hasCurioEquipped(event.getEntity(),ItemFactory.ProtectionOfDeathTalisman)){
                CuriosApi.getCuriosInventory(event.getEntity()).ifPresent(itemHandler -> itemHandler.findFirstCurio(ItemFactory.ProtectionOfDeathTalisman).ifPresent(slotResult -> {
                    ProtectionOfDeathTalisman protectionOfDeathTalisman = (ProtectionOfDeathTalisman) slotResult.stack().getItem();
                    protectionOfDeathTalisman.triggerEffect(event);

                }));
            }
        }
    }





}
