package com.example.examplemod.block.blocks;

import com.example.examplemod.api.runes.Runes;
import com.example.examplemod.capabilities.rune_knowledge.RuneKnowledgeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class RuneStatueBlock extends Block {

    private final Runes runeType;

    public RuneStatueBlock(Properties props, Runes type) {
        super(props);
        this.runeType = type;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        if (!level.isClientSide) {
            player.getCapability(RuneKnowledgeProvider.RUNE_CAP).ifPresent(cap -> {
                if (!cap.hasRune(runeType)) {
                    cap.unlockRune(runeType);
                    player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("You unlocked the " + runeType + " rune!"),
                            true
                    );
                }
            });
        }
        return InteractionResult.SUCCESS;
    }
}
