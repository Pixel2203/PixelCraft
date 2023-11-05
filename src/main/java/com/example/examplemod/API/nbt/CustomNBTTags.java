package com.example.examplemod.API.nbt;

import com.example.examplemod.ExampleMod;

public interface CustomNBTTags {
    String POTION_LEVEL = ExampleMod.MODID + "_potion_level";
    String POTION_DURATION = ExampleMod.MODID + "_potion_effectDuration";
    String POTION_AMPLIFIER = ExampleMod.MODID + "_potion_effectAmplifier";
    String POTION_BOUNDS = ExampleMod.MODID + "_potion_bounds";



    // BlockEntites
    String RITUAL_RECIPE = "ritual_recipe";
    String RITUAL_STATE = "ritual_state";
    String RITUAL_NAME = "ritual_name";
    String RITUAL_PROGRESS = "ritual_progress";
    String IS_PROGRESSING = "isProgressing";

}
