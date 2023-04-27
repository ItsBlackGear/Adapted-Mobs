package com.cf28.adaptedmobs.core.mixin;

import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster {
    private static final int CHARGE_COOLDOWN = 6000;
    private int cooldownTicks = -1;

    @Shadow public abstract boolean isPowered();
    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_IS_POWERED;

    protected CreeperMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void am$tick(CallbackInfo ci) {
        if (!this.isPowered()) {
            this.checkForBuff();
        }

        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
            if (this.cooldownTicks == 0) {
                this.entityData.set(DATA_IS_POWERED, false);
                this.cooldownTicks = -1;
            }
        }
    }

    private void checkForBuff() {
        SupportCreeper support = this.level.getNearestEntity(SupportCreeper.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expandTowards(10.0F, 10.0F, 10.0F));
        if (support != null && support.isSupportingTarget(this) && !this.isDeadOrDying()) {
            this.cooldownTicks = CHARGE_COOLDOWN;
            this.entityData.set(DATA_IS_POWERED, true);
            this.level.playSound(null, this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }
}