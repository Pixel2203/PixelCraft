package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.APIHelper;
import com.example.examplemod.API.ingredient.IngredientAPI;
import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.kettle.KettleAPI;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import com.example.examplemod.API.result.ResultTypes;
import com.example.examplemod.API.ritual.RitualAPI;
import com.example.examplemod.API.ritual.ModRituals;
import com.example.examplemod.API.ritual.rituals.ExtractLiveRitual;
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
        int ACTIVE = 3;
    }
    private String ingredientsSerialized;
    private int currentRitualState;
    private int ticker;
    private boolean isProgressing;
    private String activeRitual;
    private byte activeRitualProgress;
    private final int tickerInterval = 20;




    public GoldenChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityFactory.GoldenChalkBlockEntity, blockPos, blockState);
        setChanged();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag nbtCompound = nbt.getCompound(ExampleMod.MODID);
        this.ingredientsSerialized = nbtCompound.getString("ritual_recipe");
        this.currentRitualState = nbtCompound.getInt("ritual_state");
        this.isProgressing = nbtCompound.getBoolean("isProgressing");
        this.activeRitual =  nbtCompound.getString("ritual_name");
        this.activeRitualProgress = nbtCompound.getByte("ritual_progress");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(this.ingredientsSerialized == null){
            return;
        }
        CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putString("ritual_recipe", this.ingredientsSerialized);
        nbtCompound.putInt("ritual_state",this.currentRitualState);
        nbtCompound.putBoolean("isProgressing",this.isProgressing);
        nbtCompound.putString("ritual_name",this.activeRitual);
        nbtCompound.putByte("ritual_progress",this.activeRitualProgress);
        nbt.put(ExampleMod.MODID,nbtCompound);

    }
    @Override
    public void tick() {
        if(!isProgressing){
            return;
        }
        ticker++;
        setChanged();

        if(ticker < tickerInterval){
            return;
        }

        switch (this.currentRitualState){
            case RitualStates.COLLECTING -> collectNextItem();
            case RitualStates.COLLECTED -> handleCollectedBehaviour();
            case RitualStates.ACTIVE -> activeRitualTick();
        }
        ticker = 0;
        setChanged();
    }

    private void activeRitualTick() {
        switch (this.activeRitual){
            case ModRituals.EXTRACT_LIVE -> ExtractLiveRitual.tick();
        }
    }

    private void handleCollectedBehaviour(){
        ModRecipe recipe = RitualAPI.getRecipeBySerializedIngredients(
                IngredientAPI.deserializeIngredientList(this.ingredientsSerialized));
        if(Objects.isNull(recipe)){
            cancelRitual();
            return;
        }
        if(recipe.resultType() == ResultTypes.ITEM){
            spawnRitualResultItem(recipe);
            return;
        }
        if(recipe.resultType() == ResultTypes.RITUAL){
           performRitual((String)recipe.result());
            return;
        }
        if(recipe.resultType() == ResultTypes.ITEM){
            spawnRitualResultItem(recipe);
        }
    }
    private void performRitual(String ritualID){
        this.activeRitual = ritualID;
        this.activeRitualProgress = 0;
        this.currentRitualState = RitualStates.ACTIVE;
        setChanged();
    }


    private void spawnRitualResultItem(ModRecipe recipe) {
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
        ItemEntity chosenEntity = foundEntities.get(0);
        if(!KettleAPI.hasIngredientTag(chosenEntity.getItem())){
            cancelRitual();
            return;
        }
        ModIngredient ingredient = IngredientAPI.getIngredientByItem(chosenEntity.getItem().getItem());
        if(Objects.isNull(ingredient)){
            cancelRitual();
            return;
        }
        if(ingredientsSerialized.contains(ingredient.id())){
            cancelRitual();
            return;
        }
        chosenEntity.getItem().shrink(1);
        addIngredient(ingredient);
    }

    private void cancelRitual() {
        //TODO Give collected Items back
        //TODO Play sound after cancel
        this.currentRitualState = RitualStates.FREE;
        this.isProgressing = false;
        setChanged();
    }

    public void startRitual() {
        if(this.currentRitualState != RitualStates.FREE){
            return;
        }
        this.currentRitualState = RitualStates.COLLECTING;
        this.ingredientsSerialized = "";
        this.isProgressing = true;
        setChanged();
    }
    private List<ItemEntity> getItemEntitesInRangeFromBlockPos(Level level, BlockPos blockPos, int range){
        AABB box = new AABB(blockPos).inflate(range,range,range);
        return level.getEntitiesOfClass(ItemEntity.class,box);
    }
}
