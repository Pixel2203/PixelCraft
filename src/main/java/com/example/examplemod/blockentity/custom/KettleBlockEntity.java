package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipeFactory;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends BlockEntity {
    private String kettleIngredientsSerialized;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag tutorialmodData = nbt.getCompound(ExampleMod.MODID);
        this.kettleIngredientsSerialized = tutorialmodData.getString("recipe");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(this.kettleIngredientsSerialized != null){
            CompoundTag tutorialmodData = new CompoundTag();
            tutorialmodData.putString("recipe", this.kettleIngredientsSerialized);
            nbt.put(ExampleMod.MODID,tutorialmodData);
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
}
