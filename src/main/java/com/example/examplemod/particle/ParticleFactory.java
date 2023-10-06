package com.example.examplemod.particle;


import com.example.examplemod.ExampleMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleFactory  {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ExampleMod.MODID);

    public static final RegistryObject<SimpleParticleType> CustomBubbleParticle =
            PARTICLE_TYPES.register("custom_bubble", () -> new SimpleParticleType(true));
    public static void registerParticles(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
