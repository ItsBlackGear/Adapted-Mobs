package com.cf28.adaptedmobs.common.entity;

import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ThrownMysteryEgg extends ThrowableItemProjectile {
    @Nullable private Supplier<EntityType<? extends TamableCreeper>> creeper;

    public ThrownMysteryEgg(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownMysteryEgg(Level level, LivingEntity entity) {
        super(AMEntityTypes.MYSTERY_EGG.get(), entity, level);
    }

    public ThrownMysteryEgg(Level level, double x, double y, double z) {
        super(AMEntityTypes.MYSTERY_EGG.get(), x, y, z, level);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide && this.creeper != null) {
            TamableCreeper creeper = this.creeper.get().create(this.level);
            if (creeper != null && this.getOwner() != null) {
                creeper.setOwnerUUID(this.getOwner().getUUID());
                creeper.setTame(true);
                creeper.setBaby(true);
                creeper.setPersistenceRequired();
                creeper.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                this.level.addFreshEntity(creeper);
            }

            this.level.broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    public void setCreeper(Supplier<EntityType<? extends TamableCreeper>> creeper) {
        this.creeper = creeper;
    }

    public Supplier<EntityType<? extends TamableCreeper>> getCreeper() {
        return this.creeper;
    }

    @Override
    protected Item getDefaultItem() {
        return this.getItemRaw().getItem();
    }
}