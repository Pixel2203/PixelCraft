package com.example.examplemod.capabilities.rune_knowledge;

import com.example.examplemod.api.runes.Runes;

import java.util.HashSet;
import java.util.Set;

public class RuneKnowledge implements IRuneKnowledge {

    private final Set<Runes> unlocked = new HashSet<>();

    @Override
    public boolean hasRune(Runes type) {
        return unlocked.contains(type);
    }

    @Override
    public void unlockRune(Runes type) {
        unlocked.add(type);
    }

    @Override
    public Set<Runes> getUnlockedRunes() {
        return unlocked;
    }
}
