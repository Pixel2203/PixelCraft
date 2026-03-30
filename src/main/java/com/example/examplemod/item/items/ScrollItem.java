package com.example.examplemod.item.items;

import com.example.examplemod.api.scroll.ScrollRegistry;
import com.example.examplemod.api.scroll.ScrollSpell;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.entities.ScrollEntity;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
public class ScrollItem extends Item {

    private final String spellName;
    public ScrollItem(String spellName) {
        super(new Item.Properties().stacksTo(1));
        this.spellName = spellName;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(level.isClientSide() || interactionHand != InteractionHand.MAIN_HAND ||  !canSpawn() || Objects.isNull(spellName)){
            return super.use(level, player, interactionHand);
        }

        ScrollEntity scrollEntity = EntityRegistry.SCROLL_ENTITY.get().create(level);

        Supplier<ScrollSpell> spell = ScrollRegistry.get(this.spellName);

        if(Objects.isNull(spell)){
            log.error("Unable to find Spell {} while instantiating Scroll Entity", this.spellName);
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        }

        scrollEntity.setScrollEffect(spell.get());
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
        return false;
    }
}
