package com.example.examplemod.block;

import com.example.examplemod.block.blocks.*;
import com.example.examplemod.block.blocks.statue_blocks.Basic.StatueBlockBase;
import com.example.examplemod.block.blocks.statue_blocks.PF.StatueBlockPFBase;
import com.example.examplemod.block.blocks.statue_blocks.PF.StatueBlockPFMiddle;
import com.example.examplemod.block.blocks.statue_blocks.Basic.StatueBlockTop;
import com.example.examplemod.block.blocks.statue_blocks.PF.StatueBlockPFTop;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
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
    public static final RegistryObject<Block> StatueStoneBase = registerBlock("statue_stone_base", () -> Factory.StatueBlock_BLK);
    public static final RegistryObject<Block> StatueStoneTop = registerBlock("statue_stone_top", () -> Factory.StatueBlockTop_BLK);
    public static final RegistryObject<Block> StatuePowerfulStoneBase = registerBlock("statue_powerful_stone_base", () -> Factory.StatueBlockPowerfulBase_BLK);
    public static final RegistryObject<Block> StatuePowerfulStoneMiddle = registerBlock("statue_powerful_stone_middle", () -> Factory.StatueBlockPowerfulMiddle_BLK);
    public static final RegistryObject<Block> StatuePowerfulStoneTop = registerBlock("statue_powerful_stone_top", () -> Factory.StatueBlockPowerfulTop_BLK);

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
        public static final StatueBlockBase StatueBlock_BLK = new StatueBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE), "","statue_stone_top", "statue_stone_base",false);
        public static final StatueBlockTop StatueBlockTop_BLK = new StatueBlockTop(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.EMPTY), "","statue_stone_top", "statue_stone_base",false);
        public static final StatueBlockPFBase StatueBlockPowerfulBase_BLK = new StatueBlockPFBase(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS), "statue_powerful_stone_top","statue_powerful_stone_middle", "statue_powerful_stone_base",true);
        public static final StatueBlockPFMiddle StatueBlockPowerfulMiddle_BLK = new StatueBlockPFMiddle(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS), "statue_powerful_stone_top","statue_powerful_stone_middle", "statue_powerful_stone_base",true);
        public static final StatueBlockPFTop StatueBlockPowerfulTop_BLK = new StatueBlockPFTop(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).lightLevel((state) -> 8), "statue_powerful_stone_top", "statue_powerful_stone_middle", "statue_powerful_stone_base", true);
        public static final ChalkBlock GoldenChalkBlock_BLK = new GoldenChalkBlock();
        public static final ChalkBlock WhiteChalkBlock_BLK = new ChalkBlock();
        public static final FlowerBlock LeafCloverBlock_BLK = new FlowerBlock(() -> MobEffects.DIG_SPEED,5, BlockBehaviour.Properties.copy(Blocks.RED_TULIP));
    }
}
