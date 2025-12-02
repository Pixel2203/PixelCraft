package com.example.examplemod.item.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.KettleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class SoulFragment extends Item {
    public SoulFragment() {
        super(new Item.Properties().stacksTo(64));
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if(!compoundTag.contains(ExampleMod.MODID)) return false;
        CompoundTag modTag = compoundTag.getCompound(ExampleMod.MODID);
        String boundTo = modTag.getString("boundTo");
        return !StringUtil.isNullOrEmpty(boundTo);
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        if(!compoundTag.contains(ExampleMod.MODID)) return Component.translatable("item." + ExampleMod.MODID + ".soul_fragment");
        CompoundTag modTag = compoundTag.getCompound(ExampleMod.MODID);
        if(modTag.contains("boundTo")) {
            return Component.translatable("item." + ExampleMod.MODID + ".bound_soul_fragment");
        }
        return Component.translatable("item." + ExampleMod.MODID + ".soul_fragment");
    }

    private void bind(ItemStack stack, UUID uuid){
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.remove(ExampleMod.MODID);
        CompoundTag modTag = new CompoundTag();
        modTag.putString("boundTo", uuid.toString());
        compoundTag.put(ExampleMod.MODID, modTag);
        stack.setTag(compoundTag);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity livingEntity, InteractionHand hand) {
        if(player.level().isClientSide) return super.interactLivingEntity(stack, player, livingEntity, hand);
        if(ModUtils.isBound(stack)) return InteractionResult.FAIL;
        stack.shrink(1);
        ItemStack boundFragment = new ItemStack(this);
        bind(boundFragment, livingEntity.getUUID());
        player.addItem(boundFragment);
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide()) return InteractionResult.PASS;
        BlockPos clicked = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(clicked);
        if(!blockState.is(BlockRegistry.ZirconBlock.get())) return InteractionResult.PASS;
        ItemStack itemStack = context.getItemInHand();
        if(ModUtils.isBound(itemStack)) return InteractionResult.FAIL;
        itemStack.shrink(1);
        ItemStack boundFragment = new ItemStack(this);
        bind(boundFragment, context.getPlayer().getUUID());
        context.getPlayer().addItem(boundFragment);
        context.getPlayer().level().playSound(null, context.getPlayer().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
        return InteractionResult.SUCCESS;
    }

}
