package com.example.examplemod.item.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.vial.IVialable;
import com.example.examplemod.api.vial.VialResult;
import com.example.examplemod.api.vial.VialType;
import com.example.examplemod.sound.SoundRegistry;
import com.example.examplemod.tag.TagFactory;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Locale;

@Slf4j
public class Vial extends Item {

    public Vial() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide()) return super.useOn(context);
        ItemStack vialStack = context.getItemInHand();
        if(this.hasContent(vialStack)) return InteractionResult.PASS;
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedBlockState = context.getLevel().getBlockState(clickedPos);
        if(!clickedBlockState.is(TagFactory.VIALABLE_BLOCKS)) return InteractionResult.PASS;
        if(!(clickedBlockState.getBlock() instanceof IVialable vialable)) {
            log.error("Block {} with Tag {} on it, does not implement IVialable", clickedBlockState.getBlock().getName(), TagFactory.VIALABLE_BLOCKS);
            return InteractionResult.PASS;
        }
        VialResult tapResult = vialable.tap((ServerLevel) context.getLevel(), clickedBlockState, clickedPos);
        if(!tapResult.vialSuccess()) return InteractionResult.PASS;

        ItemStack filledVial = new ItemStack(this);
        this.saveVialTypeNbt(filledVial, tapResult.extracted());
        context.getPlayer().addItem(filledVial);
        vialStack.shrink(1);
        context.getPlayer().playNotifySound(SoundRegistry.VIAL_FILL_SOUND.get(), SoundSource.NEUTRAL,1f,1f);
        return InteractionResult.SUCCESS;
    }

    private boolean hasContent(ItemStack stack) {
        var nbt = stack.getOrCreateTag();
        if(!nbt.contains(ExampleMod.MODID)) return false;
        CompoundTag tag = nbt.getCompound(ExampleMod.MODID);
        return tag.contains("type");
    }


    private void saveVialTypeNbt(ItemStack vial, VialType vialType) {
        CompoundTag nbt = vial.getOrCreateTag();
        CompoundTag vialTag = new CompoundTag();
        vialTag.putString("type", vialType.name());
        nbt.put(ExampleMod.MODID, vialTag);
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if(!tag.contains(ExampleMod.MODID)) return super.getName(stack);
        tag = tag.getCompound(ExampleMod.MODID);
        if(!tag.contains("type")) return super.getName(stack);
        return Component.translatable("item."+ ExampleMod.MODID + ".vials."+tag.getString("type").toLowerCase());
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        if(!tag.contains(ExampleMod.MODID)) return super.getMaxStackSize(stack);
        tag = tag.getCompound(ExampleMod.MODID);
        if(!tag.contains("type")) return super.getMaxStackSize(stack);
        return 1;
    }
}
