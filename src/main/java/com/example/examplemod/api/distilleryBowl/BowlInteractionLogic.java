package com.example.examplemod.api.distilleryBowl;

import com.example.examplemod.api.kettle.BlockEntityLogic;
import com.example.examplemod.api.vial.IVialable;
import com.example.examplemod.api.vial.VialResult;
import com.example.examplemod.block.blocks.DistilleryBowl;
import com.example.examplemod.blockentity.entities.DistilleryBowlBlockEntity;
import com.example.examplemod.item.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;

public class BowlInteractionLogic extends BlockEntityLogic<DistilleryBowlBlockEntity> implements IVialable {

    public BowlInteractionLogic(DistilleryBowlBlockEntity blockEntity) {
        super(blockEntity);
    }

    public InteractionResult use(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(Items.POTION)) {
            return usePotion(player, stack);
        } else if (stack.is(ItemRegistry.VIAL.get())) {
            return InteractionResult.PASS;
        }else {
            NetworkHooks.openScreen((ServerPlayer) player, blockEntity, blockEntity.getBlockPos());
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public VialResult tap(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        if(!blockEntity.hasContent()) return VialResult.failed();
        return getVialResult().map(vialType -> {
            empty(level, blockState, blockPos);
            blockEntity.setContent(null);
            return VialResult.success(vialType);
        }).orElse(VialResult.failed());
    }

    private Optional<ModFluids> getVialResult() {
        return Optional.ofNullable(blockEntity.getContent());
    }

    private InteractionResult usePotion(Player player, ItemStack potionStack) {
        if(blockEntity.hasContent()) return InteractionResult.FAIL;
        if(!player.isCreative()){
            potionStack.shrink(1);
        }
        fill(getServerLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos());
        blockEntity.setContent(ModFluids.WATER);
        return InteractionResult.SUCCESS;
    }

    private void fill(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(DistilleryBowl.isFilled, true));
    }

    private void empty(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(DistilleryBowl.isFilled, false));
    }


}
