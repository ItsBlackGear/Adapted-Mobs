package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FestiveCreepieEntity extends Creepie {
    public FestiveCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(20);
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.dead = true;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
        ServerLevel sl = (ServerLevel) this.level();
        sl.sendParticles(AMParticles.FESTIVE_SPORES.get(),
                this.getX(), this.getY() + 0.25, this.getZ(), 60, 0.5, 0.5, 0.5, 0.15);
        this.discard();
    }
}
