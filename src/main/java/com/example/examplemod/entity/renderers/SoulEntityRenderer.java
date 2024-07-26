package com.example.examplemod.entity.renderers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.entity.entities.ScrollEntity;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.entity.models.ScrollEntityModel;
import com.example.examplemod.entity.models.SoulEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class SoulEntityRenderer extends LivingEntityRenderer<SoulEntity, SoulEntityModel<SoulEntity>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/soul.png");
    private static final Logger log = LoggerFactory.getLogger(SoulEntityRenderer.class);

    public SoulEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SoulEntityModel<>(ctx.bakeLayer(SoulEntityModel.LAYER_LOCATION)), 0);
    }

    @Override
    public void render(SoulEntity soulEntity, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if(Objects.isNull(player)){
            log.error("LocalPlayer for SoulEntityRenderer is null");
            super.render(soulEntity, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
            return;
        }
        if(soulEntity.renderToPlayer(player)) return;

        super.render(soulEntity, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SoulEntity p_114482_) {
        return TEXTURE;
    }
}


