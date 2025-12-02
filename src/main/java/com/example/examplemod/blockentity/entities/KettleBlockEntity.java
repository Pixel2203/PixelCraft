package com.example.examplemod.blockentity.entities;

import com.example.examplemod.api.APIHelper;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.kettle.KettleBrewing;
import com.example.examplemod.api.kettle.KettleBrewingLogic;
import com.example.examplemod.api.kettle.KettleInteraction;
import com.example.examplemod.api.kettle.KettleInteractionLogic;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.block.blocks.KettleBlock;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class KettleBlockEntity extends BlockEntity implements ITickableBlockEntity, KettleInteraction {
    private final NonNullList<ItemStack> ingredients = NonNullList.create();
    @Getter
    private boolean isBrewing;
    private int brewingTimeTicker;

    private final int brewingTime = 20 * 3;
    private final KettleBrewing brewingLogic;
    private final KettleInteraction interactionLogic;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
        this.brewingLogic = new KettleBrewingLogic(this);
        this.interactionLogic = new KettleInteractionLogic(this);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag modCompound = nbt.getCompound(ExampleMod.MODID);
        CompoundTag itemsCompound = modCompound.getCompound("items");
        ContainerHelper.loadAllItems(itemsCompound, ingredients);

        this.isBrewing = modCompound.getBoolean(CustomNBTTags.IS_PROGRESSING);
        this.brewingTimeTicker = modCompound.getInt(CustomNBTTags.TICKER);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag modCompound = new CompoundTag();
        CompoundTag itemsCompound = new CompoundTag();
        ContainerHelper.saveAllItems(itemsCompound, ingredients);
        modCompound.putBoolean(CustomNBTTags.IS_PROGRESSING, this.isBrewing);
        modCompound.putInt(CustomNBTTags.TICKER, this.brewingTimeTicker);
        modCompound.put("items", itemsCompound);
        nbt.put(ExampleMod.MODID,modCompound);


    }
    public void add(ItemStack item){
        ingredients.add(item);
        setChanged();
    }
    public List<ItemStack> getKettleContent(){
        return this.ingredients;
    }
    public void startBrewing(){
        if(ingredients.isEmpty()){
            return;
        }
        this.isBrewing = true;
        this.brewingTimeTicker = 0;
        setChanged();
    }

    @Override
    public void tick() {
        if (Objects.isNull(level) || level.isClientSide()) {
            return;
        }
        handleBoiling();
        handleBrewing();;
        handleBubbles();





    }

    private void handleBoiling() {
        BlockState blockState = getBlockState();
        boolean isFireBelow = ((KettleBlock) blockState.getBlock()).isFireBelow(this.level, this.getBlockPos());
        boolean isAlreadyBoiling = blockState.getValue(KettleBlock.isBoiling);
        if (isFireBelow != isAlreadyBoiling) {
            level.setBlock(getBlockPos(), blockState.setValue(KettleBlock.isBoiling, isFireBelow), 3);
        }
    }
    private void handleBrewing() {
        if(!isBrewing) return;

        brewingTimeTicker++;
        setChanged();

        if (brewingTimeTicker >= this.brewingTime) {
            brewingTimeTicker = 0;
            isBrewing = false;
            brewingLogic.onBrewingFinished();
        }

    }
    private void handleBubbles() {
        BlockState blockState = getBlockState();
        boolean isMaxFluidLevel = blockState.getValue(KettleBlock.fluid_level) == KettleBlock.NEEDED_FLUID_LEVEL_TO_BREW;
        if(!isMaxFluidLevel) return;
        if(!this.getKettleBlock().isFireBelow(this.getLevel(), this.getBlockPos())) return;
        if (blockState.getValue(KettleBlock.isMixture)) return;

        spawnBubbles();
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

    public void resetKettle() {
        this.ingredients.clear();
        ((KettleBlock)this.getBlockState().getBlock()).resetKettleBlockState((ServerLevel) this.level,this.getBlockState(),this.getBlockPos());
    }


    // Interaction Logic
    public void fallOn(Entity entity) {
        this.interactionLogic.fallOn(entity);

    }
    public InteractionResult onBottle(ServerPlayer player){
        return this.interactionLogic.onBottle(player);
    }

    // Getter
    public KettleBlock getKettleBlock() {
        return (KettleBlock) this.getBlockState().getBlock();
    }
    public Optional<ModRecipe<?>> getRecipe() {
        boolean hasIngredients = !this.getKettleContent().isEmpty();
        if(!hasIngredients) return Optional.empty();
        return RecipeMatcher.findMatchingRecipe(RecipeOrigin.KETTLE,this.getKettleContent());
    }


    public void dropContent() {
        List<ItemStack> items = this.getKettleContent();
        for(ItemStack itemStack : items){
            APIHelper.spawnItemEntity(this.getLevel(), this.getBlockPos().above().getCenter(),itemStack,Vec3.ZERO);
        }

    }
}
