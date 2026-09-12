package com.example.examplemod.api.distilleryBowl;

import lombok.Getter;

public enum ModFluids {
    WATER(0x3F76E4),
    SOUL_DEW(0x3F488fE0),
    SHIMMER_ESSENCE(0x3Fe0792b),
    WEIRD_STEW(0xAC22E4);
    @Getter
    private int hexColor;
    ModFluids(int hexColor) {

    }
}
