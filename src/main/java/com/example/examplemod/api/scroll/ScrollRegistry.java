package com.example.examplemod.api.scroll;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ScrollRegistry {
    private static final Map<String, Supplier<ScrollSpell>> SCROLL_SPELLS = new HashMap<>();


    private static void register(String scrollName, Supplier<ScrollSpell> implementation) {
        if(SCROLL_SPELLS.containsKey(scrollName)){
            throw new IllegalStateException("Cannot register a scroll spell (" +  scrollName + ") which already exists!");
        }
        SCROLL_SPELLS.put(scrollName, implementation);
    }

    static  {
        register(Scrolls.HEALING_SCROLL, HealingScrollSpell::new);
        register(Scrolls.CONFUSION_SCROLL, ConfusionScrollSpell::new);
        register(Scrolls.PROJECTILE_NULLIFIER_SCROLL, ProjectileBarrierScrollSpell::new);
    }

    @Nullable
    public static Supplier<ScrollSpell> get(@NotNull String scrollName) {
        return SCROLL_SPELLS.get(scrollName);
    }

}


