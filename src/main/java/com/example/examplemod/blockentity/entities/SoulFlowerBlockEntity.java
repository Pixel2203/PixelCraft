package com.example.examplemod.blockentity.entities;

import com.example.examplemod.api.ModUtils;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.FogBlock;
import com.example.examplemod.block.blocks.SoulFlower;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoulFlowerBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private static final int MAX_FOG_RADIUS = 30;
    public static final int CLEAN_FOG_RADIUS = 5;
    private static final int TICKS_PER_RADIUS_INCREASE = 10;

    private int currentRadius = CLEAN_FOG_RADIUS;
    private int fogTickCounter = 0;

    private List<BlockPos> fogPositions = new ArrayList<>();

    public SoulFlowerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.SOUL_FLOWER_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("CurrentFogRadius", this.currentRadius);
        // NEU: Tick Counter speichern
        tag.putInt("TickCounter", this.fogTickCounter);

        ListTag fogPositionsTag = new ListTag();
        for(BlockPos pos : fogPositions) {
            fogPositionsTag.add(ModUtils.vec3ToTag(pos));
        }
        tag.put("FogPositions", fogPositionsTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.currentRadius = tag.getInt("CurrentFogRadius");
        // NEU: Tick Counter laden
        this.fogTickCounter = tag.getInt("TickCounter");

        ListTag fogPositions = tag.getList("FogPositions", ListTag.TAG_COMPOUND);
        for(int i = 0; i < fogPositions.size(); i++) {
            BlockPos pos = ModUtils.blockPosFromTag(fogPositions.getCompound(i));
            this.fogPositions.add(pos);
        }
    }

    @Override
    public void tick() {
        if(!canTick()) return;

        if (!fogFullySpread()) {
            this.fogTickCounter++;
            spreadFog();
        }

        this.spawnInnerParticles();;
    }

    private boolean canTick() {
        if (this.level == null || this.level.isClientSide) return false;
        return this.getBlockState().getValue(SoulFlower.NIGHT_ACTIVE);
    }

    private boolean fogFullySpread() {
        return this.currentRadius >= MAX_FOG_RADIUS;
    }

    private void spreadFog() {
        // Prüfung: Hat der Zähler den Schwellenwert erreicht?
        if (this.fogTickCounter >= TICKS_PER_RADIUS_INCREASE) {

            // 1. Zähler zurücksetzen
            this.fogTickCounter = 0;

            // 2. Radius erhöhen und neue Schicht bauen
            this.currentRadius++;
            this.buildNewSphereLayer(this.currentRadius);

            // 3. Änderungen speichern
            this.setChanged();
        }
    }

    private void spawnInnerParticles() {
        if(this.level.random.nextInt(1,100) >= 65) {
            BlockPos flowerPos = this.getBlockPos();
            ((ServerLevel)this.getLevel()).sendParticles(ParticleTypes.SNOWFLAKE,flowerPos.getX(), flowerPos.getY() + 3, flowerPos.getZ(), 1, 3, 10,3,0);
        }
    }


    // Ersetzt die alte spawnFog/spreadFog Methode
    private void buildNewSphereLayer(int radius) {
        if (Objects.isNull(this.getLevel())) return;
        BlockPos flowerPos = this.getBlockPos();
        BlockState fogState = BlockRegistry.FogBlock.get().defaultBlockState().setValue(FogBlock.sourceStillExists, true);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -radius; z <= radius; z++) {

                    int distSq = x * x + y * y + z * z;
                    if (distSq <= radius * radius && distSq > (radius - 1) * (radius - 1) - 1.0) {

                        BlockPos targetPos = flowerPos.offset(x, y, z);

                        double distance = Math.sqrt(distSq);
                        if (distance <= CLEAN_FOG_RADIUS) {
                            continue; // Innerhalb der Clear Zone
                        }

                        if (this.getLevel().getBlockState(targetPos).isAir()) {
                            this.getLevel().setBlockAndUpdate(targetPos, fogState);
                            this.fogPositions.add(targetPos);
                        }
                    }
                }
            }
        }
    }

    public void nightActivationStatusChanged() {
        this.fogTickCounter = 0;
        this.currentRadius = CLEAN_FOG_RADIUS;
        setChanged();
    }

    public void notifyFogBlocksAboutFlowerRemoval() {
        if(this.level == null) return;
        if(this.level.isClientSide) return;
        for(BlockPos pos : fogPositions) {
            var state = this.level.getBlockState(pos);
            if(state.is(BlockRegistry.FogBlock.get())) {
                level.setBlock(pos, state.setValue(FogBlock.sourceStillExists, false), 6);
            }

        }
    }

}
