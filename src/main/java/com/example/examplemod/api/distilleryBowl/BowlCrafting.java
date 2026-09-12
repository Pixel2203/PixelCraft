package com.example.examplemod.api.distilleryBowl;

import com.example.examplemod.api.kettle.BlockEntityLogic;
import com.example.examplemod.api.recipe.BowlRecipe;
import com.example.examplemod.api.recipe.RecipeMatcher;
import com.example.examplemod.blockentity.entities.DistilleryBowlBlockEntity;
import com.example.examplemod.tag.TagFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

@Slf4j
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
        increaseCraftingProgress();

        if(hasProgressFinished()){
            finishBrewing();
            resetProgress();
        }
    }

    private void resetProgress() {
        this.setProgress(0);
    }

    @SneakyThrows
    private void finishBrewing() {

        if(Objects.isNull(blockEntity.getContent())) {
            log.error("BowlCrafting | finishBrewing | content is not supposed to be null");
            return;
        }

        ItemStack herb = blockEntity.getItemHandler().extractItem(INPUT_SLOT, 1, false);
        var foundBowlRecipe = RecipeMatcher.matchBowlRecipe(blockEntity.getContent(), herb);
        if(foundBowlRecipe.isEmpty()) {
            log.debug("No valid BowlRecipe could be found, turning into {}" , ModFluids.WEIRD_STEW.name());
            this.blockEntity.setContent(ModFluids.WEIRD_STEW);
        }
        BowlRecipe recipe = foundBowlRecipe.get();
        this.blockEntity.setContent(recipe.out());
    }

    private boolean hasProgressFinished() {
        return this.getProgress() >= this.getMaxProgress();
    }

    private void increaseCraftingProgress() {
        this.setProgress(this.getProgress() + 1);
    }


    public boolean hasRecipe() {
        boolean hasCraftingItem = blockEntity.getItemHandler().getStackInSlot(INPUT_SLOT).is(TagFactory.BOWL_INGREDIENT);
        return hasCraftingItem && Objects.nonNull(blockEntity.getContent());
    }
}
