package com.example.examplemod.datagen;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.sound.SoundRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    public ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ExampleMod.MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.registerSimpleSound(SoundRegistry.VIAL_FILL_SOUND);
    }

    /**
     * Erstellt einen einfachen Sound-Eintrag in der sounds.json.
     * Geht davon aus, dass die Sounddatei (.ogg) unter
     * assets/modid/sounds/<soundName>.ogg liegt.
     */
    private void registerSimpleSound(RegistryObject<SoundEvent> event) {
        // Starte den Sound-Eintrag (den Schlüssel in der JSON)
        this.add(new ResourceLocation(ExampleMod.MODID, event.getId().getPath()),
                // Füge die Sound-Definitionen hinzu
                definition()
                        // Fügt den Soundpfad hinzu (ResourceLocation: modid:soundName)
                        .with(sound(new ResourceLocation(ExampleMod.MODID, event.getId().getPath()))
                                // Optional: Lautstärke und Tonhöhe können hier angepasst werden
                                .volume(1.0F)
                                .pitch(1.0F)
                        )
        );
    }
}
