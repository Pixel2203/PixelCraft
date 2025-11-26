package com.example.examplemod.sound;

import com.example.examplemod.ExampleMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExampleMod.MODID);

    public static final RegistryObject<SoundEvent> VIAL_FILL_SOUND = registerSoundEvent("vial_fill");

    private static RegistryObject<SoundEvent> registerSoundEvent(String sound) {
        return SOUNDS.register(sound, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExampleMod.MODID, sound)));
    }

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

}
