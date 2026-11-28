package com.cf28.adaptedmobs.common.level.entity;

import com.cf28.adaptedmobs.common.level.entity.mob.Sporal;
import com.cf28.adaptedmobs.common.registries.AMMobEffects;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class SporeCloud extends Entity { // size comes from the entity type
    public static final int DURATION = 40;
    public static final int EFFECT_DURATION = 200;
    private static final int APPLY_INTERVAL = 5;

    public SporeCloud(EntityType<? extends SporeCloud> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.spawnParticles();
            return;
        }

        if (this.tickCount >= DURATION) {
            this.discard();
            return;
        }

        if (this.tickCount % APPLY_INTERVAL == 0) {
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), SporeCloud::canAffect)) {
                entity.addEffect(new MobEffectInstance(AMMobEffects.SPORE, EFFECT_DURATION, 0, false, false, true), this);
            }
        }
    }

    private static boolean canAffect(LivingEntity entity) {
        return entity.isAlive() && !(entity instanceof Sporal) && !entity.isSpectator() && entity.isAffectedByPotions();
    }

    private void spawnParticles() {
        float remaining = 1.0F - (float) this.tickCount / DURATION;
        if (remaining <= 0.0F) return;

        AABB box = this.getBoundingBox();
        int smoke = Math.round(6 * remaining) + 1;
        for (int i = 0; i < smoke; i++) {
            this.level().addParticle(AMParticles.SPORE_SMOKE.get(), this.randomX(box), this.randomY(box), this.randomZ(box), 0.0, 0.0, 0.0);
        }

        int spores = Math.round(4 * remaining) + 1;
        for (int i = 0; i < spores; i++) {
            double xSpeed = (this.random.nextDouble() - 0.5) * 0.05;
            double ySpeed = this.random.nextDouble() * 0.04;
            double zSpeed = (this.random.nextDouble() - 0.5) * 0.05;
            this.level().addParticle(AMParticles.SPORE.get(), this.randomX(box), this.randomY(box), this.randomZ(box), xSpeed, ySpeed, zSpeed);
        }
    }

    private double randomX(AABB box) {
        return box.minX + this.random.nextDouble() * box.getXsize();
    }

    private double randomY(AABB box) {
        return box.minY + this.random.nextDouble() * box.getYsize();
    }

    private double randomZ(AABB box) {
        return box.minZ + this.random.nextDouble() * box.getZsize();
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
    }
}
