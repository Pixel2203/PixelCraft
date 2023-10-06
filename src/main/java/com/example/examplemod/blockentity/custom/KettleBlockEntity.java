package com.example.examplemod.blockentity.custom;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blockentity.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class KettleBlockEntity extends BlockEntity {
    private int counter;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag tutorialmodData = nbt.getCompound(ExampleMod.MODID);
        this.counter = tutorialmodData.getInt("Counter");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag tutorialmodData = new CompoundTag();
        tutorialmodData.putInt("Counter", this.counter);
        nbt.put(ExampleMod.MODID,tutorialmodData);
    }
    public int incrementCounter(){
        this.counter++;
        setChanged();
        return this.counter;
    }
}
