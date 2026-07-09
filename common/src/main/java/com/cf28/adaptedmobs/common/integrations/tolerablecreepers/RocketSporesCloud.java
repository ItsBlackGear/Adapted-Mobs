package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.CreeperSpores;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RocketSporesCloud extends CreeperSpores {
    @SuppressWarnings("unchecked")
    public RocketSporesCloud(EntityType<?> type, Level level) {
        super((EntityType<? extends CreeperSpores>) type, level);
    }

    public void setCloudSizeDirect(int size) {
        this.setCloudSize(size);
    }

    @Override
    protected int getInitialCloudTime(int cloudSize) {
        return 60;
    }

    @Override
    protected void tickLandedClient() {
    }

    @Override
    protected void tickLandedServer() {
        if (this.getCloudTime() % 3 == 0) {
            ServerLevel sl = (ServerLevel) this.level();
            sl.sendParticles(AMParticles.ROCKET_SPORES.get(),
                    this.getX(), this.getY() + 0.5, this.getZ(),
                    5, this.getCloudSize() * 0.5, 0.3, this.getCloudSize() * 0.5, 0.04);
        }

        if (this.getCloudTime() % 5 == 0) {
            trySpawnCreepie();
        }
    }

    private void trySpawnCreepie() {
        int cloudSize = this.getCloudSize();
        for (int attempt = 0; attempt < 4 * cloudSize; attempt++) {
            float theta = (float) (this.random.nextFloat() * 2 * Math.PI);
            float phi = (float) (this.random.nextFloat() * 2 * Math.PI);
            double xPos = this.getX() + Mth.sin(phi) * Mth.cos(theta) * cloudSize * this.random.nextFloat();
            double zPos = this.getZ() + Mth.cos(phi) * cloudSize * this.random.nextFloat();
            Vec3 spawnPos = new Vec3(xPos, this.getY(), zPos);

            if (this.level().clip(new ClipContext(this.position(), spawnPos,
                            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this))
                    .getType() == HitResult.Type.MISS) {
                Creepie creepie = this.createCreepie(spawnPos);
                if (this.level().noCollision(creepie, creepie.getBoundingBox())) {
                    this.level().addFreshEntity(creepie);
                    this.setCloudSize(cloudSize - 1);
                }
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Creepie createCreepie(Vec3 pos) {
        RocketCreepieEntity creepie = new RocketCreepieEntity(
                (EntityType<? extends Creepie>) (EntityType<?>) AMEntityTypes.ROCKET_CREEPIE.get(), this.level());
        creepie.setPos(pos);
        return creepie;
    }
}
