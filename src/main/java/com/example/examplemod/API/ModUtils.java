package com.example.examplemod.API;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ModUtils {
    public static Vec3 calcCenterOfBlock(BlockPos blockPos){
        return new Vec3(blockPos.getX() + 0.5d,blockPos.getY() - 0.5d,blockPos.getZ()+ 0.5d);
    }
}
