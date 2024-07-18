package com.example.examplemod.capabilities;

import net.minecraft.nbt.CompoundTag;

public class PlayerSoulEnergy {
    private int energy;
    private final int MAX_ENERGY = 200;
    private final int MIN_ENERGY = 0;

    public int getEnergy() {
        return energy;
    }
    public void chargeEnergy(int amount) {
        this.energy = Math.min(MAX_ENERGY, energy + amount);
    }

    public void useEnergy(int amount) {
        this.energy = Math.max(MIN_ENERGY, energy - amount);
    }

    public void copyFrom(PlayerSoulEnergy source){
        this.energy = source.energy;
    }
    public void saveNBTData(CompoundTag tag){
        tag.putInt("energy", energy);
    }
    public void loadNBTData(CompoundTag tag){
        this.energy = tag.getInt("energy");
    }
}
