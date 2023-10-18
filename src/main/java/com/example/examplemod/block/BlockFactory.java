package com.example.examplemod.block;

import com.example.examplemod.block.custom.KettleBlock;
import com.example.examplemod.block.custom.chalk.GoldenChalkBlock;
import com.example.examplemod.block.custom.chalk.WhiteChalkBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockFactory {
    public static final Block ExampleBlock_BLK = new Block(BlockBehaviour.Properties.copy(Blocks.STONE));
    public static final Block ZirconBlock_BLK = new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    public static final Block CauldronCustomBlock_BLK = new KettleBlock();
    public static final Block GoldenChalkBlock_BLK = new GoldenChalkBlock(BlockBehaviour.Properties.copy(Blocks.CYAN_CARPET));
    public static final Block WhiteChalkBlock_BLK = new WhiteChalkBlock(BlockBehaviour.Properties.copy(Blocks.CYAN_CARPET));
    public static final BlockItem ExampleBlock_BLKITM = new BlockItem(BlockFactory.ExampleBlock_BLK, new Item.Properties());
    public static final BlockItem ZirconBlock_BLKITM = new BlockItem(BlockFactory.ZirconBlock_BLK, new Item.Properties());
    public static final BlockItem CauldronCustomBlock_BLKITM = new BlockItem(BlockFactory.CauldronCustomBlock_BLK, new Item.Properties());
    public static final BlockItem GoldenChalkBlock_BLKITM = new BlockItem(BlockFactory.GoldenChalkBlock_BLK, new Item.Properties());
}
