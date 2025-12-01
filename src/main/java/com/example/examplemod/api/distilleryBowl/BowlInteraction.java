package com.example.examplemod.api.distilleryBowl;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface BowlInteraction {

    BowlInteractionLogic getInteractionLogic();

    default InteractionResult use(Player player) {
        return getInteractionLogic().use(player);
    }
}
