package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.entities.ScrollEntity;
import com.example.examplemod.entity.entities.SoulEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<ScrollEntity>> SCROLL_ENTITY = ENTITIES.register("example", () -> EntityType.Builder.of(ScrollEntity::new, MobCategory.MISC).sized(1,1).build(ExampleMod.MODID + ":example"));
    public static final RegistryObject<EntityType<SoulEntity>> SOUL_ENTITY = ENTITIES.register("soul", () -> EntityType.Builder.of(SoulEntity::new, MobCategory.MISC).build(ExampleMod.MODID + ":example"));
}
