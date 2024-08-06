package com.example.examplemod.event;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.capabilities.PlayerSoulEnergy;
import com.example.examplemod.capabilities.PlayerSoulEnergyProvider;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.entity.models.SoulEntityModel;
import com.example.examplemod.entity.renderers.SoulEntityRenderer;
import com.example.examplemod.event.entity.Renderers;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.talisman.ProtectionOfDeathTalisman;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Random;

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




        }
    }
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event){
        Entity entity = event.getEntity();
        if(event.getEntity().level().isClientSide()){
            return;
        }
        if(event.getEntity().getType() != EntityRegistry.SOUL_ENTITY.get() && !entity.level().isClientSide()){
            Random random = new Random();
            ServerLevel level = (ServerLevel) entity.level();
            Vec3 entityDeathPosition = entity.position();
            SoulEntity soulEntity = new SoulEntity(EntityRegistry.SOUL_ENTITY.get(),level);
            soulEntity.setEnergy(random.nextFloat(0.8f));
            soulEntity.setPos(entityDeathPosition.x, entityDeathPosition.y, entityDeathPosition.z);
            level.addFreshEntity(soulEntity);
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
