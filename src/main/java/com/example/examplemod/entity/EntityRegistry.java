package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.entities.ScrollEntity;
import com.example.examplemod.entity.entities.SoulEntity;
import com.example.examplemod.entity.entities.SoulLightEntity;
import com.example.examplemod.entity.entities.SoulWispEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<ScrollEntity>> SCROLL_ENTITY = ENTITIES.register("example", () -> EntityType.Builder.of(ScrollEntity::new, MobCategory.MISC).sized(1,1).build(ExampleMod.MODID + ":scroll"));
    public static final RegistryObject<EntityType<SoulEntity>> SOUL_ENTITY = ENTITIES.register("soul", () -> EntityType.Builder.of(SoulEntity::new, MobCategory.MISC).sized(6/16f,6/16f).build(ExampleMod.MODID + ":soul"));
    public static final RegistryObject<EntityType<SoulWispEntity>> SOUL_WISP_ENTITY = ENTITIES.register("soul_wisp", () -> EntityType.Builder.of(SoulWispEntity::new, MobCategory.MISC).sized(0.1f, 0.1f).build(ExampleMod.MODID + ":soul_wisp"));
    public static final RegistryObject<EntityType<SoulLightEntity>> SOUL_LIGHT_ENTITY = ENTITIES.register("soul_light", () -> EntityType.Builder.of(SoulLightEntity::new, MobCategory.MISC).sized(1f, 1f).build(ExampleMod.MODID + ":soul_wisp"));

}
