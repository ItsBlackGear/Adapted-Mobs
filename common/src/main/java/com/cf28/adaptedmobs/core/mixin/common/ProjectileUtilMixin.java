package com.cf28.adaptedmobs.core.mixin.common;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
    @Inject(
        method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void adaptedmobs$allowHittingHarpyVehicle(
        Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, CallbackInfoReturnable<EntityHitResult> cir
    ) {
        if (cir.getReturnValue() == null && shooter != null && shooter.getVehicle() instanceof Harpy harpy) {
            if (filter.test(harpy)) {
                AABB aabb = harpy.getBoundingBox().inflate(harpy.getPickRadius());
                Optional<Vec3> clip = aabb.clip(startVec, endVec);
                if (clip.isPresent()) {
                    Vec3 hitVec = clip.get();
                    if (startVec.distanceToSqr(hitVec) <= distance) {
                        cir.setReturnValue(new EntityHitResult(harpy, hitVec));
                    }
                }
            }
        }
    }
}
