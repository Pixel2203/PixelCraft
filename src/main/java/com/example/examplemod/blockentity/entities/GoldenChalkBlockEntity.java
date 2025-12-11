package com.example.examplemod.blockentity.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.api.goldenChalk.rituals.RitualFactory;
import com.example.examplemod.api.goldenChalk.rituals.util.ModRitual;
import com.example.examplemod.api.goldenChalk.rituals.util.ModRituals;
import com.example.examplemod.api.goldenChalk.rituals.util.RitualStates;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class GoldenChalkBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private int ticker = 0;
    private int ritualProgress = 0;
    private final int tickerInterval = 20;
    private ModRitual ritualHandler = null;
    private RitualStates currentRitualState = RitualStates.FREE;
    private final NonNullList<ItemStack> ingredients = NonNullList.create();


    public GoldenChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.GOLDEN_CHALK_BLOCK_ENTITY.get(), blockPos, blockState);
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
        this.currentRitualState = RitualStates.valueOf(modCompound.getString(CustomNBTTags.RITUAL_STATE));
        this.ritualProgress = modCompound.getInt(CustomNBTTags.PROGRESS);
        this.ticker = modCompound.getInt(CustomNBTTags.TICKER);

        var ritualName = modCompound.getString(CustomNBTTags.RITUAL_NAME);
        if(EnumUtils.isValidEnum(ModRituals.class, ritualName)) {
            ModRituals activeRitual = ModRituals.valueOf(ritualName);
            this.ritualHandler = RitualFactory.build(activeRitual,this.ritualProgress);
        }

    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag modCompound = new CompoundTag();
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, ingredients);
        if(Objects.nonNull(this.currentRitualState)) {
            modCompound.putString(CustomNBTTags.RITUAL_STATE,this.currentRitualState.name());
        }

        modCompound.putInt(CustomNBTTags.PROGRESS,this.ritualProgress);
        modCompound.putInt(CustomNBTTags.TICKER,this.ticker);
        if(Objects.nonNull(this.ritualHandler)) {
            modCompound.putString(CustomNBTTags.RITUAL_NAME, this.ritualHandler.getType().name());
        }

        modCompound.put("items", itemsTag);
        nbt.put(ExampleMod.MODID,modCompound);


    }

    public void clickRitualBlock() {
        if(isBusy())return;
        this.initRitual();
    }

    private void initRitual() {
        this.currentRitualState = RitualStates.COLLECTING;
        this.ingredients.clear();
        setChanged();
    }

    private void cancelRitual() {
        if(Objects.isNull(level)){
            return;
        }
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS,0.25f,1f);
        dropContent();
        resetToDefault();

    }
    @Override
    public void tick() {
        if(!this.isBusy()){
            return;
        }
        ticker++;

        if(ticker >= tickerInterval){
            switch (this.currentRitualState){
                case COLLECTING -> onCollect();
                case COLLECTED -> onCollected();
                case ACTIVE -> onRitualTick();
            }
            ticker = 0;


            // Spawns Particles
            var spawnParticlePosition = getBlockPos().above();
            Random random = new Random();
            int amountOfParticles = random.nextInt(1,4);
            ModUtils.sendParticles((ServerLevel) getLevel(), ParticleTypes.FLAME, spawnParticlePosition,1f, amountOfParticles,0.4f,0,0.4f,0);

        }


        setChanged();
    }

    private void onRitualTick() {
        this.ritualProgress = ritualHandler.tick((ServerLevel) this.getLevel(), this.getBlockState(), this.getBlockPos(), this);
        if(ritualHandler.isFinished()){
            ritualHandler.onFinish((ServerLevel) this.getLevel(), this.getBlockState(), this.getBlockPos(), this);
            resetToDefault();
        }
    }
    private void resetToDefault() {
        this.currentRitualState = RitualStates.FREE;
        this.ritualHandler = null;
        this.ritualProgress = 0;
        this.ingredients.clear();
        this.ticker = 0;
    }

    private void onCollected(){
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
        this.ritualProgress = 0;
        this.currentRitualState = RitualStates.ACTIVE;
        this.ritualHandler = RitualFactory.build(ritual, this.ritualProgress);;
    }

    private void dropContent(){
        Vec3 itemSpawnPosition = getBlockPos().above().getCenter();
        ingredients.forEach(itemStack -> APIHelper.spawnItemEntity(level,itemSpawnPosition,itemStack,Vec3.ZERO));
        ingredients.clear();
    }
    private void spawnRitualResultItem(ModRecipe<ItemStack> recipe) {
            if(Objects.isNull(level) || level.isClientSide()){
                return;
            }
            Vec3 itemSpawnPosition = this.getBlockPos().above().getCenter();
            APIHelper.spawnItemEntity(level,itemSpawnPosition,recipe.getResult().get(),Vec3.ZERO);
            ModUtils.sendParticles((ServerLevel)level, ParticleTypes.EXPLOSION, itemSpawnPosition, 1,1,1,1,1,1);
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
    }


    /**
     * Searches for near ItemEntities and adds them to the GoldenChalkBlock Inventory
     * Only adds ingredients that has an ingredient Tag!
     */
    private void onCollect() {
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

    private boolean isBusy() {
        return this.currentRitualState != RitualStates.FREE;
    }




}
