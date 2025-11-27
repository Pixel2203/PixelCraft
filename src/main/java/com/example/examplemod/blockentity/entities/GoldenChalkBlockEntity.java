package com.example.examplemod.blockentity.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.api.result.ResultTypes;
import com.example.examplemod.api.ritual.rituals.ChangeTimeToDayRitual;
import com.example.examplemod.api.ritual.rituals.ExtractLiveRitual;
import com.example.examplemod.api.ritual.util.ModRitual;
import com.example.examplemod.api.ritual.util.ModRituals;
import com.example.examplemod.api.ritual.util.RitualStates;
import com.example.examplemod.block.blocks.GoldenChalkBlock;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GoldenChalkBlockEntity extends BlockEntity implements ITickableBlockEntity {

    // Current inventory of GoldenChalkBlockEntity
    private final NonNullList<ItemStack> ingredients = NonNullList.create();
    // Saves the current ritual state
    private int currentRitualState;

    // Determines whether the entity is even counting ticks or not
    private boolean isProgressing;
    // ticker variable the counts the amount of ticks
    private int ticker;
    // Amount of ticks until the tick() method is called
    private final int tickerInterval = 20;

    // Name by which the Ritual can be identified
    private ModRituals ritual;
    // Progress of the ritual (amount of ticks it already recived)
    private int ritualProgress;
    // Instance of the Ritual Class
    private ModRitual ritualHandler;



    public GoldenChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.GOLDEN_CHALK_BLOCK_ENTITY.get(), blockPos, blockState);
        setChanged();
    }

    /**
     * Loads the item into this entity
     */
    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        CompoundTag modCompound = nbt.getCompound(ExampleMod.MODID);
        CompoundTag itemsTag = modCompound.getCompound("items");
        ContainerHelper.loadAllItems(itemsTag, ingredients);
        this.currentRitualState = modCompound.getInt(CustomNBTTags.RITUAL_STATE);
        this.isProgressing = modCompound.getBoolean(CustomNBTTags.IS_PROGRESSING);
        this.ritualProgress = modCompound.getInt(CustomNBTTags.PROGRESS);
        this.ticker = modCompound.getInt(CustomNBTTags.TICKER);

        var ritualName = modCompound.getString(CustomNBTTags.RITUAL_NAME);
        if(EnumUtils.isValidEnum(ModRituals.class, ritualName)) this.ritual = ModRituals.valueOf(ritualName);

    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag modCompound = new CompoundTag();
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, ingredients);
        modCompound.putInt(CustomNBTTags.RITUAL_STATE,this.currentRitualState);
        modCompound.putBoolean(CustomNBTTags.IS_PROGRESSING,this.isProgressing);
        modCompound.putInt(CustomNBTTags.PROGRESS,this.ritualProgress);
        modCompound.putInt(CustomNBTTags.TICKER,this.ticker);
        modCompound.putString(CustomNBTTags.RITUAL_NAME,Objects.isNull(this.ritual) ? "" : this.ritual.name());
        modCompound.put("items", itemsTag);
        nbt.put(ExampleMod.MODID,modCompound);


    }

    public void clickRitualBlock() {
        if(this.currentRitualState != RitualStates.FREE){
            return;
        }
        this.currentRitualState = RitualStates.COLLECTING;
        this.ingredients.clear();
        this.isProgressing = true;
        setChanged();
    }

    private void cancelRitual() {
        if(Objects.isNull(level)){
            return;
        }
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS,0.25f,1f);
        dropInventoryItems();
        resetToDefault();

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


        // Spawns Particles
        var spawnParticlePosition = getBlockPos().above();
        Random random = new Random();
        int amountOfParticles = random.nextInt(1,4);
        ModUtils.sendParticles((ServerLevel) getLevel(), ParticleTypes.FLAME, spawnParticlePosition,1f, amountOfParticles,0.4f,0,0.4f,0);

        setChanged();
    }

    private void activeRitualTick() {
        if(Objects.isNull(ritualHandler)){
            this.ritualHandler = getRitualHandler(this.ritual);
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
        this.ritual = null;
        this.currentRitualState = RitualStates.FREE;
        this.isProgressing = false;
        this.ritualHandler = null;
        this.ritualProgress = 0;
        this.ingredients.clear();
        this.ticker = 0;
        setChanged();
    }

    private void handleCollectedBehaviour(){
        Optional<ModRecipe<?>> recipeOptional = RecipeMatcher.findMatchingRecipe(RecipeOrigin.CHALK,this.ingredients);
        if(recipeOptional.isEmpty()){
            cancelRitual();
            return;
        }
        ModRecipe<?> recipe = recipeOptional.get();
        switch (recipe.getResultType()){
            case ITEM -> spawnRitualResultItem((ModRecipe<ItemStack>) recipe);
            case RITUAL -> performRitual((ModRituals)recipe.getResult().get());
            default -> cancelRitual();
        }
    }
    private void performRitual(ModRituals ritual){
        this.ritual = ritual;
        this.ritualProgress = 0;
        this.currentRitualState = RitualStates.ACTIVE;
        this.ritualHandler = getRitualHandler(this.ritual);
        setChanged();
    }

    // Helper Methods
    @Nullable
    private ModRitual getRitualHandler(ModRituals ritualIdentifier) {
        ModRitual ritual;

        if(Objects.isNull(ritualIdentifier)){
            return null;
        }
        if(Objects.isNull(level)){
            return null;
        }
        switch (ritualIdentifier){
            case EXTRACT_LIVE -> ritual = new ExtractLiveRitual(level, getBlockPos(), getBlockState(),this.ritualProgress);
            case CHANGE_TIME_TO_DAY -> ritual = new ChangeTimeToDayRitual(level, getBlockPos(),getBlockState(),this.ritualProgress);
            default -> ritual = null;
        }
        return ritual;
    }
    private void dropInventoryItems(){
        if(ingredients.isEmpty()) return;
        Vec3 itemSpawnPosition = ModUtils.calcCenterOfBlock(getBlockPos().above());
        ingredients.forEach(itemStack -> APIHelper.spawnItemEntity(level,itemSpawnPosition,itemStack,Vec3.ZERO));
        ingredients.clear();
    }
    private void spawnRitualResultItem(ModRecipe<ItemStack> recipe) {
            if(Objects.isNull(level) || level.isClientSide()){
                return;
            }
            BlockPos aboveBlock = this.getBlockPos().above();
            Vec3 itemSpawnPosition = ModUtils.calcCenterOfBlock(aboveBlock);
            APIHelper.spawnItemEntity(level,itemSpawnPosition,recipe.getResult().get(),Vec3.ZERO);
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, aboveBlock.getX() + 0.5f,aboveBlock.getY()+0.5f,aboveBlock.getZ() +0.5f,0,1,1,1,1);
            level.playSound(null, getBlockPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS,0.25f,1f);
            resetToDefault();
    }

    /**
     * Adds an ItemStack to the current GoldenChalkBlock inventory and shrinks it by one!
     * @param itemStack The ItemStack which will be added to the inventory
     */
    private void addIngredientFromGround(ItemStack itemStack){
        ingredients.add(itemStack.copy());
        itemStack.shrink(1);
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
        if(foundEntities.isEmpty()){
            this.currentRitualState = RitualStates.COLLECTED;
            return;
        }
        ItemEntity chosenEntity = foundEntities.get(0);
        if(!ModUtils.isIngredient(chosenEntity.getItem())){
            cancelRitual();
            return;
        }
        if(ingredients.contains(chosenEntity.getItem())){
            cancelRitual();
            return;
        }
        addIngredientFromGround(chosenEntity.getItem());
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,0.25f,1f);
    }




}
