package com.example.examplemod.block;

import com.example.examplemod.block.blocks.*;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.example.examplemod.ExampleMod.MODID;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    //private static final RegistryObject<Block> ExampleBlock = registerBlockWithItem("example_block",() -> BlockFactory.ExampleBlock_BLK);
    public static final RegistryObject<Block> ZirconBlock = registerBlockWithItem("zircon_block", () -> Factory.ZirconBlock_BLK);
    public static final RegistryObject<Block> CauldronCustomBlock = registerBlockWithItem("cauldron_custom",() -> Factory.CauldronCustomBlock_BLK);
    public static final RegistryObject<Block> GoldenChalkBlock =registerBlock("golden_chalk",() -> Factory.GoldenChalkBlock_BLK);
    public static final RegistryObject<Block> WhiteChalkBlock = registerBlock("white_chalk",() -> Factory.WhiteChalkBlock_BLK);
    public static final RegistryObject<Block> LeafCloverBlock = registerBlockWithItem("four_leaf_clover", () -> Factory.LeafCloverBlock_BLK);
    public static final RegistryObject<Block> StatuePolishedTuffBlock = registerBlockWithItem("statue_polished_tuff", () -> Factory.StatueBlock_BLK);
    public static final RegistryObject<Block> StatuePolishedTuffTopBlock = registerBlockWithItem("statue_polished_tuff_top", () -> Factory.StatueBlockTop_BLK);


    private static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> block){
        RegistryObject<T> toReturn = (RegistryObject<T>) registerBlock(name, block);
        ItemRegistry.ITEMS.register(name, () ->  new BlockItem(block.get(), new Item.Properties()));
        return toReturn;
    }
    private static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> block, Item.Properties properties){
        RegistryObject<T> toReturn = (RegistryObject<T>) registerBlock(name,block);
        ItemRegistry.ITEMS.register(name, () ->  new BlockItem(block.get(), properties));
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Block> registerBlock(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }

    public static void registerBlocks(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

    private static class Factory {
        public static final Block ZirconBlock_BLK = new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
        public static final Block CauldronCustomBlock_BLK = new KettleBlock();
        public static final StatueBlock StatueBlock_BLK = new StatueBlock();
        public static final StatueBlock StatueBlockTop_BLK = new StatueBlockTop();
        public static final ChalkBlock GoldenChalkBlock_BLK = new GoldenChalkBlock();
        public static final ChalkBlock WhiteChalkBlock_BLK = new ChalkBlock();
        public static final FlowerBlock LeafCloverBlock_BLK = new FlowerBlock(() -> MobEffects.DIG_SPEED,5, BlockBehaviour.Properties.copy(Blocks.RED_TULIP));
    }
}
