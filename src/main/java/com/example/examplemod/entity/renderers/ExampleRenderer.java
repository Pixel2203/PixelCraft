package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.models.ExampleEntityModel;
import com.example.examplemod.entity.entities.ExampleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;


public class ExampleRenderer extends LivingEntityRenderer<ExampleEntity,ExampleEntityModel<ExampleEntity>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID,"textures/entity/example_entity.png");

    public ExampleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ExampleEntityModel<>(ctx.bakeLayer(ExampleEntityModel.LAYER_LOCATION)), 0);
    }


    @Override
    public ResourceLocation getTextureLocation(ExampleEntity p_114482_) {
        return TEXTURE;
    }
}
