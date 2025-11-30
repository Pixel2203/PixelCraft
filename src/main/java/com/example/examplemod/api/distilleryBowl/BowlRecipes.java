package com.example.examplemod.api.distilleryBowl;

import com.example.examplemod.api.vial.VialType;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
@Getter
public enum BowlRecipes {
    WATER(0x3F76E4, null),
    GREEN_SOUP(0x3F00FF00, () -> VialType.SHIMMER_ESSENCE),;


    private final int color;
    @Nullable
    private final Supplier<VialType> vialTypeSupplier;
    BowlRecipes(int color, @Nullable Supplier<VialType> supplier) {
        this.color = color;
        this.vialTypeSupplier = supplier;

    }
}
