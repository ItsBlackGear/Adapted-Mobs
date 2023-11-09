package com.cf28.adaptedmobs.core.mixin;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow @Final private @Nullable Entity source;
    @Shadow @Final private Level level;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;
    @Shadow @Final private ExplosionDamageCalculator damageCalculator;
    @Shadow @Final private ObjectArrayList<BlockPos> toBlow;
    @Shadow @Final private float radius;
    @Shadow @Final private Map<Player, Vec3> hitPlayers;

    @Shadow public abstract DamageSource getDamageSource();

    @Shadow @javax.annotation.Nullable public abstract LivingEntity getSourceMob();

    /**
     * returns the message "[target] was blown up by [attacker]" when using Festive TNT
     */
    @Inject(method = "getSourceMob", at = @At("TAIL"), cancellable = true)
    private void am$getSource(CallbackInfoReturnable<LivingEntity> cir) {
        if (this.source instanceof PrimedFestiveTnt tnt) {
            cir.setReturnValue(tnt.getOwner());
        }
    }

    /**
     * - mimics the behavior of TNT explosion without the need of networking usage or new classes
     * - prevents collateral damage from allied entities such as owner or other pets
     */
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void am$explode(CallbackInfo ci) {
        if (this.source instanceof PrimedFestiveTnt || this.source instanceof TamableCreeper) {
            this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
            this.am$causeExplosion();
            this.am$causeDamage(this.source);

            ci.cancel();
        }
    }

    @Unique
    private void am$causeDamage(Entity source) {
        float explosionRadius = this.radius * 2.0F;
        int minX = Mth.floor(this.x - (double)explosionRadius - 1.0);
        int maxX = Mth.floor(this.x + (double)explosionRadius + 1.0);
        int minY = Mth.floor(this.y - (double)explosionRadius - 1.0);
        int maxY = Mth.floor(this.y + (double)explosionRadius + 1.0);
        int minZ = Mth.floor(this.z - (double)explosionRadius - 1.0);
        int maxZ = Mth.floor(this.z + (double)explosionRadius + 1.0);
        List<Entity> affectedEntities = this.level.getEntities(this.source, new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        Vec3 explosionCenter = new Vec3(this.x, this.y, this.z);

        for (Entity entity : affectedEntities) {
            if (!entity.ignoreExplosion()) {
                double distancePercent = Math.sqrt(entity.distanceToSqr(explosionCenter)) / (double) explosionRadius;
                if (distancePercent <= 1.0) {
                    double xOffset = entity.getX() - this.x;
                    double yOffset = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double zOffset = entity.getZ() - this.z;
                    double distanceBetween = Math.sqrt(xOffset * xOffset + yOffset * yOffset + zOffset * zOffset);
                    if (distanceBetween != 0.0) {
                        xOffset /= distanceBetween;
                        yOffset /= distanceBetween;
                        zOffset /= distanceBetween;
                        double visibilityPercent = Explosion.getSeenPercent(explosionCenter, entity);
                        double damagePercent = (1.0 - distancePercent) * visibilityPercent;

                        if (source instanceof PrimedFestiveTnt tnt) {
                            if (tnt.getOwner() instanceof FestiveCreeper creeper) {
                                am$applyDamage(explosionRadius, entity, xOffset, yOffset, zOffset, damagePercent, creeper);
                            }
                        } else if (source instanceof TamableCreeper creeper) {
                            am$applyDamage(explosionRadius, entity, xOffset, yOffset, zOffset, damagePercent, creeper);
                        }
                    }
                }
            }
        }
    }

    @Unique
    private void am$applyDamage(float explosionRadius, Entity target, double xOffset, double yOffset, double zOffset, double damagePercent, TamableCreeper self) {
        // checks if the owner is not null
        if (self.getOwnerUUID() != null && target instanceof LivingEntity living) {
            LivingEntity owner = this.level.getPlayerByUUID(self.getOwnerUUID());
            // will check if the entity is not owned by the target, and if it wants to attack any other mobs
            if (!self.isOwnedBy(living) && self.wantsToAttack(living, owner)) {
                this.am$applyDamageAndKnockback(explosionRadius, target, xOffset, yOffset, zOffset, damagePercent);
            }
        } else {
            // checks if the target is not itself
            if (target != self) {
                this.am$applyDamageAndKnockback(explosionRadius, target, xOffset, yOffset, zOffset, damagePercent);
            }
        }
    }

    @Unique
    private void am$causeExplosion() {
        Set<BlockPos> affectedBlocks = Sets.newHashSet();

        for(int xIndex = 0; xIndex < 16; ++xIndex) {
            for(int yIndex = 0; yIndex < 16; ++yIndex) {
                for(int zIndex = 0; zIndex < 16; ++zIndex) {
                    if (xIndex == 0 || xIndex == 15 || yIndex == 0 || yIndex == 15 || zIndex == 0 || zIndex == 15) {
                        double xNormalized = (float)xIndex / 15.0F * 2.0F - 1.0F;
                        double yNormalized = (float)yIndex / 15.0F * 2.0F - 1.0F;
                        double zNormalized = (float)zIndex / 15.0F * 2.0F - 1.0F;
                        double distance = Math.sqrt(xNormalized * xNormalized + yNormalized * yNormalized + zNormalized * zNormalized);
                        xNormalized /= distance;
                        yNormalized /= distance;
                        zNormalized /= distance;
                        float explosionPower = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double x = this.x;
                        double y = this.y;
                        double z = this.z;

                        for(; explosionPower > 0.0F; explosionPower -= 0.22500001F) {
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockState state = this.level.getBlockState(pos);
                            FluidState fluid = this.level.getFluidState(pos);
                            if (!this.level.isInWorldBounds(pos)) {
                                break;
                            }

                            Optional<Float> resistance = this.damageCalculator.getBlockExplosionResistance((Explosion)(Object)this, this.level, pos, state, fluid);
                            if (resistance.isPresent()) {
                                explosionPower -= (resistance.get() + 0.3F) * 0.3F;
                            }

                            if (explosionPower > 0.0F && this.damageCalculator.shouldBlockExplode((Explosion)(Object)this, this.level, pos, state, explosionPower)) {
                                affectedBlocks.add(pos);
                            }

                            x += xNormalized * 0.3F;
                            y += yNormalized * 0.3F;
                            z += zNormalized * 0.3F;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll(affectedBlocks);
    }

    /**
     * damages the entity upon explosion and applies the calculated knockback
     */
    @Unique
    private void am$applyDamageAndKnockback(double explosionRadius, Entity entity, double x, double y, double z, double damagePercent) {
        entity.hurt(this.getDamageSource(), (float)((int)((damagePercent * damagePercent + damagePercent) / 2.0 * 7.0 * explosionRadius + 1.0)));

        double knockbackPercent = damagePercent;
        if (entity instanceof LivingEntity living) {
            knockbackPercent = ProtectionEnchantment.getExplosionKnockbackAfterDampener(living, damagePercent);
        }

        entity.setDeltaMovement(entity.getDeltaMovement().add(x * knockbackPercent, y * knockbackPercent, z * knockbackPercent));
        if (entity instanceof Player player && !player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
            this.hitPlayers.put(player, new Vec3(x * damagePercent, y * damagePercent, z * damagePercent));
        }
    }
}