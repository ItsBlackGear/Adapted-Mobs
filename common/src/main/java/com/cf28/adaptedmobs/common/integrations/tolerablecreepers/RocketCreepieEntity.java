package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.common.util.RocketFlightMath;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RocketCreepieEntity extends Creepie {
    private static final EntityDataAccessor<Boolean> ROCKETING =
            SynchedEntityData.defineId(RocketCreepieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final float ROTATION_TICKS = 30.0F;
    private static final int LANDING_DELAY = 4;
    private static final int FLIGHT_TICKS = RocketFlightMath.predictFlightTicks(1.2);
    private static final float ROCKET_ANIMATION_SPEED = ROTATION_TICKS / FLIGHT_TICKS;

    public final AnimationState launchAnimationState = new AnimationState();

    private final int launchDelay;
    private int ticksAlive;
    private int landingTimer = -1;
    private int flightTicksRemaining = -1;

    public RocketCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(100);
        this.launchDelay = this.random.nextInt(21);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ROCKETING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canFight() {
        return false;
    }

    @Override
    public void setAge(int age) {
        if (!this.level().isClientSide() && age >= 0) {
            this.convertTo(AMEntityTypes.ROCKET_CREEPER.get(), false);
            return;
        }
        super.setAge(age);
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
            if (this.landingTimer >= 0) {
                if (this.landingTimer-- == 0) {
                    this.explodeCustom();
                }
                return;
            }
            if (this.isRocketing()) {
                this.hasImpulse = true;
                if (--this.flightTicksRemaining <= 0) {
                    this.startLanding();
                }
            } else if (this.onGround() && this.ticksAlive >= this.launchDelay && RocketFlightMath.hasEnoughVerticalSpace(this)) {
                this.launch();
            }
            this.ticksAlive++;
        }
    }

    private void launch() {
        this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 0.5F);
        this.setDeltaMovement(0.0D, 1.2D, 0.0D);
        this.hasImpulse = true;
        this.fallDistance = 0.0F;
        this.setRocketing(true);
        this.flightTicksRemaining = FLIGHT_TICKS;
    }

    @Override
    public void setSwellDir(int swellDir) {
        super.setSwellDir(-1);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        if (this.isRocketing()) {
            this.startLanding();
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    private void startLanding() {
        this.setDeltaMovement(Vec3.ZERO);
        this.landingTimer = LANDING_DELAY;
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.setRocketing(false);
        this.dead = true;
        this.level().explode(
                this,
                null,
                null,
                this.getX(), this.getY(), this.getZ(),
                1.0F,
                false,
                Level.ExplosionInteraction.NONE,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.FIREWORK_ROCKET_TWINKLE)
        );
        ServerLevel sl = (ServerLevel) this.level();
        TolerableCreepersIntegration.spawnParticleRing(sl, AMParticles.ROCKET_SPORES.get(), this.position().add(0.0, 0.1, 0.0), 0.6, 16);
        this.discard();
    }

    public boolean isRocketing() {
        return this.entityData.get(ROCKETING);
    }

    public void setRocketing(boolean rocketing) {
        this.entityData.set(ROCKETING, rocketing);
    }

    public float getLaunchSpeed() {
        return ROCKET_ANIMATION_SPEED;
    }
}
