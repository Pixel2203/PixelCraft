package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.models.ScrollEntityModel;
import com.example.examplemod.entity.entities.ScrollEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;


public class ScrollEntityRenderer extends LivingEntityRenderer<ScrollEntity, ScrollEntityModel<ScrollEntity>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID,"textures/entity/example_entity.png");

    public ScrollEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ScrollEntityModel<>(ctx.bakeLayer(ScrollEntityModel.LAYER_LOCATION)), 0);
    }


    @Override
    public ResourceLocation getTextureLocation(ScrollEntity p_114482_) {
        return TEXTURE;
    }
}
