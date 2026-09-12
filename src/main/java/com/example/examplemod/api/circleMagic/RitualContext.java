package com.example.examplemod.api.circleMagic;

import com.example.examplemod.api.recipe.RitualRecipe;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class RitualContext {
    @NotNull private RitualState ritualState;
    @Nullable private RitualRecipe recipe;
    @Nullable private Ritual ritualHandler;

    private int ritualProgress = 0;
}
