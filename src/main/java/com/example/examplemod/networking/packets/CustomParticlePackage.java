package com.example.examplemod.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class CustomParticlePackage {
    float posX;
    float posY;
    float posZ;
    float velX;
    float velY;
    float velZ;
    public CustomParticlePackage(FriendlyByteBuf buffer) {
        this.posX = buffer.readFloat();
        this.posY = buffer.readFloat();
        this.posZ = buffer.readFloat();
        this.velX = buffer.readFloat();
        this.velY = buffer.readFloat();
        this.velZ = buffer.readFloat();

    }
    public CustomParticlePackage(float posX, float posY, float posZ, float velX, float velY, float velZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;

    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.posX);
        buffer.writeFloat(this.posY);
        buffer.writeFloat(this.posZ);
        buffer.writeFloat(this.velX);
        buffer.writeFloat(this.velY);
        buffer.writeFloat(this.velZ);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();


        // Here, this will be enqued to run on the server, only the following code will be run on the server
        context.enqueueWork(() -> {
            // SERVER SIDE FROM NOW ON
            Minecraft minecraft = Minecraft.getInstance();
            if(Objects.nonNull(minecraft.level) && minecraft.level.isClientSide()){
                minecraft.level.addParticle(ParticleTypes.SNOWFLAKE,this.posX,this.posY,this.posZ,this.velX,this.velY,this.velZ);
            }
        });
        return true;
    }

}
