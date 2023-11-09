package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * FEATURES:
 * - when it spots you, it launches itself up into the air before crashing down on you with an explosion *
 * - when there is not enough space to jump, they'll act like regular creepers and explode normally *
 * - they have a rare chance of dropping a blue Mystery Egg that hatches a tame baby Rocket Creeper
 * - when it grows up, it will be able to fight alongside you
 * ADDITIONAL CHANGES:
 * - when charged, it has a larger explosion radius / higher damage
 * LOOT:
 * - gunpowder [similar to creepers]
 * - light blue creeper burst firework star [rare]
 * - blue mystery egg [very rare]
 */
public class RocketCreeper extends TamableCreeper {
    private static final EntityDataAccessor<Boolean> IS_ROCKET = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);
    private int timeBeforeJumping;

    public RocketCreeper(EntityType<? extends TamableCreeper> entityType, Level level) {
        super(entityType, level);
        this.setRocket(false);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 10;
    }

    @Override
    protected void postExplosion() {
        super.postExplosion();
        this.setRocket(false);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (this.isRocket()) {
            this.setSwellDir(-1);
            this.causeExplosion();
        }

        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    public boolean hasEnoughVerticalSpace() {
        BlockPos pos = this.blockPosition();
        while (pos.getY() < this.level.getHeight()) {
            BlockState state = this.level.getBlockState(pos);
            if (!state.getMaterial().isReplaceable()) {
                return false;
            }

            pos = pos.above();
        }

        return true;
    }

    @Override
    public void tick() {
        super.tick();

        this.launchTowardsTarget();
        if (this.level.isClientSide && this.isRocket()) {
            this.spawnSmokeParticles();
        }

        if (this.isInWaterOrBubble() && this.getState().is(CreeperState.ATTACKING)) {
            this.setState(CreeperState.IDLING);
        }
    }

    private void launchTowardsTarget() {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (this.distanceToSqr(target) > 25) {
                this.setSwellDir(-1);
            }
        }

        if (this.getSwellDir() > 0) {
            this.timeBeforeJumping++;
        } else {
            this.timeBeforeJumping = 0;
        }

        if (this.shouldRocket() && target != null) {
            this.setState(CreeperState.ATTACKING);
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 0.5F);
            this.setDeltaMovement((target.getX() - this.getX()) / 6.0D, 1.2D, (target.getZ() - this.getZ()) / 6.0D);

            this.setRocket(true);
            this.fallDistance = 0.0F;
        }
    }

    private boolean shouldRocket() {
        return this.timeBeforeJumping > 15 && this.isAlive() && this.getSwellDir() > 0 && this.onGround && this.hasEnoughVerticalSpace();
    }

    private void spawnSmokeParticles() {
        double xSpeed = this.random.nextGaussian() * 0.02D;
        double ySpeed = this.random.nextGaussian() * 0.02D;
        double zSpeed = this.random.nextGaussian() * 0.02D;
        this.level.addParticle(ParticleTypes.SMOKE,
                this.getX(),
                this.getY(),
                this.getZ(),
                xSpeed, ySpeed, zSpeed
        );
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer) {
        super.dropCustomDeathLoot(damageSource, looting, hitByPlayer);

        if (hitByPlayer && Math.max(this.random.nextFloat() - (float)looting * 0.01F, 0.0F) < 0.2F) {
            ItemStack stack = new ItemStack(Items.FIREWORK_STAR);
            CompoundTag tag = stack.getOrCreateTagElement("Explosion");
            List<Integer> colors = Lists.newArrayList();
            colors.add(DyeColor.LIGHT_BLUE.getFireworkColor());
            tag.putIntArray("Colors", colors);
            tag.putByte("Type", (byte)FireworkRocketItem.Shape.CREEPER.getId());
            this.spawnAtLocation(stack);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ROCKET, false);
    }

    public void setRocket(boolean rocket) {
        this.entityData.set(IS_ROCKET, rocket);
    }

    public boolean isRocket() {
        return this.entityData.get(IS_ROCKET);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Rocket", this.isRocket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setRocket(tag.getBoolean("Rocket"));
    }

    @Override
    public ClothType getClothType() {
        return ClothType.ROCKET;
    }
}