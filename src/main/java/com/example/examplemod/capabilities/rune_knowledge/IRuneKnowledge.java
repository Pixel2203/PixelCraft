package com.example.examplemod.capabilities.rune_knowledge;

import com.example.examplemod.api.runes.Runes;

import java.util.Set;

public interface IRuneKnowledge {
    boolean hasRune(Runes type);
    void unlockRune(Runes type);
    Set<Runes> getUnlockedRunes();
}
