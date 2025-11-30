package com.example.examplemod.menus;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MOD_MENU_TYPE =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExampleMod.MODID);


    public static final RegistryObject<MenuType<DistilleryBowlMenu>> DISTILLERY_BOWL_MENU =
            registerMenuType("distillery_bowl_menu", DistilleryBowlMenu::new);
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MOD_MENU_TYPE.register(name,() -> IForgeMenuType.create(factory));
    }


    public static void register(IEventBus bus) {
        MOD_MENU_TYPE.register(bus);
    }
}
