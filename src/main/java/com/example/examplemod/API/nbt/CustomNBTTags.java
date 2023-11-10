package com.example.examplemod.API.nbt;

import com.example.examplemod.ExampleMod;

public interface CustomNBTTags {
    String POTION_LEVEL = ExampleMod.MODID + "_potion_level";
    String POTION_DURATION = ExampleMod.MODID + "_potion_effectDuration";
    String POTION_AMPLIFIER = ExampleMod.MODID + "_potion_effectAmplifier";
    String POTION_BOUNDS = ExampleMod.MODID + "_potion_bounds";



    // BlockEntites
    String RECIPE = "recipe";
    String PROGRESS = "progress";
    String TICKER  = "ticker";
    String IS_PROGRESSING = "isProgressing";
    String RITUAL_STATE = "ritual_state";
    String RITUAL_NAME = "ritual_name";


}
