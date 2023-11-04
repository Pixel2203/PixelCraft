package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.APIHelper;
import com.example.examplemod.API.ingredient.IngredientAPI;
import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.kettle.KettleAPI;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import com.example.examplemod.API.ritual.RitualAPI;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.blockentity.BlockEntityFactory;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Objects;

public class GoldenChalkBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private interface RitualStates {
        int FREE = 0;
        int COLLECTING = 1;
        int COLLECTED = 2;
    }
    private String ingredientsSerialized;
    private int currentRitualState;
    private int ticker;
    private final int tickerInterval = 20;

    public GoldenChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityFactory.GoldenChalkBlockEntity, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag nbtCompound = nbt.getCompound(ExampleMod.MODID);
        this.ingredientsSerialized = nbtCompound.getString("recipe");
        this.currentRitualState = nbtCompound.getInt("state");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(this.ingredientsSerialized != null){
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putString("recipe", this.ingredientsSerialized);
            nbtCompound.putInt("state",this.currentRitualState);
            nbt.put(ExampleMod.MODID,nbtCompound);
        }
    }
    @Override
    public void tick() {
        ticker++;
        setChanged();
        if(ticker < tickerInterval){
            return;
        }
        ticker = 0;
        setChanged();
        switch (this.currentRitualState){
            case RitualStates.COLLECTING -> {
                collectNextItem();
            }
            case RitualStates.COLLECTED -> {
                ModRecipe recipe = RitualAPI.getRecipeBySerializedIngredients(
                        IngredientAPI.deserializeIngredientList(this.ingredientsSerialized));
                if(Objects.isNull(recipe)){
                    cancelRitual();
                    break;
                }
                System.out.println("GEKLAPPT!");
            }
            default -> {
                return;
            }
        }
        setChanged();
    }
    public void addIngredient(ModIngredient ingredient){
        this.ingredientsSerialized = APIHelper.getNextRecipeString(this.ingredientsSerialized,ingredient);
        setChanged();
    }

    private void collectNextItem() {
        if(Objects.isNull(this.level)){
            return;
        }
        List<ItemEntity> foundEntities = getItemEntitesInRangeFromBlockPos(this.level,this.getBlockPos(),3);
        if(foundEntities.size() == 0){
            this.currentRitualState = RitualStates.COLLECTED;
            return;
        }
        ItemEntity choosenEntity = foundEntities.get(0);
        if(!KettleAPI.hasIngredientTag(choosenEntity.getItem())){
            cancelRitual();
            return;
        }
        ModIngredient ingredient = IngredientAPI.getIngredientByItem(choosenEntity.getItem().getItem());
        if(Objects.isNull(ingredient)){
            cancelRitual();
            return;
        }
        if(ingredientsSerialized.contains(ingredient.id())){
            cancelRitual();
            return;
        }
        choosenEntity.getItem().shrink(1);
        addIngredient(ingredient);
    }

    private void cancelRitual() {
        //TODO Give collected Items back
        //TODO Play sound after cancel
        this.currentRitualState = RitualStates.FREE;
        setChanged();
    }

    public void startRitual() {
        if(!(currentRitualState == RitualStates.FREE)){
          return ;
        }
        currentRitualState = RitualStates.COLLECTING;
        this.ingredientsSerialized ="mein test";
        ticker = 0;
        setChanged();
    }
    private List<ItemEntity> getItemEntitesInRangeFromBlockPos(Level level, BlockPos blockPos, int range){
        AABB box = new AABB(blockPos).inflate(range,range,range);
        return level.getEntitiesOfClass(ItemEntity.class,box);
    }
}
