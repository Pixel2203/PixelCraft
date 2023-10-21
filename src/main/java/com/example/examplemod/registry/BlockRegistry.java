package com.example.examplemod.registry;

import com.example.examplemod.block.BlockFactory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MODID;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> ExampleBlock =
            BLOCKS.register("example_block",() -> BlockFactory.ExampleBlock_BLK);
    public static final RegistryObject<Block> ZirconBlock =
            BLOCKS.register("zircon_block",() -> BlockFactory.ZirconBlock_BLK);
    public static final RegistryObject<Block> CauldronCustomBlock =
            BLOCKS.register("cauldron_custom",() -> BlockFactory.CauldronCustomBlock_BLK);
    public static final RegistryObject<Block> GoldenChalkBlock =
            BLOCKS.register("golden_chalk",() -> BlockFactory.GoldenChalkBlock_BLK);
    public static final RegistryObject<Block> WhiteChalkBlock =
            BLOCKS.register("white_chalk",() -> BlockFactory.WhiteChalkBlock_BLK);

    public static final RegistryObject<BlockItem> ExampleBlockItem =
            ItemRegistry.ITEMS.register("example_block", () -> BlockFactory.ExampleBlock_BLKITM);
    public static final RegistryObject<BlockItem> ZirconBlockItem =
            ItemRegistry.ITEMS.register("zircon_block", () -> BlockFactory.ZirconBlock_BLKITM);
    public static final RegistryObject<BlockItem> CauldronCustomBlockItem =
            ItemRegistry.ITEMS.register("cauldron_custom", () -> BlockFactory.CauldronCustomBlock_BLKITM);
    public static void registerBlocks(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
