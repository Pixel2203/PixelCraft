package com.example.examplemod.item.items;

import com.example.examplemod.API.scroll.ScrollSpell;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.ScrollEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ScrollItem extends Item {
    private ScrollSpell spell;
    public ScrollItem(ScrollSpell spell) {
        super(new Item.Properties().stacksTo(1));
        this.spell = spell;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(level.isClientSide() || interactionHand != InteractionHand.MAIN_HAND ||  !canSpawn() || Objects.isNull(spell)){
            return super.use(level, player, interactionHand);
        }

        ScrollEntity scrollEntity = EntityRegistry.SCROLL_ENTITY.get().create(level);
        scrollEntity.setScrollEffect(spell);
        scrollEntity.setPos(player.getX(),player.getY(),player.getZ());
        level.addFreshEntity(scrollEntity);
        player.getItemInHand(interactionHand).shrink(1);
        return super.use(level,player,interactionHand);
    }
    private boolean canSpawn(){
        return true;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }
}
