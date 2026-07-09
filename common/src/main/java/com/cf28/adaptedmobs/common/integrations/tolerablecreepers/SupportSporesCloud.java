package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.CreeperSpores;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SupportSporesCloud extends CreeperSpores {
    private final boolean friendly;

    @SuppressWarnings("unchecked")
    public SupportSporesCloud(EntityType<?> type, Level level, boolean friendly) {
        super((EntityType<? extends CreeperSpores>) type, level);
        this.friendly = friendly;
    }

    public void setCloudSizeDirect(int size) {
        this.setCloudSize(size);
    }

    @Override
    protected void tickLandedClient() {
    }

    @Override
    protected void tickLandedServer() {
        if (this.getCloudTime() % 5 == 0) {
            ServerLevel sl = (ServerLevel) this.level();
            SimpleParticleType p = friendly
                    ? (this.random.nextBoolean() ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_RED.get())
                    : (this.random.nextBoolean() ? AMParticles.SUPPORTED_YELLOW.get() : AMParticles.SUPPORTED_GREY.get());
            sl.sendParticles(p, this.getX(), this.getY() + 0.5, this.getZ(),
                    3, this.getCloudSize() * 0.5, 0.3, this.getCloudSize() * 0.5, 0.02);
        }

        if (this.getCloudTime() % 20 == 0) {
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
        SupportCreepieEntity creepie = new SupportCreepieEntity(
                (EntityType<? extends Creepie>) (EntityType<?>) AMEntityTypes.SUPPORT_CREEPIE.get(), this.level());
        if (friendly) {
            creepie.setVariant(this.random.nextBoolean()
                    ? SupportCreepieEntity.Variant.SPEED : SupportCreepieEntity.Variant.STRENGTH);
            creepie.setOwner(this.getOwner());
        } else {
            creepie.setVariant(this.random.nextBoolean()
                    ? SupportCreepieEntity.Variant.SLOWNESS : SupportCreepieEntity.Variant.WEAKNESS);
        }
        creepie.setPos(pos);
        return creepie;
    }
}
