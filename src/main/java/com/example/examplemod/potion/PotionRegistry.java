package com.example.examplemod.potion;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionRegistry {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ExampleMod.MODID);

    public static final RegistryObject<Potion> TEST_POTION = POTIONS.register("test_potion", () -> new Potion(new MobEffectInstance(MobEffects.BLINDNESS,900,0)));
    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
