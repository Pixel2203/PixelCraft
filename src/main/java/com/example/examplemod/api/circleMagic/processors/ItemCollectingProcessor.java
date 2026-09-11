package com.example.examplemod.api.circleMagic.processors;

import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.circleMagic.CircleContext;
import com.example.examplemod.api.circleMagic.rituals.RitualState;
import com.example.examplemod.block.blocks.GoldenChalkBlock;
import com.example.examplemod.blockentity.entities.GoldenChalkBlockEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Searches for near ItemEntities and adds them to the GoldenChalkBlock Inventory
 * Only adds ingredients that has an ingredient Tag!
 */
@Slf4j
public class ItemCollectingProcessor extends MagicCircleProcessor {
    @Override
    public RitualState process(CircleContext context,
                               ServerLevel level,
                               BlockState blockState,
                               BlockPos blockPos,
                               GoldenChalkBlockEntity blockEntity) {
        log.debug("ItemCollectingProcessor | process | Collecting next item");
        List<ItemEntity> foundEntities = ((GoldenChalkBlock)blockState.getBlock()).getItemEntitesInRangeFromBlockPos(level,blockPos,3);
        if(foundEntities.isEmpty()){
            log.debug("ItemCollectingProcessor | process | No more items found");
            return RitualState.EVALUATING;
        }
        ItemEntity chosenEntity = foundEntities.get(0);
        if(!ModUtils.isIngredient(chosenEntity.getItem())){
            log.debug("ItemCollectingProcessor | process | Item found that was not an ingredient");
            return cancelRitual(level, blockPos, blockEntity, context);
        }
        if(blockEntity.getIngredients().contains(chosenEntity.getItem())){
            log.debug("ItemCollectingProcessor | process | Found an Item I already collected, aborting");
            return cancelRitual(level, blockPos,blockEntity, context);
        }
        addIngredientFromGround(blockEntity,chosenEntity.getItem());
        level.playSound(null, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,0.25f,1f);
        return RitualState.COLLECTING;
    }


    /**
     * Adds an ItemStack to the current GoldenChalkBlock inventory and shrinks it by one!
     * @param itemStack The ItemStack which will be added to the inventory
     */
    private void addIngredientFromGround(GoldenChalkBlockEntity blockEntity, ItemStack itemStack){
        blockEntity.getIngredients().add(itemStack.copy());
        itemStack.shrink(1);
    }
}
