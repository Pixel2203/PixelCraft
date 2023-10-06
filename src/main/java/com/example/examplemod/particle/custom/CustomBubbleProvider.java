package com.example.examplemod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class CustomBubbleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;
    public CustomBubbleProvider(SpriteSet spriteSet){
        this.sprites = spriteSet;
    }
    public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        return new CustomBubbleParticle(level, x ,y ,z , this.sprites, dx ,dy ,dz);
    }
}
