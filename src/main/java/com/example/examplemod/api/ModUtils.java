package com.example.examplemod.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModUtils {
    public static Vec3 calcCenterOfBlock(BlockPos blockPos){
        return new Vec3(blockPos.getX() + 0.5d,blockPos.getY() + 0.5d,blockPos.getZ()+ 0.5d);
    }
    public static double calculateDistanceBetweenBlockPos(BlockPos firstPos, BlockPos secondPos){
        int dX = firstPos.getX() - secondPos.getX();
        int dZ = firstPos.getZ() - secondPos.getZ();
        int dY = firstPos.getY() - secondPos.getY();
        double a = Math.sqrt(Math.pow(dX,2) + Math.pow(dZ,2));
        return Math.sqrt(Math.pow(dY,2) + Math.pow(a,2));
    }

    public static void setHoverName(ItemStack itemStack, String translatable){
        itemStack.setHoverName(Component.translatable(translatable));
    }

    /**
     * Spawns a particle in a Box that is defined by the passed parameters. The particle is spawned randomly inside the box
     * @param level ServerLevel where the particle will be spawned in
     * @param particle The ParticleType that will be spawned
     * @param defaultPos Position of the particle
     * @param amount Amount of particles that will be spawned
     * @param xSpread Spread in X-Direction
     * @param ySpread Spread in Y-Direction
     * @param zSpread Spread in Z-Direction
     * @param p4
     */
    private static Random random = new Random();
    public static void sendParticles(ServerLevel level, ParticleOptions particle, BlockPos defaultPos, float probability, int amount, double xSpread, double ySpread, double zSpread, double p4){

        if(random.nextFloat() > (1-probability)){
            level.sendParticles(particle,defaultPos.getX(), defaultPos.getY(), defaultPos.getZ(),amount,xSpread,ySpread,zSpread,p4);
        }

    }


    public interface ArmorSlots {
        int HELMET = 3;
        int CHEST_PLATE = 2;
        int LEGGINGS = 1;
        int SHOES = 0;
    }

    public static List<BlockInfo> getBlocksInBoundingBox(Level level, AABB boundingBox){
        int minX = (int) boundingBox.minX;
        int minY = (int) boundingBox.minY;
        int minZ = (int) boundingBox.minZ;
        int maxX = (int) boundingBox.maxX;
        int maxY = (int) boundingBox.maxY;
        int maxZ = (int) boundingBox.maxZ;
        List<BlockInfo> list = new ArrayList<>();
        for(int x = minX; x < maxX; x++){
            for(int y = minY; y < maxY; y++){
                for(int z = minZ; z < maxZ; z++){
                    BlockPos pos = new BlockPos(x, y, z);
                    list.add(new BlockInfo(pos,level.getBlockState(pos)));
                }
            }
        }
        return list;
    }

    public static CompoundTag vec3ToTag(Vec3 vec3){
        CompoundTag tag = new CompoundTag();
        tag.putDouble("X", vec3.x);
        tag.putDouble("Y", vec3.y);
        tag.putDouble("Z", vec3.z);
        return tag;
    }
    public static CompoundTag vec3ToTag(BlockPos vec3){
        CompoundTag tag = new CompoundTag();
        tag.putDouble("X", vec3.getX());
        tag.putDouble("Y", vec3.getY());
        tag.putDouble("Z", vec3.getZ());
        return tag;
    }

    public static Vec3 vec3FromTag(CompoundTag tag){
        if(!tag.contains("X") || !tag.contains("Y") || !tag.contains("Z")) return Vec3.ZERO;
        double x = tag.getDouble("X");
        double y = tag.getDouble("Y");
        double z = tag.getDouble("Z");
        return new Vec3(x, y, z);
    }

    public static BlockPos blockPosFromTag(CompoundTag tag){
        Vec3 vec3 = vec3FromTag(tag);
        return new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z);
    }
}
