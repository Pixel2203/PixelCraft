package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.APIHelper;
import com.example.examplemod.API.ingredient.IngredientAPI;
import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.kettle.KettleAPI;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import com.example.examplemod.API.ritual.RitualAPI;
import com.example.examplemod.API.ritual.ModRituals;
import com.example.examplemod.API.ritual.rituals.ExtractLiveRitual;
import com.example.examplemod.API.ritual.rituals.Ritual;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.custom.ritual.chalk.GoldenChalkBlock;
import com.example.examplemod.blockentity.BlockEntityFactory;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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
    private int activeRitualProgress;
    private final int tickerInterval = 20;
    private Ritual activeRitualClass;



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
        this.activeRitualProgress = nbtCompound.getInt("ritual_progress");
        if(!StringUtil.isNullOrEmpty(activeRitual)){
            this.activeRitualClass = getRitualClass();
        }
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
        nbtCompound.putInt("ritual_progress",this.activeRitualProgress);
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
        if(Objects.isNull(activeRitualClass)){
            return;
        }
        this.activeRitualProgress = activeRitualClass.tick();
        setChanged();
        if(activeRitualClass.isFinished()){
            activeRitualClass.finishRitual();
            resetToDefault();
        }
    }
    private void resetToDefault(){
        this.currentRitualState = RitualStates.FREE;
        this.isProgressing = false;
        this.activeRitualClass = null;
        this.activeRitualProgress = 0;
        this.ingredientsSerialized = "";
        this.ticker = 0;
        setChanged();
    }

    private void handleCollectedBehaviour(){
        ModRecipe recipe = RitualAPI.getRecipeBySerializedIngredients(
                IngredientAPI.deserializeIngredientList(this.ingredientsSerialized));
        if(Objects.isNull(recipe)){
            cancelRitual();
            return;
        }
        switch (recipe.resultType()){
            case ITEM -> spawnRitualResultItem(recipe);
            case RITUAL -> performRitual((String)recipe.result());
        }
    }
    private void performRitual(String ritualID){
        this.activeRitual = ritualID;
        this.activeRitualProgress = 0;
        this.currentRitualState = RitualStates.ACTIVE;
        this.activeRitualClass = getRitualClass();
        setChanged();
    }

    private Ritual getRitualClass() {
        Ritual ritual;

        if(StringUtil.isNullOrEmpty(activeRitual)){
            return null;
        }
        switch (activeRitual){
            case ModRituals.EXTRACT_LIVE -> ritual = new ExtractLiveRitual(level, getBlockPos(), getBlockState(),this.activeRitualProgress);
            default -> ritual = null;
        }
        return ritual;
    }


    private void spawnRitualResultItem(ModRecipe<ItemStack> recipe) {
            if(level.isClientSide){
                return;
            }
            BlockPos aboveBlock = this.getBlockPos().above();
            ItemEntity itemEntity = new ItemEntity(level,aboveBlock.getX() +0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ()+0.5f,recipe.result());
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, aboveBlock.getX() + 0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ() +0.5f,0,1,1,1,1);;

    }


    public void addIngredient(ModIngredient ingredient){
        this.ingredientsSerialized = APIHelper.getNextRecipeString(this.ingredientsSerialized,ingredient);
        setChanged();
    }

    private void collectNextItem() {
        if(Objects.isNull(this.level)){
            return;
        }
        List<ItemEntity> foundEntities = ((GoldenChalkBlock)this.getBlockState().getBlock()).getItemEntitesInRangeFromBlockPos(this.level,this.getBlockPos(),3);
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
}
