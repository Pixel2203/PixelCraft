package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.brewing.kettle.KettleAPI;
import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipeFactory;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.API.brewing.kettle.records.KettleRecipe;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.custom.KettleBlock;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.particle.ParticleFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class KettleBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private String kettleIngredientsSerialized;
    private boolean isProgressing;
    private int progress;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
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
    public void add(KettleIngredient ingredient){
        this.kettleIngredientsSerialized = KettleRecipeFactory.getNextRecipeString(this.kettleIngredientsSerialized,ingredient);

        setChanged();
    }
    public void resetContent(){
        this.kettleIngredientsSerialized = null;
    }
    public String getSerializedKettleRecipe(){
        return this.kettleIngredientsSerialized;
    }
    public void startBrewing(){
        if(StringUtil.isNullOrEmpty(this.kettleIngredientsSerialized)){
            return;
        }
        this.isProgressing = true;
        this.progress = 0;
        setChanged();
    }

    @Override
    public void tick() {
        if(isProgressing && progress >= 40){
            progress = 0;
            isProgressing = false;
            spawnResultOfRecipeOnKettle(level, this.getBlockPos(),this.getBlockState(), this, KettleAPI.getRecipeBySerializedIngredientList(this.kettleIngredientsSerialized));
            setChanged();
        }

        if(isProgressing){
            this.progress++;
            setChanged();
        }
    }
    private void spawnResultOfRecipeOnKettle(
            Level level,
            BlockPos blockPos,
            BlockState blockState,
            KettleBlockEntity blockEntity,
            @NotNull KettleRecipe recipe


    ){
        if(blockState.getValue(KettleBlock.fluid_level) == KettleBlock.MAX_FLUID_LEVEL && !level.isClientSide()){
            ItemEntity itemEntity = new ItemEntity(level,blockPos.getX(),blockPos.above().getY(),blockPos.getZ(),recipe.result());
            level.addFreshEntity(itemEntity);
            level.setBlock(blockPos,blockState.setValue(KettleBlock.fluid_level,KettleBlock.MIN_FLUID_LEVEL),3);
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, blockPos.getX(),blockPos.getY(),blockPos.getZ(),0,2,1,1,1);
            blockEntity.resetContent();
        }
    }
    public boolean isProgressing() {
        return this.isProgressing;
    }
}
