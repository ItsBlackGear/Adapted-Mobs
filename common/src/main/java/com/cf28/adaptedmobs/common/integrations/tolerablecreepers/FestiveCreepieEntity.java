package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;

public class FestiveCreepieEntity extends Creepie {
    public FestiveCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(20);
    }

    @Override
    public boolean canMove() {
        return false;
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
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        this.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE);
        this.setAggressive(false);
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
