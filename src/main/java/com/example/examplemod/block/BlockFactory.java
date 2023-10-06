package com.example.examplemod.block;

import com.example.examplemod.block.custom.KettleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockFactory {
    public static final Block ExampleBlock_BLK = new Block(BlockBehaviour.Properties.copy(Blocks.STONE));
    public static final Block ZirconBlock_BLK = new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    public static final Block CauldronCustomBlock_BLK = new KettleBlock();
    public static final BlockItem ExampleBlock_BLKITM = new BlockItem(BlockFactory.ExampleBlock_BLK, new Item.Properties());
    public static final BlockItem ZirconBlock_BLKITM = new BlockItem(BlockFactory.ZirconBlock_BLK, new Item.Properties());
    public static final BlockItem CauldronCustomBlock_BLKITM = new BlockItem(BlockFactory.CauldronCustomBlock_BLK, new Item.Properties());
}
