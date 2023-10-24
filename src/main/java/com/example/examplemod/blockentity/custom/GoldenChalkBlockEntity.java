package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.kettle.KettleAPI;
import com.example.examplemod.API.kettle.recipe.KettleRecipeFactory;
import com.example.examplemod.API.kettle.records.KettleIngredient;
import com.example.examplemod.API.kettle.records.KettleRecipe;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.custom.kettle.KettleBlock;
import com.example.examplemod.blockentity.BlockEntityFactory;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class GoldenChalkBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private String kettleIngredientsSerialized;
    private boolean isProgressing;
    private int progress;
    public GoldenChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityFactory.KettleBlockEntity, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag nbtCompound = nbt.getCompound(ExampleMod.MODID);
        this.kettleIngredientsSerialized = nbtCompound.getString("recipe");
        this.isProgressing = nbtCompound.getBoolean("isProgressing");
        this.progress = nbtCompound.getInt("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(this.kettleIngredientsSerialized != null){
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putString("recipe", this.kettleIngredientsSerialized);
            nbtCompound.putBoolean("isProgressing", this.isProgressing);
            nbtCompound.putInt("progress", this.progress);
            nbt.put(ExampleMod.MODID,nbtCompound);
        }

    }
    @Override
    public void tick() {

    }
}
