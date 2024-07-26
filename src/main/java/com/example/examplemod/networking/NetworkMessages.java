package com.example.examplemod.networking;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.networking.packets.CustomParticlePackage;
import com.example.examplemod.networking.packets.ExampleC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.sql.ResultSet;

public class NetworkMessages {
    private static SimpleChannel COMMUNICATION_CHANNEL;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void registerChannel() {
       COMMUNICATION_CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ExampleMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

       registerPackages();
    }


    private static void registerPackages() {
        COMMUNICATION_CHANNEL.messageBuilder(ExampleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ExampleC2SPacket::new)
                .encoder(ExampleC2SPacket::toBytes)
                .consumerMainThread(ExampleC2SPacket::handle)
                .add();
        COMMUNICATION_CHANNEL.messageBuilder(CustomParticlePackage.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CustomParticlePackage::new)
                .encoder(CustomParticlePackage::toBytes)
                .consumerMainThread(CustomParticlePackage::handle)
                .add();
    }


    public static <MSG> void sendToServer(MSG message) {
        COMMUNICATION_CHANNEL.sendToServer(message);
    }
    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        COMMUNICATION_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message );
    }
}
