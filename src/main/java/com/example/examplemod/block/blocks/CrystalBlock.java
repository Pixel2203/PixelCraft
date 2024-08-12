package com.example.examplemod.block.blocks;

import com.example.examplemod.api.BlockDynamicUtil;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.block.template.TurnableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalBlock extends TurnableBlock {
    public static final IntegerProperty ENERGY = IntegerProperty.create("energy", 0,4);
//    IntegerProperty charged = IntegerProperty.create("charged", 0, 100);
public static final IntegerProperty CHARGED_BLOCK = IntegerProperty.create("charged_block", 0, 100);

    private int maxCharge;
    protected static final VoxelShape SHAPE = Block.box(6.0D, 0.0D, 6.0D, 11.0D, 8.0D, 11.0D);

    public CrystalBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENERGY, 0).setValue(FACING, Direction.NORTH).setValue(CHARGED_BLOCK, 0));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENERGY, FACING, CHARGED_BLOCK);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return SHAPE;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(ENERGY, 0);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        if (!level.isClientSide) {
        System.out.println(blockState.getValue(ENERGY));
        int chargeValue = blockState.getValue(CHARGED_BLOCK);
        int energyValue = Math.min(4, chargeValue / 25);
//        blockState.setValue(ENERGY, energyValue);
        BlockState newState = blockState.setValue(ENERGY, energyValue);
        level.setBlock(blockPos, newState, 3);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        checkAndDestroy(level, pos, state);
        return true;
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        checkAndDestroy(level, pos, state);
    }

    protected void checkAndDestroy(Level level, BlockPos pos, BlockState blockState) {
        if (level.isClientSide) {
            return;
        }

        // Zerstöre den Block, aber erzeuge keine normalen Drops
        level.destroyBlock(pos, false);
        level.gameEvent(null, GameEvent.BLOCK_DESTROY, pos);

        // Erstelle einen neuen ItemStack des Blocks
        ItemStack itemStack = new ItemStack(blockState.getBlock());

        // Erstelle ein NBT Tag Compound
        CompoundTag nbt = new CompoundTag();

        // Füge die Block-Properties als NBT-Tags hinzu
        for (Property<?> property : blockState.getProperties()) {
            Comparable<?> value = blockState.getValue(property);
            nbt.putString(property.getName(), value.toString());
        }
        nbt.putInt(CustomNBTTags.ENERGY_CHARGE, blockState.getValue(CHARGED_BLOCK));

        // Füge das NBT Tag dem ItemStack hinzu
        itemStack.setTag(nbt);

        // Droppe das Item mit den NBT-Tags
//        ItemEntity itemEntity = new ItemEntity(level, pos.getX()+ 0.5, pos.getY(), pos.getZ()+0.5, itemStack);
//        level.addFreshEntity(itemEntity);

        ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5f, (double)pos.getY(), (double)pos.getZ() + 0.5f, itemStack);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }

}