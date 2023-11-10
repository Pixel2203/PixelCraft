package com.example.examplemod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;


public class CustomBubbleParticle extends TextureSheetParticle {
    protected CustomBubbleParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                   SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.friction = 0.1f;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *=0.85F;
        this.lifetime = 20;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }
    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime)*age + 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }




}
