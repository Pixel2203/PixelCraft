package com.example.examplemod.blockentity.custom;

import com.example.examplemod.API.APIHelper;
import com.example.examplemod.API.ingredient.IngredientAPI;
import com.example.examplemod.API.ingredient.ModIngredient;
import com.example.examplemod.API.kettle.KettleAPI;
import com.example.examplemod.API.kettle.recipe.ModRecipe;
import com.example.examplemod.API.ritual.RitualAPI;
import com.example.examplemod.API.ritual.ModRituals;
import com.example.examplemod.API.ritual.rituals.ExtractLiveRitual;
import com.example.examplemod.API.ritual.ModRitual;
import com.example.examplemod.API.ritual.rituals.RitualStates;
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

    // Current inventory of GoldenChalkBlockEntity
    private String ingredientsSerialized;
    // Saves the current ritual state
    private int currentRitualState;

    // Determines whether the entity is even counting ticks or not
    private boolean isProgressing;
    // ticker variable the counts the amount of ticks
    private int ticker;
    // Amount of ticks until the tick() method is called
    private int tickerInterval = 20;

    // Name by which the Ritual can be identified
    private String ritualID;
    // Progress of the ritual (amount of ticks it already recived)
    private int ritualProgress;
    // Instance of the Ritual Class
    private ModRitual ritualHandler;



    public GoldenChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityFactory.GoldenChalkBlockEntity, blockPos, blockState);
        setChanged();
    }

    /**
     * Loads the item into this entity
     * @param nbt
     */
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag nbtCompound = nbt.getCompound(ExampleMod.MODID);
        this.ingredientsSerialized = nbtCompound.getString("ritual_recipe");
        this.currentRitualState = nbtCompound.getInt("ritual_state");
        this.isProgressing = nbtCompound.getBoolean("isProgressing");
        this.ritualID =  nbtCompound.getString("ritual_name");
        this.ritualProgress = nbtCompound.getInt("ritual_progress");

    }

    /**
     * Saves the variables into a nbt tag
     * @param nbt
     */
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
        nbtCompound.putString("ritual_name",this.ritualID);
        nbtCompound.putInt("ritual_progress",this.ritualProgress);
        nbt.put(ExampleMod.MODID,nbtCompound);

    }

    public void initRitual() {
        if(this.currentRitualState != RitualStates.FREE){
            return;
        }
        this.currentRitualState = RitualStates.COLLECTING;
        this.ingredientsSerialized = "";
        this.isProgressing = true;
        setChanged();
    }

    private void cancelRitual() {
        //TODO Give collected Items back
        //TODO Play sound after cancel
        dropInventoryItems();
        this.currentRitualState = RitualStates.FREE;
        this.isProgressing = false;
        this.ingredientsSerialized = "";
        setChanged();
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
        if(Objects.isNull(ritualHandler)){
            this.ritualHandler = getRitualHandler();
            if(Objects.isNull(ritualHandler)){
                return;
            }
        }
        this.ritualProgress = ritualHandler.tick();
        setChanged();
        if(ritualHandler.isFinished()){
            ritualHandler.finishRitual();
            resetToDefault();
        }
    }
    private void resetToDefault() {
        this.ritualID = "";
        this.currentRitualState = RitualStates.FREE;
        this.isProgressing = false;
        this.ritualHandler = null;
        this.ritualProgress = 0;
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
        this.ritualID = ritualID;
        this.ritualProgress = 0;
        this.currentRitualState = RitualStates.ACTIVE;
        this.ritualHandler = getRitualHandler();
        setChanged();
    }



    // Helper Methods
    private ModRitual getRitualHandler() {
        ModRitual ritual;

        if(StringUtil.isNullOrEmpty(ritualID)){
            return null;
        }
        if(Objects.isNull(level)){
            return null;
        }
        switch (ritualID){
            case ModRituals.EXTRACT_LIVE -> ritual = new ExtractLiveRitual(level, getBlockPos(), getBlockState(),this.ritualProgress);
            default -> ritual = null;
        }
        return ritual;
    }
    private void dropInventoryItems(){
        if(StringUtil.isNullOrEmpty(this.ingredientsSerialized)){
            return;
        }
        BlockPos spawnPos = getBlockPos();
        List<ModIngredient> items = IngredientAPI.deserializeIngredientList(this.ingredientsSerialized);
        items.parallelStream()
                .map(item -> new ItemStack(item.item()))
                .forEach(itemStack -> APIHelper.spawnItemEntity(level,spawnPos,itemStack,Vec3.ZERO));
        this.ingredientsSerialized = "";
    }
    private void spawnRitualResultItem(ModRecipe<ItemStack> recipe) {
            if(level.isClientSide()){
                return;
            }
            BlockPos aboveBlock = this.getBlockPos().above();
            APIHelper.spawnItemEntity(level,aboveBlock,recipe.result(),Vec3.ZERO);
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, aboveBlock.getX() + 0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ() +0.5f,0,1,1,1,1);;
    }

    /**
     * Adds an ItemStack to the current GoldenChalkBlock inventory and shrinks it by one!
     * @param itemStack
     * @param ingredient
     */
    private void addIngredientFromGround(ItemStack itemStack , ModIngredient ingredient){
        itemStack.shrink(1);
        this.ingredientsSerialized = APIHelper.getNextRecipeString(this.ingredientsSerialized,ingredient);
        setChanged();
    }


    /**
     * Searches for near ItemEntities and adds them to the GoldenChalkBlock Inventory
     * Only adds ingredients that has an ingredient Tag!
     */
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
        addIngredientFromGround(chosenEntity.getItem(), ingredient);
    }




}
