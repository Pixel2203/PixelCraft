package com.example.examplemod.blockentity.custom;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blockentity.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class KettleBlockEntity extends BlockEntity {
    private ItemStack item;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag tutorialmodData = nbt.getCompound(ExampleMod.MODID);
        this.item = ItemStack.of(tutorialmodData.getCompound("item"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(this.item != null){
            CompoundTag tutorialmodData = new CompoundTag();
            tutorialmodData.put("item", this.item.serializeNBT());
            nbt.put(ExampleMod.MODID,tutorialmodData);
        }

    }
    public void saveNewItem(ItemStack item){
        this.item = item;
        setChanged();
    }
    public ItemStack getCurrentSave(){
        return this.item;
    }
}
