package com.example.examplemod.event;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.capabilities.PlayerSoulEnergy;
import com.example.examplemod.capabilities.PlayerSoulEnergyProvider;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.ScrollEntity;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.talisman.ProtectionOfDeathTalisman;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventHandler {
    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);

    @SubscribeEvent
    public static void onEntityDamageEvent(LivingHurtEvent event){
        LivingEntity entity = event.getEntity();
        float currentEntityHealth = entity.getHealth();
        // Protection of Death Talisman
        if(currentEntityHealth - event.getAmount() < 0.5){
            if(APIHelper.hasCurioEquipped(entity, ItemRegistry.PROTECTION_OF_DEATH_TALISMAN.get())){
                CuriosApi.getCuriosInventory(entity).ifPresent(itemHandler -> itemHandler.findFirstCurio(ItemRegistry.PROTECTION_OF_DEATH_TALISMAN.get()).ifPresent(slotResult -> {
                    ProtectionOfDeathTalisman protectionOfDeathTalisman = (ProtectionOfDeathTalisman) slotResult.stack().getItem();
                    protectionOfDeathTalisman.triggerEffect(event);

                }));
                return;
            }


            // Entity will still die
            if(event.getEntity().getType() != EntityRegistry.SOUL_ENTITY.get() && !entity.level().isClientSide()){
                ServerLevel level = (ServerLevel) entity.level();
                BlockPos entityDeathPosition = entity.blockPosition();
                var spawned = EntityRegistry.SOUL_ENTITY.get().spawn(level, entityDeathPosition ,MobSpawnType.EVENT);

                //spawned.startAscending(entityDeathPosition);
            }

        }
    }


    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player){
            if(!event.getObject().getCapability(PlayerSoulEnergyProvider.PLAYER_SOUL_ENERGY).isPresent()){
                event.addCapability(new ResourceLocation(ExampleMod.MODID, "properties"), new PlayerSoulEnergyProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            var capability = event.getOriginal().getCapability(PlayerSoulEnergyProvider.PLAYER_SOUL_ENERGY);
            capability.ifPresent(oldStore -> {
                capability.ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.register(PlayerSoulEnergy.class);
    }




}
