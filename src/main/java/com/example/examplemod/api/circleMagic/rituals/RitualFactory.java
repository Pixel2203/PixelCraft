package com.example.examplemod.api.circleMagic.rituals;

import org.jetbrains.annotations.NotNull;

public class RitualFactory {

    public static ModRitual build(@NotNull ModRituals ritualIdentifier, int ritualProgress) {

        return switch (ritualIdentifier){
            case EXTRACT_LIVE -> new ExtractLiveRitual(ritualProgress);
            case CHANGE_TIME_TO_DAY -> new ChangeTimeToDayRitual(ritualProgress);
            case UNDEAD_CLEANSE ->  new UndeadCleanseRitual(ritualProgress);
        };

    }
}
