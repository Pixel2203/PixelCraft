package com.example.examplemod.api;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockDynamicUtil {
    private static String namespace = ExampleMod.MODID.toString();

    /**
     * @param IsBelow If True, the method will check if the given Block is below the Executing Block,
     *                if its false, it will check for the Block above
     * @param IsBelow defines the Distance to the Block of Request in Relation to Below/Above
     * @return
     */
    public static boolean isBlockPos(Level level, BlockPos pos, String blockName, Boolean IsBelow, int Distance) {
        // Konvertiere den Blocknamen (String) zu einem ResourceLocation-Objekt
        ResourceLocation blockLocation = new ResourceLocation( namespace, blockName);

        // Hole den Block anhand des ResourceLocation aus der Registry
        Block block = ForgeRegistries.BLOCKS.getValue(blockLocation);

        // Überprüfe, ob der Block am angegebenen Ort gleich dem dynamisch ermittelten Block ist
        if (IsBelow) {
            if (block != null) {
                BlockState blockState = level.getBlockState(pos.below(Distance));
                return blockState.getBlock() == block;
            }
        }
        else {
            if (block != null) {
                BlockState blockState = level.getBlockState(pos.above(Distance));
                return blockState.getBlock() == block;
            }
        }

        // Rückgabe false, wenn der Block nicht gefunden wird
        return false;
    }

    public static Block getBlockFromString(String blockName) {
        ResourceLocation blockLocation = new ResourceLocation(namespace, blockName);
        return ForgeRegistries.BLOCKS.getValue(blockLocation);
    }
}
