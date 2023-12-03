package com.example.examplemod.entity;

import com.example.examplemod.entity.entities.ExampleEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ExampleEntityRenderer extends EntityRenderer<ExampleEntity> {
    protected ExampleEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(ExampleEntity p_114482_) {
        return null;
    }
}
