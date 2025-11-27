package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.entities.SoulLightEntity;
import com.example.examplemod.entity.entities.SoulWispEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class SoulLightEntityRenderer extends EntityRenderer<SoulLightEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/soul_light.png");
    private static final float SIZE = 1F;
    public SoulLightEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(SoulLightEntity wispEntity) {
        return TEXTURE;
    }
    @Override
    public void render(SoulLightEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        // Billboard zur Kamera
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(SIZE, SIZE, SIZE);

        // Licht auf max setzen
        int forcedLight = 15728880;
        if (packedLight == 0) packedLight = forcedLight;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));

        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f positionMatrix = lastPose.pose();
        Matrix3f normalMatrix = lastPose.normal();

        // --- ANIMATION: Horizontal SpriteSheet ---
        final int FRAME_COUNT = 5;     // Anzahl der Frames nebeneinander
        final int TICKS_PER_FRAME = 4; // Animationstempo

        // Frame bestimmen
        int frameIndex = (entity.tickCount / TICKS_PER_FRAME) % FRAME_COUNT;

        // UV pro Frame berechnen
        float frameWidth = 1.0f / FRAME_COUNT; // 5 Frames → jedes 0.2 breit
        float u0 = frameIndex * frameWidth;    // linke UV-Grenze
        float u1 = u0 + frameWidth;            // rechte UV-Grenze

        float v0 = 0.0f; // nur 1 Row, also volle Höhe
        float v1 = 1.0f;

        // --- QUAD ZEICHNEN MIT ANIMIERTER UV ---
        // unten rechts
        vertexConsumer.vertex(positionMatrix, 0.5F, -0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .uv(u1, v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();

        // oben rechts
        vertexConsumer.vertex(positionMatrix, 0.5F, 0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .uv(u1, v0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();

        // oben links
        vertexConsumer.vertex(positionMatrix, -0.5F, 0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .uv(u0, v0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();

        // unten links
        vertexConsumer.vertex(positionMatrix, -0.5F, -0.5F, 0.0F)
                .color(255, 255, 255, 255)
                .uv(u0, v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

}


