package com.example.examplemod.event;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.capabilities.PlayerSoulEnergy;
import com.example.examplemod.capabilities.PlayerSoulEnergyProvider;
import com.example.examplemod.effect.MobEffectRegistry;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.entity.models.SoulEntityModel;
import com.example.examplemod.entity.renderers.SoulEntityRenderer;
import com.example.examplemod.event.entity.Renderers;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.item.items.talisman.ProtectionOfDeathTalisman;
import com.example.examplemod.item.items.talisman.SoulboundTalisman;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventHandler {
    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);

    @SubscribeEvent
    public static void onEntityDamageEvent(LivingHurtEvent event){
        LivingEntity entity = event.getEntity();
        float currentEntityHealth = entity.getHealth();
        // Soulbound Talisman
        if(APIHelper.hasCurioEquipped(entity, ItemRegistry.SOULBOUND_TALISMAN.get())) {
            CuriosApi.getCuriosInventory(entity).ifPresent(itemHandler -> {
                itemHandler.findFirstCurio(ItemRegistry.SOULBOUND_TALISMAN.get()).ifPresent(slotResult -> {
                    SoulboundTalisman soulboundTalisman = (SoulboundTalisman) slotResult.stack().getItem();
                    soulboundTalisman.triggerEffect(event, slotResult.stack());
                });
            });

            return;
        }

        // Protection of Death Talisman
        if(currentEntityHealth - event.getAmount() < 0.5){
            if(APIHelper.hasCurioEquipped(entity, ItemRegistry.PROTECTION_OF_DEATH_TALISMAN.get())){
                CuriosApi.getCuriosInventory(entity).ifPresent(itemHandler -> itemHandler.findFirstCurio(ItemRegistry.PROTECTION_OF_DEATH_TALISMAN.get()).ifPresent(slotResult -> {
                    ProtectionOfDeathTalisman protectionOfDeathTalisman = (ProtectionOfDeathTalisman) slotResult.stack().getItem();
                    protectionOfDeathTalisman.triggerEffect(event);

                }));
            }
        }
    }
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event){
        Entity entity = event.getEntity();
        if(event.getEntity().level().isClientSide()) return;
        if(event.getEntity().getType() == EntityRegistry.SOUL_ENTITY.get()) return;
        Random random = new Random();
        ServerLevel level = (ServerLevel) entity.level();
        Vec3 entityDeathPosition = entity.position();
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.SOUL_ENTITY.get(),level);
        soulEntity.setEnergy(random.nextFloat(0.8f));
        soulEntity.setPos(entityDeathPosition.x, entityDeathPosition.y, entityDeathPosition.z);
        level.addFreshEntity(soulEntity);
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

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event){
        BlockPos pos = event.getCamera().getBlockPosition();
        if (event.getCamera().getEntity().level().getBlockState(pos).is(BlockRegistry.FogBlock.get())) {
            event.setNearPlaneDistance(0.1F);
            event.setFarPlaneDistance(4.0F); // Sicht stark reduziert
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFoodConsume(LivingEntityUseItemEvent.Finish event){
        if(!(event.getEntity() instanceof Player p)) return;
        MobEffectInstance effect = p.getEffect(MobEffectRegistry.GLUTTONY.get());
        if(Objects.isNull(effect)) return;

        ItemStack item = event.getItem();
        FoodProperties properties = item.getFoodProperties(p);
        if(properties == null) return;
        int nutrition = properties.getNutrition();
        float saturation = properties.getSaturationModifier();
        FoodData foodData = p.getFoodData();

        for(int i = 0; i < effect.getAmplifier() + 1; i++){
            foodData.eat(nutrition, saturation);
        }

    }

    @SubscribeEvent
    public static void onTargetChange(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewTarget();
        LivingEntity attacker = event.getEntity();

        if (!(attacker.getType() == EntityType.ZOMBIE || attacker.getType() == EntityType.SKELETON)) return; // Nur Untote
        if (!(target instanceof Player player)) return;

        // Prüfen, ob Ziel den Effekt hat
        if (player.hasEffect(MobEffectRegistry.CRYPTLIGHT.get())) {
            // Ziel darf nicht angegriffen werden
            event.setCanceled(true);
        }
    }


}
