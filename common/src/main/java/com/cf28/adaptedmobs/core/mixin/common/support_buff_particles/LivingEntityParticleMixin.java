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

@Mixin(LivingEntity.class)
public abstract class LivingEntityParticleMixin {
    @Unique
    private static SimpleParticleType am$pickParticle(LivingEntity entity) {
        if (entity.hasEffect(AMMobEffects.SUPPORT_SPEED)) {
            return AMParticles.SUPPORTED_YELLOW.get();
        } else if (entity.hasEffect(AMMobEffects.SUPPORT_STRENGTH)) {
            return AMParticles.SUPPORTED_RED.get();
        } else if (entity.hasEffect(AMMobEffects.SUPPORT_SLOWNESS)) {
            return AMParticles.SUPPORTED_BLUE.get();
        } else if (entity.hasEffect(AMMobEffects.SUPPORT_WEAKNESS)) {
            return AMParticles.SUPPORTED_GREY.get();
        }

        return null;
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
