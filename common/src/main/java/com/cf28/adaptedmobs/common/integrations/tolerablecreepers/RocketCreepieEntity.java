package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class RocketCreepieEntity extends Creepie {
    private boolean launched = false;

    public RocketCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(15);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && !launched) {
            launched = true;
            this.setSwellDir(1);
            this.setDeltaMovement(this.getDeltaMovement().x, 0.6, this.getDeltaMovement().z);
            this.hasImpulse = true;
        }
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.dead = true;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
        ServerLevel sl = (ServerLevel) this.level();
        sl.sendParticles(AMParticles.ROCKET_SPORES.get(),
                this.getX(), this.getY() + 0.25, this.getZ(), 30, 0.4, 0.4, 0.4, 0.1);
        this.discard();
    }
}
