package com.example.examplemod.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ExampleC2SPacket {
    public ExampleC2SPacket(FriendlyByteBuf buffer) {
    }
    public ExampleC2SPacket() {

    }

    public void toBytes(FriendlyByteBuf buffer) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();


        // Here, this will be enqued to run on the server, only the following code will be run on the server
        context.enqueueWork(() -> {
            // SERVER SIDE FROM NOW ON
            ServerPlayer player = supplier.get().getSender();
            if(Objects.isNull(player)){
                return;
            }
            ServerLevel level = player.serverLevel();
            Entity cow = new Cow(EntityType.COW, level);
            level.addFreshEntity(cow);
        });
        return true;
    }

}
