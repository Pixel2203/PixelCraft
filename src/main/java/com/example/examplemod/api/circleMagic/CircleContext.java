package com.example.examplemod.api.circleMagic;

import com.example.examplemod.api.circleMagic.rituals.Ritual;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.api.recipe.ModRecipe;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class CircleContext {
    @NotNull private RitualState ritualState;
    @Nullable private ModRecipe<?> recipe;
    @Nullable private Ritual ritualHandler;

    private int ritualProgress = 0;
}
