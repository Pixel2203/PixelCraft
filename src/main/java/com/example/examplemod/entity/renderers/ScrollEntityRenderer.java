package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.models.ScrollEntityModel;
import com.example.examplemod.entity.entities.ScrollEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class ScrollEntityRenderer extends LivingEntityRenderer<ScrollEntity, ScrollEntityModel<ScrollEntity>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/example_entity.png");

    public ScrollEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ScrollEntityModel<>(ctx.bakeLayer(ScrollEntityModel.LAYER_LOCATION)), 0);
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ScrollEntity p_114482_) {
        return TEXTURE;
    }
}


