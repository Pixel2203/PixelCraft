package com.example.examplemod.api.distilleryBowl;

import com.example.examplemod.api.kettle.BlockEntityLogic;
import com.example.examplemod.api.recipe.ModRecipe;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.api.recipe.RecipeOrigin;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.block.blocks.DistilleryBowl;
import com.example.examplemod.blockentity.entities.DistilleryBowlBlockEntity;
import com.example.examplemod.tag.TagFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

public class BowlCrafting extends BlockEntityLogic<DistilleryBowlBlockEntity> {
    private final int INPUT_SLOT = 0;
    @Getter
    @Setter
    private int progress;

    @Getter
    @Setter
    private int maxProgress = 78;

    public BowlCrafting(DistilleryBowlBlockEntity blockEntity) {
        super(blockEntity);
    }

    public void craft() {
        if(!hasRecipe()) return;
        increaseCraftingProgress();

        if(hasProgressFinished()){
            craftItem();
            resetProgress();
        }
    }

    private void resetProgress() {
        this.setProgress(0);
    }

    @SneakyThrows
    private void craftItem() {
        ItemStack herb = blockEntity.getItemHandler().extractItem(INPUT_SLOT, 1, false);
        var foundBowlRecipe = RecipeMatcher.findMatchingRecipe(RecipeOrigin.BOWL, List.of(herb));
        if(foundBowlRecipe.isEmpty()) {
            throw new Exception("Bowl Recipe could not be found! Invalid Item: " + herb.getDisplayName());
        }
        ModRecipe<?> recipe = foundBowlRecipe.get();
        Lazy<?> vialTypeLazy = recipe.getResult();
        if(vialTypeLazy.get() instanceof VialType vialType) {
            this.blockEntity.setContent(vialType);
        }else {
            throw new Exception("Bowl result was not of type VialType, got:" + vialTypeLazy.get().toString());
        }
    }

    private boolean hasProgressFinished() {
        return this.getProgress() >= this.getMaxProgress();
    }

    private void increaseCraftingProgress() {
        this.setProgress(this.getProgress() + 1);
    }


    public boolean hasRecipe() {
        boolean hasCraftingItem = blockEntity.getItemHandler().getStackInSlot(INPUT_SLOT).is(TagFactory.BOWL_INGREDIENT);
        DistilleryBowl bowl = (DistilleryBowl) blockEntity.getBlockState().getBlock();
        return hasCraftingItem && bowl.isFilled(blockEntity.getBlockState()) && blockEntity.isWater();
    }
}
