package com.example.examplemod.blockentity.entities;

import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.block.blocks.SoulFlower;
import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.blockentity.util.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Random;

public class SoulFlowerBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private static final Logger log = LoggerFactory.getLogger(SoulFlowerBlockEntity.class);
    private static final int MAX_FOG_RADIUS = 30;
    private static final int CLEAN_FOG_RADIUS = 5;
    private final Random random = new Random();
    private static final int TICKS_PER_RADIUS_INCREASE = 10;

    private int currentRadius = CLEAN_FOG_RADIUS;
    private int tickCounter = 0;
    // Innerhalb der Klasse SoulFlowerBlockEntity
    public SoulFlowerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegistry.SOUL_FLOWER_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("CurrentFogRadius", this.currentRadius);
        // NEU: Tick Counter speichern
        tag.putInt("TickCounter", this.tickCounter);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.currentRadius = tag.getInt("CurrentFogRadius");
        // NEU: Tick Counter laden
        this.tickCounter = tag.getInt("TickCounter");
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide) return;

        boolean isActive = this.getBlockState().getValue(SoulFlower.NIGHT_ACTIVE);
        if(!isActive) return;

        // Nur bauen, solange der max. Radius nicht erreicht ist
        if (this.currentRadius < MAX_FOG_RADIUS) {

            // Zähler erhöhen
            this.tickCounter++;

            // Prüfung: Hat der Zähler den Schwellenwert erreicht?
            if (this.tickCounter >= TICKS_PER_RADIUS_INCREASE) {

                // 1. Zähler zurücksetzen
                this.tickCounter = 0;

                // 2. Radius erhöhen und neue Schicht bauen
                this.currentRadius++;
                this.buildNewSphereLayer(this.currentRadius);

                // 3. Änderungen speichern
                this.setChanged();
            }
        } else {
            // Falls der maximale Radius erreicht ist, setzen wir den Zähler zurück.
            this.tickCounter = 0;
        }
    }

    // Ersetzt die alte spawnFog/spreadFog Methode
    private void buildNewSphereLayer(int radius) {
        if (Objects.isNull(this.getLevel())) return;
        BlockPos flowerPos = this.getBlockPos();
        BlockState fogState = BlockRegistry.FogBlock.get().defaultBlockState();

        // Wir iterieren in einem Würfel um die Blume, dessen Kantenlänge 2*Radius beträgt.
        for (int x = -radius; x <= radius; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -radius; z <= radius; z++) {

                    // Absolute Distanz zur Flower Position (Quadratwurzel vermieden für Performance)
                    int distSq = x * x + y * y + z * z;

                    // 1. Prüfung: Liegt der Block auf der Oberfläche des aktuellen Radius (Sphere)?
                    // Wir verwenden distSq, um nur die Blöcke auf der aktuellen Schicht zu finden.
                    //
                    // Der Block ist Teil der neuen Schicht, wenn:
                    // a) seine Distanz im Quadrat <= Radius im Quadrat ist.
                    // UND
                    // b) seine Distanz im Quadrat > (Radius - 1) im Quadrat ist.
                    // Wir fügen eine kleine Toleranz (z.B. 1.0) hinzu, um auch schräge Ecken zu erwischen,
                    // da wir ganzzahlige Koordinaten verwenden.
                    if (distSq <= radius * radius && distSq > (radius - 1) * (radius - 1) - 1.0) {

                        BlockPos targetPos = flowerPos.offset(x, y, z);

                        // 2. Clear Zone Prüfung:
                        // Wir verwenden die tatsächliche (nicht quadrierte) Distanz für die Clear Zone.
                        double distance = Math.sqrt(distSq);
                        if (distance <= CLEAN_FOG_RADIUS) {
                            continue; // Innerhalb der Clear Zone
                        }

                        // 3. Spawning-Prüfung: Nur Air überschreiben
                        if (this.getLevel().getBlockState(targetPos).isAir()) {
                            this.getLevel().setBlockAndUpdate(targetPos, fogState);
                        }
                    }
                }
            }
        }
    }

    public void nightActivationStatusChanged() {
        this.tickCounter = 0;
        this.currentRadius = CLEAN_FOG_RADIUS;
        setChanged();
    }


}
