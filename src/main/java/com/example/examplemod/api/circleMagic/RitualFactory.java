package com.example.examplemod.api.circleMagic;

import com.example.examplemod.api.circleMagic.rituals.ChangeTimeToDayRitual;
import com.example.examplemod.api.circleMagic.rituals.ExtractLiveRitual;
import com.example.examplemod.api.circleMagic.rituals.SimpleItemSpawnRitual;
import com.example.examplemod.api.circleMagic.rituals.UndeadCleanseRitual;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RitualFactory {

    @Nullable
    public static Ritual build(@NotNull ModRituals ritualIdentifier, int ritualProgress) {

        return switch (ritualIdentifier){
            case EXTRACT_LIVE -> new ExtractLiveRitual(ritualProgress);
            case CHANGE_TIME_TO_DAY -> new ChangeTimeToDayRitual(ritualProgress);
            case UNDEAD_CLEANSE ->  new UndeadCleanseRitual(ritualProgress);
            case TEST_RITUAL_TO_GET_HERB -> new SimpleItemSpawnRitual(ritualProgress, ModRituals.TEST_RITUAL_TO_GET_HERB, new ItemStack(ItemRegistry.GLIMMER_LEAF.get()));
            default -> null;
        };

    }
}
