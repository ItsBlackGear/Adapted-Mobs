package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.evandev.tolerable_creepers.common.item.CreeperSporesItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FestiveSporesItem extends CreeperSporesItem {
    public FestiveSporesItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide()) {
            Vec3 look = player.getLookAngle();
            Entity entity = TolerableCreepersIntegration.createFestiveCreepie(
                    level, player.getX() + look.x * 0.8, player.getEyeY() - 0.1 + look.y * 0.8, player.getZ() + look.z * 0.8);

            float yaw = player.getYRot();
            Vec3 dir = look.scale(1.2);

            if (entity instanceof LivingEntity creepie) {
                creepie.setDeltaMovement(dir);
                creepie.setYRot(yaw);
                creepie.setYBodyRot(yaw);
                creepie.setYHeadRot(yaw);
            }

            level.addFreshEntity(entity);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.isCreative()) stack.shrink(1);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
