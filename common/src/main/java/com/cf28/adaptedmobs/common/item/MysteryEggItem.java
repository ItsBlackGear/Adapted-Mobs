package com.cf28.adaptedmobs.common.item;

import com.cf28.adaptedmobs.common.entity.ThrownMysteryEgg;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class MysteryEggItem extends Item {
    private final Supplier<? extends EntityType<? extends TamableCreeper>> entity;

    public MysteryEggItem(Supplier<? extends EntityType<? extends TamableCreeper>> entity, Properties properties) {
        super(properties);
        this.entity = entity;
    }

    @Override @SuppressWarnings("unchecked")
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            ThrownMysteryEgg egg = new ThrownMysteryEgg(level, player);
            egg.setItem(stack);
            egg.setCreeper((Supplier<EntityType<? extends TamableCreeper>>) this.entity);
            egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(egg);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return super.use(level, player, hand);
    }
}