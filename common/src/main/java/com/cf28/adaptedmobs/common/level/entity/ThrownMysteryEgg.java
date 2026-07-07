package com.cf28.adaptedmobs.common.level.entity;

import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
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

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(
                    new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    ((double) this.random.nextFloat() - 0.5) * 0.08,
                    ((double) this.random.nextFloat() - 0.5) * 0.08,
                    ((double) this.random.nextFloat() - 0.5) * 0.08
                );
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide && this.creeper != null) {
            TamableCreeper creeper = this.creeper.get().create(this.level());
            if (creeper != null && this.getOwner() != null) {
                creeper.setOwnerUUID(this.getOwner().getUUID());
                creeper.setTame(true);
                creeper.setBaby(true);
                creeper.setPersistenceRequired();
                creeper.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                this.level().addFreshEntity(creeper);
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    public void setCreeper(@Nullable Supplier<EntityType<? extends TamableCreeper>> creeper) {
        this.creeper = creeper;
    }

    public @Nullable Supplier<EntityType<? extends TamableCreeper>> getCreeper() {
        return this.creeper;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return AMItems.GREEN_MYSTERY_EGG.get();
    }
}