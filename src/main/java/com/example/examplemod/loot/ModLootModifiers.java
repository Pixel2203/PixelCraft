package com.example.examplemod.loot;

import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ExampleMod.MODID);


    public static RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM  = LOOT_MODIFIER_SERIALIZER.register("add_item", AddItemModifier.CODEC);
    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZER.register(bus);
    }
}
