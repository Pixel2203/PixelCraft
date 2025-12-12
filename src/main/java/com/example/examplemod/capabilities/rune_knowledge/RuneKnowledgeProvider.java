package com.example.examplemod.capabilities.rune_knowledge;

import com.example.examplemod.api.runes.Runes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class RuneKnowledgeProvider implements ICapabilitySerializable<Tag> {


    public static final Capability<RuneKnowledge> RUNE_CAP =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final RuneKnowledge backend = new RuneKnowledge();
    private final LazyOptional<RuneKnowledge> optional = LazyOptional.of(() -> backend);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == RUNE_CAP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        ListTag list = new ListTag();
        for (Runes type : backend.getUnlockedRunes()) {
            list.add(StringTag.valueOf(type.name()));
        }
        return list;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        backend.getUnlockedRunes().clear();
        ListTag list = (ListTag) nbt;

        for (Tag t : list) {
            Runes type = Runes.valueOf(t.getAsString());
            backend.unlockRune(type);
        }
    }
}
