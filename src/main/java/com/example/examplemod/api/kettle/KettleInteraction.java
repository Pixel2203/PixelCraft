package com.example.examplemod.api.kettle;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface KettleInteraction {
    InteractionResult onBottle(ServerPlayer player);
    void fallOn(Entity entity);
}
