package com.example.examplemod.api.vial;

import com.example.examplemod.api.distilleryBowl.ModFluids;
import org.jetbrains.annotations.NotNull;

public record VialResult(boolean vialSuccess, ModFluids extracted) {

    public static VialResult failed() {
        return new VialResult(false, null);
    }

    public static VialResult success(@NotNull ModFluids extracted) {
        return new VialResult(true, extracted);
    }
}
