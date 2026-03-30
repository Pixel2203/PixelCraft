package com.example.examplemod.api.vial;

import org.jetbrains.annotations.NotNull;

public record VialResult(boolean vialSuccess, VialType extracted) {

    public static VialResult failed() {
        return new VialResult(false, null);
    }

    public static VialResult success(@NotNull VialType extracted) {
        return new VialResult(true, extracted);
    }
}
