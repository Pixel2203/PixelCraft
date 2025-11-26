package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
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


public class SoulWispEntityRenderer extends EntityRenderer<SoulWispEntity> {

    public static final ResourceLocation TEXTURE_1 = new ResourceLocation(ExampleMod.MODID, "textures/entity/souls/soul_1.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation(ExampleMod.MODID, "textures/entity/souls/soul_2.png");
    private static final float SIZE = 0.3F;
    public SoulWispEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(SoulWispEntity wispEntity) {
        int i = wispEntity.getTextureIndex();
        return switch (i) {
            case 0 -> TEXTURE_1;
            case 1 -> TEXTURE_2;
            default -> TEXTURE_1;
        };
    }

    @Override
    public void render(SoulWispEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {


        poseStack.pushPose();

        // --- 1. BILLBOARD-TRANSFORMATION ---
        // Dreht das Entity immer zur Kamera. Dies ist der Schlüssel für Billboards.
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

        // Skalierung: Macht das Quad größer oder kleiner
        poseStack.scale(SIZE, SIZE, SIZE);

        // --- 2. LIGHT-FIX (Zur Sicherstellung der Sichtbarkeit) ---
        // Erzwingt maximale Helligkeit, um Lichtprobleme auszuschließen
        int forcedLight = 15728880;
        if (packedLight == 0) {
            packedLight = forcedLight;
        }

        // --- 3. GEOMETRIE ZEICHNEN ---
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));

        // Holen der Matrizen nach allen Transformationen
        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f positionMatrix = lastPose.pose();
        Matrix3f normalMatrix = lastPose.normal();

        // Zeichnen des Quads (Die Normalen zeigen in Z-Richtung, da wir es zur Kamera gedreht haben)
        // x, y, z in der Billboard-Ebene: -0.5 bis 0.5

        // Unten Rechts (U=1, V=1)
        vertexConsumer.vertex(positionMatrix, 0.5F, -0.5F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();

        // Oben Rechts (U=1, V=0)
        vertexConsumer.vertex(positionMatrix, 0.5F, 0.5F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();

        // Oben Links (U=0, V=0)
        vertexConsumer.vertex(positionMatrix, -0.5F, 0.5F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();

        // Unten Links (U=0, V=1)
        vertexConsumer.vertex(positionMatrix, -0.5F, -0.5F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();


        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

}


