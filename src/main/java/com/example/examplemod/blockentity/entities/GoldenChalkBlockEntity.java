package com.example.examplemod.blockentity.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.circleMagic.RitualContext;
import com.example.examplemod.api.circleMagic.processors.RitualPipelineHandler;
import com.example.examplemod.api.circleMagic.processors.MagicCircleProcessor;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.circleMagic.RitualFactory;
import com.example.examplemod.api.circleMagic.ModRituals;
import com.example.examplemod.api.circleMagic.RitualState;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class GoldenChalkBlockEntity extends BlockEntity implements ITickableBlockEntity {
    @Setter
    private int ticker = 0;
    private final int tickerInterval = 20;
    @NotNull private RitualContext context;

    @Getter
    private final NonNullList<ItemStack> ingredients = NonNullList.create();

    private final RitualPipelineHandler circlePipelineHandler = new RitualPipelineHandler();

    public GoldenChalkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.GOLDEN_CHALK_BLOCK_ENTITY.get(), blockPos, blockState);
        this.context = RitualContext.builder().
                ritualState(RitualState.FREE)
                .build();
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
        this.ticker = modCompound.getInt(CustomNBTTags.TICKER);
        this.context = loadRitualContext(modCompound);


    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag modCompound = new CompoundTag();
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, ingredients);
        modCompound.putInt(CustomNBTTags.TICKER,this.ticker);
        modCompound.put("items", itemsTag);
        saveRitualContext(modCompound);
        nbt.put(ExampleMod.MODID,modCompound);
    }


    private void saveRitualContext(@NotNull CompoundTag modCompound) {
        CompoundTag ritualContextTag = modCompound.getCompound("ritualContext");
        ritualContextTag.putInt(CustomNBTTags.PROGRESS,context.getRitualProgress());
        ritualContextTag.putString(CustomNBTTags.RITUAL_STATE,context.getRitualState().name());
        if(Objects.nonNull(context.getRitualHandler())) {
            ritualContextTag.putString(CustomNBTTags.RITUAL_NAME, context.getRitualHandler().getType().name());
        }
    }

    private RitualContext loadRitualContext(@NotNull CompoundTag modCompound) {
        CompoundTag ritualContextTag = modCompound.getCompound("ritualContext");
        var ritualName = ritualContextTag.getString(CustomNBTTags.RITUAL_NAME);
        int ritualProgress = ritualContextTag.getInt(CustomNBTTags.PROGRESS);

        RitualState ritualState = RitualState.FREE;
        String ritualStateData = ritualContextTag.getString(CustomNBTTags.RITUAL_STATE);
        if(!ritualStateData.isBlank()) {
            ritualState = RitualState.valueOf(ritualStateData);
        }
        var context = RitualContext.builder()
                .ritualProgress(ritualProgress)
                .ritualState(ritualState);

        if(EnumUtils.isValidEnum(ModRituals.class, ritualName)) {
            ModRituals activeRitual = ModRituals.valueOf(ritualName);
            context.ritualHandler(RitualFactory.build(activeRitual,ritualProgress));
        }

        return context.build();
    }

    public void clickRitualBlock() {
        if(isBusy())return;
        triggerRitualProcessor(this.context);
        setChanged();
    }


    @Override
    public void tick() {
        if(!this.isBusy()){
            return;
        }
        ticker++;

        if(ticker >= tickerInterval){
            triggerRitualProcessor(this.context);
            ticker = 0;


            // Spawns Particles
            var spawnParticlePosition = getBlockPos().above();
            Random random = new Random();
            int amountOfParticles = random.nextInt(1,4);
            ModUtils.sendParticles((ServerLevel) getLevel(), ParticleTypes.FLAME, spawnParticlePosition,1f, amountOfParticles,0.4f,0,0.4f,0);
            setChanged();
        }



    }

    private void triggerRitualProcessor(RitualContext context) {
        MagicCircleProcessor processor = circlePipelineHandler.getProcessor(context.getRitualState());
        RitualState result = processor.process(context,(ServerLevel) getLevel(), getBlockState(), getBlockPos(), this);
        context.setRitualState(result);
    }









    private boolean isBusy() {
        return context.getRitualState() != RitualState.FREE;
    }




}
