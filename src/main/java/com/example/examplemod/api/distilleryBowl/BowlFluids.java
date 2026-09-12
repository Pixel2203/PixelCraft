package com.example.examplemod.api.distilleryBowl;

import lombok.Getter;

public enum BowlFluids {
    WATER(0x3F76E4),
    SOUL_DEW(0x3F488fE0),
    SHIMMER_ESSENCE(0x3Fe0792b);

    @Getter
    private int hexColor;
    BowlFluids(int hexColor) {

    }
}
