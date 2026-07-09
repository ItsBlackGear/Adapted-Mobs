package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.level.entity.PrimedFestiveTnt;
import com.evandev.tolerable_creepers.common.item.CreeperSporesItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
            PrimedFestiveTnt tnt = new PrimedFestiveTnt(
                    level, player.getX(), player.getEyeY() - 0.1, player.getZ(), player);
            tnt.setFuse(60);
            tnt.setSmall(true);
            float pitch = player.getXRot();
            float yaw = player.getYRot();
            float f = -Mth.sin(yaw * ((float) Math.PI / 180.0F)) * Mth.cos(pitch * ((float) Math.PI / 180.0F));
            float g = -Mth.sin(pitch * ((float) Math.PI / 180.0F));
            float h = Mth.cos(yaw * ((float) Math.PI / 180.0F)) * Mth.cos(pitch * ((float) Math.PI / 180.0F));
            Vec3 dir = new Vec3(f, g, h).normalize()
                    .add(player.getRandom().triangle(0, 0.0172275),
                            player.getRandom().triangle(0, 0.0172275),
                            player.getRandom().triangle(0, 0.0172275))
                    .scale(1.5);
            tnt.setDeltaMovement(dir);
            level.addFreshEntity(tnt);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.isCreative()) stack.shrink(1);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
