package com.example.examplemod.blockentity.entities;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.ingredient.IngredientAPI;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeAPI;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.blocks.KettleBlock;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class KettleBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private List<Item> ingredients = new ArrayList<>();
    private boolean isProgressing;
    private int ticker;


    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag nbtCompound = nbt.getCompound(ExampleMod.MODID);
        this.ingredients = IngredientAPI.deserializeIngredientList(nbtCompound.getString(CustomNBTTags.RECIPE));
        this.isProgressing = nbtCompound.getBoolean(CustomNBTTags.IS_PROGRESSING);
        this.ticker = nbtCompound.getInt(CustomNBTTags.TICKER);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putString(CustomNBTTags.RECIPE, IngredientAPI.serializeIngredients(ingredients));
        nbtCompound.putBoolean(CustomNBTTags.IS_PROGRESSING, this.isProgressing);
        nbtCompound.putInt(CustomNBTTags.TICKER, this.ticker);
        nbt.put(ExampleMod.MODID,nbtCompound);


    }
    public void add(Item item){
        ingredients.add(item);
        setChanged();
    }
    public void resetContent(){
        this.ingredients.clear();
    }
    public List<Item> getKettleIngredients(){
        return this.ingredients;
    }
    public void startBrewing(){
        if(ingredients.isEmpty()){
            return;
        }
        this.isProgressing = true;
        this.ticker = 0;
        setChanged();
    }

    @Override
    public void tick() {
        BlockState blockState = getBlockState();
        boolean isFireBelow = ((KettleBlock) blockState.getBlock()).isFireBelow(this.level, this.getBlockPos());
        boolean isAlreadyBoiling = blockState.getValue(KettleBlock.isBoiling);

        if (isFireBelow != isAlreadyBoiling) {
            level.setBlock(getBlockPos(), blockState.setValue(KettleBlock.isBoiling, isFireBelow), 3);
        }

        if (isProgressing) {
            ticker++;
            setChanged();

            if (ticker >= 40) {
                ticker = 0;
                isProgressing = false;
                Optional<ModRecipe<?>> recipeOptional = RecipeAPI.getRecipeBySerializedIngredients(RecipeAPI.RecipeOrigins.KETTLE,this.ingredients);
                if(recipeOptional.isPresent()){
                    spawnResultOfRecipeOnKettle((ModRecipe<ItemStack>) recipeOptional.get());
                    setChanged();
                }

            }
        }

        if (Objects.isNull(level) || level.isClientSide()) {
            return;
        }

        boolean isMaxFluidLevel = blockState.getValue(KettleBlock.fluid_level) == KettleBlock.MAX_FLUID_LEVEL;

        if (isFireBelow && isMaxFluidLevel && !blockState.getValue(KettleBlock.isMixture)) {
            spawnBubbles();
        }
    }
    private void spawnBubbles(){
        BlockPos aboveBlock = this.getBlockPos().above();
        Random random = new Random();
        int bubbleChange = random.nextInt(30);
        if(bubbleChange <= 25){
            return;
        }
        Vec3 particlePos = ModUtils.calcCenterOfBlock(aboveBlock);
        ((ServerLevel)level).sendParticles(ParticleTypes.BUBBLE_POP,particlePos.x, particlePos.y - 0.5d, particlePos.z,1,0.2,0,0.2,0);

    }


    private void spawnResultOfRecipeOnKettle(@NotNull ModRecipe<ItemStack> recipe){
        if(this.getBlockState().getValue(KettleBlock.fluid_level) == KettleBlock.MAX_FLUID_LEVEL && !level.isClientSide()){
            BlockPos aboveBlock = this.getBlockPos().above();
            APIHelper.spawnItemEntity(level, ModUtils.calcCenterOfBlock(aboveBlock),recipe.result().get(),Vec3.ZERO);
            level.setBlock(this.getBlockPos(),this.getBlockState().setValue(KettleBlock.fluid_level,KettleBlock.MIN_FLUID_LEVEL),3);
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, aboveBlock.getX() + 0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ() +0.5f,0,1,1,1,1);
            this.resetContent();
        }
    }
    public boolean isProgressing() {
        return this.isProgressing;
    }
}
