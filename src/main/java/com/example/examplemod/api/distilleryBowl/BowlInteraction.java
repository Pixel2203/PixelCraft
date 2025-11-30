package com.example.examplemod.api.distilleryBowl;

import com.example.examplemod.api.kettle.BlockEntityLogic;
import com.example.examplemod.api.vial.IVialable;
import com.example.examplemod.api.vial.VialResult;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.block.blocks.DistilleryBowl;
import com.example.examplemod.blockentity.entities.DistilleryBowlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class BowlInteraction extends BlockEntityLogic<DistilleryBowlBlockEntity> implements IVialable {

    public BowlInteraction(DistilleryBowlBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public VialResult tap(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        if(!canUseVial(blockState)) return VialResult.failed();
        return getVialResult().map(vialType -> {
            DistilleryBowl bowl = (DistilleryBowl) blockState.getBlock();
            bowl.empty(level, blockState, blockPos);
            blockEntity.setContent(VialType.WATER);
            return VialResult.success(vialType);
        }).orElse(VialResult.failed());
    }

    private boolean canUseVial(BlockState blockState) {
        DistilleryBowl bowl = (DistilleryBowl) blockState.getBlock();
        return bowl.isFilled(blockState) && !blockEntity.isWater();
    }

    private Optional<VialType> getVialResult() {
        return Optional.ofNullable(blockEntity.getContent());
    }


}
