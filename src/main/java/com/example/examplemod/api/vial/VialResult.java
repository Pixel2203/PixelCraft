package com.example.examplemod.api.vial;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public record VialResult(boolean vialSuccess, @Nullable VialType extracted) {

    public static VialResult failed() {
        return new VialResult(false, null);
    }

    public static VialResult success(@NotNull VialType extracted) {
        return new VialResult(true, extracted);
    }
}
