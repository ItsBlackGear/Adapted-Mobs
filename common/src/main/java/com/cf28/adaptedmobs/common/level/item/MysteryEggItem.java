package com.cf28.adaptedmobs.common.level.item;

import com.cf28.adaptedmobs.common.level.entity.ThrownMysteryEgg;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
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
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class MysteryEggItem extends Item {
    private final Supplier<? extends EntityType<? extends TamableCreeper>> creeper;
    
    public MysteryEggItem(Supplier<? extends EntityType<? extends TamableCreeper>> creeper, Properties properties) {
        super(properties);
        this.creeper = creeper;
    }
    
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        
        if (!level.isClientSide) {
            ThrownMysteryEgg egg = new ThrownMysteryEgg(level, player);
            egg.setItem(stack);
            egg.setCreeper((Supplier<EntityType<? extends TamableCreeper>>) this.creeper);
            egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(egg);
        }
        
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}