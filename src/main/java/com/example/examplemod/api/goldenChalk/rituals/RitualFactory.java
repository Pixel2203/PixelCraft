package com.example.examplemod.api.goldenChalk.rituals;

import com.example.examplemod.api.goldenChalk.rituals.util.ModRitual;
import com.example.examplemod.api.goldenChalk.rituals.util.ModRituals;
import org.jetbrains.annotations.NotNull;

public class RitualFactory {

    public static ModRitual build(@NotNull ModRituals ritualIdentifier, int ritualProgress) {
        ModRitual ritual;

        switch (ritualIdentifier){
            case EXTRACT_LIVE -> ritual = new ExtractLiveRitual(ritualProgress);
            case CHANGE_TIME_TO_DAY -> ritual = new ChangeTimeToDayRitual(ritualProgress);
            default -> ritual = null;
        }
        return ritual;
    }
}
