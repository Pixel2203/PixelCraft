package com.example.examplemod.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSoulEnergyProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerSoulEnergy> PLAYER_SOUL_ENERGY = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerSoulEnergy soulEnergy = null;
    private final LazyOptional<PlayerSoulEnergy> optional = LazyOptional.of(this::createPlayerSoulEnergy);

    private PlayerSoulEnergy createPlayerSoulEnergy() {
        if(soulEnergy == null) {
            soulEnergy = new PlayerSoulEnergy();
        }
        return soulEnergy;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_SOUL_ENERGY){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSoulEnergy().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSoulEnergy().loadNBTData(nbt);
    }
}
