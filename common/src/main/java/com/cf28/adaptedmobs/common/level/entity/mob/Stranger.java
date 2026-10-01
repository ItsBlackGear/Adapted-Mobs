package com.cf28.adaptedmobs.common.level.entity.mob;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Only ever exists on the client of the player it appears to
 */
public class Stranger extends Mob {
    public static final int CLIENT_ID = -1_048_576;

    private static final double SEEN_THRESHOLD = Math.cos(Math.toRadians(30.0));
    private static final double LOOKED_AWAY_THRESHOLD = Math.cos(Math.toRadians(80.0));
    private static final double LOOKING_BACK_THRESHOLD = Math.cos(Math.toRadians(70.0));

    private static final double MIN_DISTANCE = 40.0;
    private static final double MAX_DISTANCE = 128.0;
    private static final int LIFETIME = 3600;

    private boolean seen;
    private boolean lookedAway;
    private boolean offScreen = true;
    private boolean vanished;

    public Stranger(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.setId(CLIENT_ID);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes();
    }

    public boolean updateGaze(Vec3 eyePosition, Vec3 lookDirection, boolean hasLineOfSight) {
        if (this.vanished) return true;

        Vec3 toThis = this.getBoundingBox().getCenter().subtract(eyePosition).normalize();
        double dot = lookDirection.normalize().dot(toThis);
        this.offScreen = dot < LOOKED_AWAY_THRESHOLD;

        if (!this.seen) {
            this.seen = dot > SEEN_THRESHOLD && hasLineOfSight;
        } else if (!this.lookedAway) {
            this.lookedAway = this.offScreen;
        } else if (dot > LOOKING_BACK_THRESHOLD) {
            this.vanished = true;
        }

        return this.vanished;
    }

    public void tickFor(Player player) {
        double distanceSqr = this.distanceToSqr(player);
        boolean vanish = this.vanished
                || !player.isAlive()
                || player.isSpectator()
                || distanceSqr < MIN_DISTANCE * MIN_DISTANCE
                || distanceSqr > MAX_DISTANCE * MAX_DISTANCE
                || (this.tickCount > LIFETIME && this.offScreen);

        if (vanish) {
            this.discard();
            return;
        }

        this.lookAt(EntityAnchorArgument.Anchor.EYES, player.getEyePosition());
        this.setYHeadRot(this.getYRot());
        this.setYBodyRot(this.getYRot());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < MAX_DISTANCE * MAX_DISTANCE;
    }

    @Override
    protected @NotNull Component getTypeName() {
        return EntityType.PLAYER.getDescription();
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }
}
