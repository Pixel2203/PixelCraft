package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.entities.ExampleEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class ScrollRenderer extends EntityRenderer<ExampleEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID,"textures/entity/example_entity.png");

    protected ScrollRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }
    @Override
    public ResourceLocation getTextureLocation(ExampleEntity p_114482_) {
        return TEXTURE;
    }



}
