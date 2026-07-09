package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RocketCreepieEntity extends Creepie {
    private static final EntityDataAccessor<Boolean> ROCKETING =
            SynchedEntityData.defineId(RocketCreepieEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState launchAnimationState = new AnimationState();

    private int timeBeforeJumping;

    public RocketCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(40);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ROCKETING, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.isRocketing()) {
                this.launchAnimationState.startIfStopped(this.tickCount);
            } else {
                this.launchAnimationState.stop();
            }
        } else {
            this.launchTowardsTarget();
        }
    }

    private void launchTowardsTarget() {
        LivingEntity target = this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        if (target != null && this.distanceToSqr(target) > 25.0) {
            this.setSwellDir(-1);
        }

        if (this.getSwellDir() > 0) {
            this.timeBeforeJumping++;
        } else {
            this.timeBeforeJumping = 0;
        }

        if (this.shouldRocket(target)) {
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 0.5F);
            this.setDeltaMovement((target.getX() - this.getX()) / 6.0D, 1.2D, (target.getZ() - this.getZ()) / 6.0D);
            this.hasImpulse = true;
            this.fallDistance = 0.0F;
            this.setRocketing(true);
        }
    }

    private boolean shouldRocket(LivingEntity target) {
        return target != null
                && this.timeBeforeJumping > 15
                && this.isAlive()
                && this.getSwellDir() > 0
                && this.onGround();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (this.isRocketing()) {
            this.setRocketing(false);
            this.explodeCustom();
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.setRocketing(false);
        this.dead = true;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
        ServerLevel sl = (ServerLevel) this.level();
        sl.sendParticles(AMParticles.ROCKET_SPORES.get(),
                this.getX(), this.getY() + 0.25, this.getZ(), 30, 0.4, 0.4, 0.4, 0.1);
        this.discard();
    }

    public boolean isRocketing() {
        return this.entityData.get(ROCKETING);
    }

    public void setRocketing(boolean rocketing) {
        this.entityData.set(ROCKETING, rocketing);
    }
}
