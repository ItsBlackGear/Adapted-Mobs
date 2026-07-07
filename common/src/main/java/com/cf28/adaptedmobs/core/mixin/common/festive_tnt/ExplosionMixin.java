package com.cf28.adaptedmobs.core.mixin.common.festive_tnt;

import com.cf28.adaptedmobs.common.level.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Inject(method = "makeDamageCalculator", at = @At("HEAD"), cancellable = true)
    private void am$redirectDamageCalculator(@Nullable Entity entity, CallbackInfoReturnable<ExplosionDamageCalculator> cir) {
        if (entity instanceof PrimedFestiveTnt || entity instanceof TamableCreeper) {
            cir.setReturnValue(new EntityBasedExplosionDamageCalculator(entity) {
                private boolean shouldIgnoreDamage(Entity target) {
                    Entity source = entity;
                    
                    if (entity instanceof PrimedFestiveTnt tnt) {
                        source = tnt.getOwner();
                    }
                    
                    if (source == target) {
                        return true;
                    }
                    
                    if (source instanceof TamableCreeper creeper) {
                        if (creeper.getOwnerUUID() != null) {
                            if (target instanceof LivingEntity livingTarget) {
                                LivingEntity owner = creeper.level().getPlayerByUUID(creeper.getOwnerUUID());
                                
                                return creeper.isOwnedBy(livingTarget) || (owner != null && !creeper.wantsToAttack(livingTarget, owner));
                            } else {
                                return true;
                            }
                        }
                    }
                    
                    return false;
                }
                
                @Override
                public boolean shouldDamageEntity(Explosion explosion, Entity target) {
                    return !this.shouldIgnoreDamage(target) && super.shouldDamageEntity(explosion, target);
                }
                
                @Override
                public float getKnockbackMultiplier(Entity target) {
                    return this.shouldIgnoreDamage(target) ? 0.0F : super.getKnockbackMultiplier(target);
                }
                
                @Override
                public float getEntityDamageAmount(Explosion explosion, Entity target) {
                    if (entity instanceof TamableCreeper creeper) {
                        float doubleRadius = explosion.radius() * 2.0F;
                        Vec3 center = explosion.center();
                        double dist = Math.sqrt(entity.distanceToSqr(center)) / doubleRadius;
                        double pow = (1.0 - dist) * (double) Explosion.getSeenPercent(center, entity);
                        return (float) ((pow * pow + pow) / 2.0 * 7.0 * doubleRadius + 1.0) * creeper.getExplosionDamageMultiplier();
                    } else {
                        return super.getEntityDamageAmount(explosion, target);
                    }
                }
            });
        }
    }
}