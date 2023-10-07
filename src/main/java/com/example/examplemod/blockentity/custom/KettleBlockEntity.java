package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.brewing.kettle.recipe.KettleRecipeFactory;
import com.example.examplemod.API.brewing.kettle.records.KettleIngredient;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.blockentity.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends BlockEntity {
    private String recipe;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag tutorialmodData = nbt.getCompound(ExampleMod.MODID);
        this.recipe = tutorialmodData.getString("recipe");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(this.recipe != null){
            CompoundTag tutorialmodData = new CompoundTag();
            tutorialmodData.putString("recipe", this.recipe);
            nbt.put(ExampleMod.MODID,tutorialmodData);
        }

    }
    public void add(KettleIngredient ingredient){
        this.recipe = KettleRecipeFactory.getNextRecipeString(this.recipe,ingredient);

        setChanged();
    }
    public void resetContent(){
        this.recipe = null;
    }
    public String getSerializedKettleRecipe(){
        return this.recipe;
    }
}
