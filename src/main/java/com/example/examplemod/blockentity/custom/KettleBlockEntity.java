package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.kettle.KettleAPI;
import com.example.examplemod.API.kettle.recipe.KettleRecipeFactory;
import com.example.examplemod.API.kettle.records.KettleIngredient;
import com.example.examplemod.API.kettle.records.KettleRecipe;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.custom.kettle.KettleBlock;
import com.example.examplemod.blockentity.BlockEntityFactory;
import com.example.examplemod.registry.BlockEntityRegistry;
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

public class KettleBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private String kettleIngredientsSerialized;
    private boolean isProgressing;
    private int progress;
    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
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
            spawnResultOfRecipeOnKettle(KettleAPI.getRecipeBySerializedIngredientList(this.kettleIngredientsSerialized));
            setChanged();
        }

        if(isProgressing){
            this.progress++;
            setChanged();
        }else {
            if(Objects.isNull(this.level) || this.level.isClientSide() ){
                return;
            }
            boolean isFireBelow = ((KettleBlock)this.getBlockState().getBlock()).isFireBelow(this.level,this.getBlockPos());
            boolean isMaxFluidLevel = this.getBlockState().getValue(KettleBlock.fluid_level) == KettleBlock.MAX_FLUID_LEVEL;
            if(!isFireBelow || !isMaxFluidLevel){
                return;
            }
            BlockPos aboveBlock = this.getBlockPos().above();
            Random random = new Random();
            int bubbleChange = random.nextInt(30);
            if(bubbleChange <= 25){
                return;
            }
            ((ServerLevel)level).sendParticles(ParticleTypes.BUBBLE_POP,aboveBlock.getX() + 0.5f, aboveBlock.getY(), aboveBlock.getZ()+0.5f,1,0.2,0,0.2,0);

        }
    }
    private void spawnResultOfRecipeOnKettle(
            @NotNull KettleRecipe recipe


    ){
        if(this.getBlockState().getValue(KettleBlock.fluid_level) == KettleBlock.MAX_FLUID_LEVEL && !level.isClientSide()){
            BlockPos aboveBlock = this.getBlockPos().above();
            ItemEntity itemEntity = new ItemEntity(level,aboveBlock.getX() +0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ()+0.5f,recipe.result());
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
            level.setBlock(this.getBlockPos(),this.getBlockState().setValue(KettleBlock.fluid_level,KettleBlock.MIN_FLUID_LEVEL),3);
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, aboveBlock.getX() + 0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ() +0.5f,0,1,1,1,1);
            this.resetContent();
        }
    }
    public boolean isProgressing() {
        return this.isProgressing;
    }
}
