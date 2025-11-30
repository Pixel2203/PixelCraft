package com.example.examplemod.api.vial;

import lombok.Getter;

public enum VialType {
    SOUL_DEW(0x3F488fE0),
    SHIMMER_ESSENCE(0x3Fe0792b);

    @Getter
    private final int hexColor;
    VialType(int hexColor) {
        this.hexColor = hexColor;
    }
}