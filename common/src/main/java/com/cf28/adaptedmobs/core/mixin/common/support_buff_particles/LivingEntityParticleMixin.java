package com.cf28.adaptedmobs.core.mixin.common.support_buff_particles;

import com.cf28.adaptedmobs.common.registries.AMMobEffects;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityParticleMixin {
    @Unique
    private static SimpleParticleType am$pickParticle(LivingEntity entity) {
        List<SimpleParticleType> active = new ArrayList<>();
        if (entity.hasEffect(AMMobEffects.SUPPORT_SPEED)) {
            active.add(AMParticles.SUPPORTED_YELLOW.get());
        }
        if (entity.hasEffect(AMMobEffects.SUPPORT_STRENGTH)) {
            active.add(AMParticles.SUPPORTED_RED.get());
        }
        if (entity.hasEffect(AMMobEffects.SUPPORT_SLOWNESS)) {
            active.add(AMParticles.SUPPORTED_BLUE.get());
        }
        if (entity.hasEffect(AMMobEffects.SUPPORT_WEAKNESS)) {
            active.add(AMParticles.SUPPORTED_GREY.get());
        }

        if (active.isEmpty()) {
            return null;
        }
        return active.get(entity.getRandom().nextInt(active.size()));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void am$spawnSupportBuffParticles(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!self.level().isClientSide() || self.getRandom().nextInt(3) != 0) {
            return;
        }

        SimpleParticleType particle = am$pickParticle(self);
        if (particle == null) {
            return;
        }

        double x = self.getX() + (self.getRandom().nextDouble() - 0.5) * self.getBbWidth();
        double y = self.getY() + self.getRandom().nextDouble() * self.getBbHeight();
        double z = self.getZ() + (self.getRandom().nextDouble() - 0.5) * self.getBbWidth();
        double xSpeed = (self.getRandom().nextDouble() - 0.5) * 0.05;
        double ySpeed = self.getRandom().nextDouble() * 0.05;
        double zSpeed = (self.getRandom().nextDouble() - 0.5) * 0.05;
        self.level().addParticle(particle, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}
