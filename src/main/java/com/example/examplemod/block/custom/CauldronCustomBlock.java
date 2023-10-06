package com.example.examplemod.block.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CauldronCustomBlock extends Block {
    public CauldronCustomBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON));
    }

}
