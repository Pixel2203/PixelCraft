package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.entities.ScrollEntity;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.entity.models.ScrollEntityModel;
import com.example.examplemod.entity.models.SoulEntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class SoulEntityRenderer extends LivingEntityRenderer<SoulEntity, SoulEntityModel<SoulEntity>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/soul.png");

    public SoulEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SoulEntityModel<>(ctx.bakeLayer(SoulEntityModel.LAYER_LOCATION)), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(SoulEntity p_114482_) {
        return TEXTURE;
    }
}


