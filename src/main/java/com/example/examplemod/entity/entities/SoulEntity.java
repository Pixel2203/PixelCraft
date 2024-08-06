package com.example.examplemod.entity.entities;

import com.example.examplemod.api.BlockInfo;
import com.example.examplemod.api.ModUtils;
import com.example.examplemod.api.nbt.CustomNBTTags;
import com.example.examplemod.config.entity.SoulConfig;
import com.example.examplemod.entity.entities.generalEntities.GeneralSoulEntity;
import com.example.examplemod.networking.NetworkMessages;
import com.example.examplemod.networking.packets.CustomParticlePackage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SoulEntity extends GeneralSoulEntity {
    /**
     * Definitions of the Additional Sync Data Attributes
     */
    private static final EntityDataAccessor<Integer> SPAWNED_POS = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.FLOAT);



    private static final Logger log = LoggerFactory.getLogger(SoulEntity.class);
    @Nullable
    private Float highestEntityEnergy;
    private boolean alreadyGenerated = false;
    private Integer nextRandomTick;
    private Integer ticker;
    private final Random random = new Random();




    public SoulEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    /**
     * Simply defines the additional data that should be synchronised between Server and Client
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWNED_POS, 0);
        this.entityData.define(ENERGY,0f);
    }

    /**
     * Determines whether the highestEnergy already has been calculated or not, if not then it will be calculated
     * and saved in the entity. If it has already been saved in the entity, it will simply return it
     * @return Energy level of the Entity when it had spawned.
     */

    private float getHighestEntityEnergy() {
        if(Objects.isNull(highestEntityEnergy)){
            this.highestEntityEnergy = random.nextFloat(SoulConfig.DEFAULT_SOUL_ENTITY_ENERGY);
            return highestEntityEnergy;
        }
        return highestEntityEnergy;
    }

    /**
     * Loads all necessary data into the entity when it is being added to the world.
     * Only for First-Time-Added Entities, if it was spawned already it will ignore this
     * because of the if condition
     */
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(!alreadyGenerated) {
            entityData.set(ENERGY, getHighestEntityEnergy());
            entityData.set(SPAWNED_POS, this.getOnPos().getY());
            alreadyGenerated = true;
        }
    }

    /**
     * Summons the SoulParticles that can be picked up via the crystal
     * @param serverLevel ServerLevel in which the Particles will be spawned
     */
    private void summonSoulParticles(ServerLevel serverLevel) {
        float currentEnergy = this.entityData.get(ENERGY);
        float retrieved= random.nextFloat(currentEnergy);
        if(retrieved < 0.1f){
            retrieved = Math.min(currentEnergy, getHighestEntityEnergy() / 10);
        }
        float newEnergyLevel = currentEnergy - retrieved;
        this.entityData.set(ENERGY, newEnergyLevel);
        if(newEnergyLevel <= 0){
            this.discard();
        }


        // Get Spawn Position
        AABB boundingBox = this.getBoundingBox().inflate(3);
        List<BlockInfo> airBlocks = ModUtils.getBlocksInBoundingBox(serverLevel, boundingBox).stream()
                .filter(blockInfo -> blockInfo.blockState().isAir()).toList();


        if(airBlocks.isEmpty()){
            // Can not spawn particles becuase no air block is nearby
            return;
        }
        BlockPos spawnPos = airBlocks.get(random.nextInt(airBlocks.size())).blockPos();
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        areaeffectcloud.setRadius(retrieved);
        areaeffectcloud.setDuration(SoulConfig.SOUL_PARTICLE_LIFESPAN);
        areaeffectcloud.setOwner(this);
        serverLevel.addFreshEntity(areaeffectcloud);
    }

    /**
     * Determines whether the entity should be rendered to a player or not.
     * @param player Player which should be checked if he is allowed to see the entity
     * @return Returns a boolean deciding if the player is allowed to see the entity or nor
     */
    public boolean renderToPlayer(Player player) {
        return player.getInventory()
                .getArmor(ModUtils.ArmorSlots.HELMET)
                .is(Items.IRON_HELMET);
    }

    /**
     * Handles the behaviour of the Entity every tick
     */
    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide()) {
            this.setDeltaMovement(new Vec3(0, this.getAttributeValue(Attributes.MOVEMENT_SPEED), 0));
            float probability = this.entityData.get(ENERGY) / getHighestEntityEnergy();
            sendParticlesToPlayers((ServerLevel) level(), probability);
            checkFlightDistanceReached();
            checkRandomTick();
        }
    }

    /**
     * Simulates a randomTick method for the entity
     * Random Ticks do not exist for entities, so this is some boilerplate code to simulate random Ticks
     * Maybe try to outsource it into a separate Object in the future
     */
    private void checkRandomTick() {
        if(Objects.isNull(ticker)){
            ticker = 0;
            nextRandomTick = random.nextInt(SoulConfig.MINIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN , SoulConfig.MAXIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN);
            return;
        }
        if(ticker >= nextRandomTick){
            ticker = 0;
            nextRandomTick = random.nextInt(SoulConfig.MINIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN , SoulConfig.MAXIMUM_TICKS_TO_COLLECTABLE_PARTICLE_SPAWN);
            this.onRandomTick((ServerLevel) level());
        }
        ticker++;
    }

    /**
     * Handles the behavior if the entity gets a random tick from the custom written checkRandomTick method
     * @param serverLevel ServerLevel of the World in which the tick happened
     */
    private void onRandomTick(ServerLevel serverLevel) {
        summonSoulParticles(serverLevel);
    }

    /**
     * Sends the particles that spawn nearby the entity to the player
     * Notice that the default sendParticle method is used here, this is because not everyone should be able to see the particles
     * Only those players that are allowed to see the particle should be provided with them. So we check for all players on the Server
     * And if they fulfill a certain condition, a particle package will be sent towards them
     * @param serverLevel ServerLevel of the World in which the players are
     * @param probability Probability of the particles spawning
     */
    private void sendParticlesToPlayers(ServerLevel serverLevel, float probability) {
        var players = serverLevel.getPlayers(serverPlayer -> true);
        AABB boundingBox = this.getBoundingBox().inflate(2);
        var blocks = ModUtils.getBlocksInBoundingBox(serverLevel, boundingBox);
        if(blocks.isEmpty()){
            return;
        }
        BlockPos pos = blocks.get(random.nextInt(blocks.size())).blockPos();
        for(ServerPlayer player : players) {
            if(renderToPlayer(player)){
                NetworkMessages.sendToClient(new CustomParticlePackage(pos.getX(), pos.getY(), pos.getZ(), 0,0,0), player);
            }
        }

    }

    private void checkFlightDistanceReached() {
        int heightDifference = this.getOnPos().getY() - this.entityData.get(SPAWNED_POS);
        if(heightDifference >= SoulConfig.SOUL_DISAPPEARANCE_HEIGHT) {
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt(CustomNBTTags.SPAWNED_HEIGHT, this.entityData.get(SPAWNED_POS));
        nbt.putFloat(CustomNBTTags.ENERGY_CHARGE, this.entityData.get(ENERGY));
        nbt.putBoolean(CustomNBTTags.ALREADY_GENERATED, alreadyGenerated);
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(SPAWNED_POS,nbt.getInt(CustomNBTTags.SPAWNED_HEIGHT));
        this.entityData.set(ENERGY,nbt.getFloat(CustomNBTTags.ENERGY_CHARGE));
        this.alreadyGenerated = nbt.getBoolean(CustomNBTTags.ALREADY_GENERATED);
    }

    public void setEnergy(float energy) {
        this.highestEntityEnergy = energy;
    }


}
