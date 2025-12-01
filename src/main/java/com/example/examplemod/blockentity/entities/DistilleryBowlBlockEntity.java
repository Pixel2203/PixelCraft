package com.example.examplemod.blockentity.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.distilleryBowl.BowlCrafting;
import com.example.examplemod.api.distilleryBowl.BowlInteraction;
import com.example.examplemod.api.distilleryBowl.BowlInteractionLogic;
import com.example.examplemod.api.vial.IVialable;
import com.example.examplemod.api.vial.VialResult;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import com.example.examplemod.menus.DistilleryBowlMenu;
import joptsimple.internal.Strings;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DistilleryBowlBlockEntity extends BlockEntity implements ITickableBlockEntity, MenuProvider, IVialable, BowlInteraction {
    @Getter
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private final ContainerData data;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    @Getter
    @Nullable
    private VialType content;

    private final BowlCrafting craftingLogic;
    private final BowlInteractionLogic interactionLogic;

    public DistilleryBowlBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.DISTILLERY_BOWL_BLOCK_ENTITY.get(), p_155229_, p_155230_);
        this.craftingLogic = new BowlCrafting(this);
        this.interactionLogic = new BowlInteractionLogic(this);
        this.data = new ContainerData() {

            @Override
            public int get(int p_39284_) {
                return switch (p_39284_) {
                    case 0 -> DistilleryBowlBlockEntity.this.craftingLogic.getProgress();
                    case 1 -> DistilleryBowlBlockEntity.this.craftingLogic.getMaxProgress();
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> DistilleryBowlBlockEntity.this.craftingLogic.setProgress(pValue);
                    case 1 -> DistilleryBowlBlockEntity.this.craftingLogic.setMaxProgress(pValue);

                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };



    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    @Override
    public void tick() {
        if(craftingLogic.hasRecipe()) {
            craftingLogic.craft();
            setChanged();
        }

    }






    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("DistilleryBowl");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player p_39956_) {
        return new DistilleryBowlMenu(id, inventory, this, this.data);
    }




    /**
     *
     * @return Returns the current bowl fluid color - water is befind returned as default
     */
    public int getColor() {
        if(Objects.nonNull(this.content)) {
            return this.content.getHexColor();
        }
        return 0x3F76E4;
    }

    @Override
    public VialResult tap(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        return this.interactionLogic.tap(level, blockState, blockPos);
    }

    public boolean isWater() {
        return this.getContent() == VialType.WATER;
    }


    public void setContent(@Nullable VialType content) {
        this.content = content;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
    public boolean hasContent() {
        return this.content != null;
    }






    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        CompoundTag modCompound = new CompoundTag();
        modCompound.putInt("progress", this.craftingLogic.getProgress());
        modCompound.put("inventory", itemHandler.serializeNBT());

        if(Objects.nonNull(this.content)) {
            modCompound.putString("content", this.content.name());
        }

        compoundTag.put(ExampleMod.MODID, modCompound);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        CompoundTag modCompound = p_155245_.getCompound(ExampleMod.MODID);
        if(modCompound.contains("progress")) {
            this.craftingLogic.setProgress(modCompound.getInt("progress"));
        }
        if(modCompound.contains("inventory")) {
            itemHandler.deserializeNBT(modCompound.getCompound("inventory"));
        }
        if(modCompound.contains("content")) {
            this.content = VialType.valueOf(Objects.requireNonNull(modCompound.getString("content")));
        }
        super.load(p_155245_);

    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        if(Objects.nonNull(this.content)) tag.putString("content", this.content.name());
        else tag.putString("content", Strings.EMPTY);
        return tag;
    }
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.content = tag.getString("content").isBlank() ?
                null : VialType.valueOf(tag.getString("content"));
    }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if(Objects.isNull(pkt.getTag())) return;
        handleUpdateTag(pkt.getTag());
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public BowlInteractionLogic getInteractionLogic() {
        return this.interactionLogic;
    }
}
