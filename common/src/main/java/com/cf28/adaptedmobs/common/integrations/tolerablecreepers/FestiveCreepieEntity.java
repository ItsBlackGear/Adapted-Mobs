package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FestiveCreepieEntity extends Creepie {
    private static final int LANDED_FUSE_TICKS = 40;

    private boolean hasLanded;
    private int landedTicks;

    public FestiveCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.lookControl = new LookControl(this) {
            @Override
            public void tick() {
            }
        };
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
    protected void registerGoals() {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (!this.onGround()) {
                this.level().addParticle(AMParticles.FESTIVE_TNT_PARTICLETRAIL.get(), this.getX(), this.getY() + 0.25, this.getZ(), 0.0, 0.0, 0.0);
            }
        } else if (this.isAlive()) {
            if (!this.hasLanded && this.onGround()) {
                this.hasLanded = true;
            }
            if (this.hasLanded && ++this.landedTicks >= LANDED_FUSE_TICKS) {
                this.explodeCustom();
            }
        }
    }

    @Override
    public void setAge(int age) {
        if (!this.level().isClientSide() && age >= 0) {
            this.convertTo(AMEntityTypes.FESTIVE_CREEPER.get(), false);
            return;
        }
        super.setAge(age);
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.dead = true;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
        ServerLevel sl = (ServerLevel) this.level();
        TolerableCreepersIntegration.spawnParticleRing(sl, AMParticles.FESTIVE_SPORES.get(), this.position().add(0.0, 0.1, 0.0), 0.8, 20);
        this.discard();
    }
}
