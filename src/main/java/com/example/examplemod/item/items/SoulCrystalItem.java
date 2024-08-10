package com.example.examplemod.item.items;

import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.api.translation.CustomTranslatable;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.CrystalBlock;
import com.example.examplemod.entity.entities.SoulEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SoulCrystalItem extends BlockItem {
    private static final Logger log = LoggerFactory.getLogger(SoulCrystalItem.class);
    private final float maxCharge;
    public SoulCrystalItem(Block block, Properties properties, float maxCharge) {
        super(block, properties);
        this.maxCharge = maxCharge;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack p_41398_, Player p_41399_, LivingEntity p_41400_, InteractionHand p_41401_) {
        log.info("Interacted with entity: {}" , p_41400_.getDisplayName().getString());
        return super.interactLivingEntity(p_41398_, p_41399_, p_41400_, p_41401_);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.isCrouching()) {
            if (player.level().isClientSide() || hand == InteractionHand.OFF_HAND) {
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
            List<AreaEffectCloud> soulParticlesInRange = level.getEntitiesOfClass(AreaEffectCloud.class, player.getBoundingBox().inflate(2.0D), (effectCloud) -> effectCloud != null && effectCloud.isAlive() && effectCloud.getOwner() instanceof SoulEntity);
            if (soulParticlesInRange.isEmpty()) {
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
            AreaEffectCloud clickedEntity = soulParticlesInRange.get(0);
            return retrieveEnergyFromEffectCloud(level, player, hand, clickedEntity);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

//    public InteractionResult place(BlockPlaceContext context) {
//        if (context.getPlayer().isCrouching()) {
//            IntegerProperty ENERGY = IntegerProperty.create("energy", 0, 4);
//            ItemStack crystal = context.getPlayer().getItemInHand(context.getHand());
//            CompoundTag nbt = crystal.hasTag() ? crystal.getTag() : new CompoundTag();
//            int charge = (int)(Math.random() * 5);
//            Level level = context.getLevel();
//            BlockPos pos = context.getClickedPos();
//            BlockState newState = this.getBlock().defaultBlockState().setValue(ENERGY, charge);
//
//            if (!level.isClientSide()) {
//                InteractionResult result = super.place(context);
//                if (result.consumesAction()) {
//                }
//                if (newState.getBlock() instanceof CrystalBlock) {
//                    level.setBlock(pos, newState, 3);
//                }
//                return result;
//            }
//        }
//        return InteractionResult.FAIL;
//    }
    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (context.getPlayer().isCrouching()) {
            IntegerProperty ENERGY = IntegerProperty.create("energy", 0, 4);
            ItemStack crystal = context.getPlayer().getItemInHand(context.getHand());
            CompoundTag nbt = crystal.hasTag() ? crystal.getTag() : new CompoundTag();
            float nbtValue = nbt.getFloat(CustomNBTTags.ENERGY_CHARGE);
//            int charge = (int)(Math.random() * 5);
//            int charge = nbt.getInt(CustomNBTTags.ENERGY_CHARGE);
            int charge = (int) Math.min(4, Math.floor(nbtValue * 5));
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState existingState = level.getBlockState(pos);

            if (!existingState.isAir()) {
                return InteractionResult.FAIL;
            }

            BlockState newState = this.getBlock().defaultBlockState().setValue(ENERGY, charge);

            if (newState.hasProperty(BlockStateProperties.FACING)) {
                Direction playerFacing = context.getNearestLookingDirection().getOpposite();
                newState = newState.setValue(BlockStateProperties.FACING, playerFacing);
            } else if (newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction playerFacing = context.getHorizontalDirection().getOpposite();
                newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, playerFacing);
            }

            if (!level.isClientSide()) {
                InteractionResult result = super.place(context);
                if (result.consumesAction() && newState.getBlock() instanceof CrystalBlock) {
                    level.setBlock(pos, newState, 3);
                }
                return result;
            }
        }
        return InteractionResult.FAIL;
    }


    @NotNull
    public InteractionResultHolder<ItemStack> retrieveEnergyFromEffectCloud(Level level, Player player, InteractionHand hand,AreaEffectCloud entity) {

        ItemStack crystal = player.getItemInHand(hand);
        CompoundTag nbt = crystal.hasTag() ? crystal.getTag() : new CompoundTag();
        float chargedInItem = nbt.getFloat(CustomNBTTags.ENERGY_CHARGE);
        //float retrievedFromSoul = soulEntity.retrieveEnergyFromSoul(player.getOnPos());
        float retrieved = Math.min(entity.getRadius(), 0.05f);
        entity.setRadius(entity.getRadius() - retrieved);
        if(entity.getRadius() == 0){
            entity.discard();
        }
        float nextEnergyLevel= Math.min(maxCharge, chargedInItem + retrieved);
        nbt.putFloat(CustomNBTTags.ENERGY_CHARGE, nextEnergyLevel);
        crystal.setTag(nbt);
        return InteractionResultHolder.success(crystal);
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        CompoundTag nbt = itemStack.hasTag() ? itemStack.getTag() : new CompoundTag();
        components.add(Component.translatable(CustomTranslatable.SOUL_CRYSTAL_INFO));
        components.add(Component.literal("%s / %s".formatted(nbt.getFloat(CustomNBTTags.ENERGY_CHARGE), maxCharge)));
        super.appendHoverText(itemStack, level, components, flag);
    }
}
