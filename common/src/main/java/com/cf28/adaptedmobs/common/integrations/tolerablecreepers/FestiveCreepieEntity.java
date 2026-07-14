package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FestiveCreepieEntity extends Creepie {
    private static final int TRAIL_START_DELAY = 6;
    private static final int WATER_EXPLODE_DELAY = 60;

    private boolean hasLanded;
    private int waterTicks;

    public FestiveCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.maxSwell = 40;
        this.lookControl = new LookControl(this) {
            @Override
            public void tick() {
            }
        };
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canFight() {
        return false;
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public int getMaxHeadXRot() {
        return 0;
    }

    @Override
    public int getMaxHeadYRot() {
        return 0;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        this.updateInWaterStateAndDoFluidPushing();
        if (onGround) {
            this.resetFallDistance();
        }
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.hasLanded) {
            if (!this.isControlledByLocalInstance()) {
                super.travel(travelVector);
            }
            return;
        }

        if (this.isControlledByLocalInstance()) {
            Vec3 delta = this.getDeltaMovement();
            this.move(MoverType.SELF, delta);
            if (this.isInWater()) {
                Vec3 scaled = delta.scale(0.6);
                double submersion = this.getBbHeight() > 0.0F
                        ? Mth.clamp(this.getFluidHeight(FluidTags.WATER) / this.getBbHeight(), 0.0, 1.0)
                        : 0.0;
                double newY = Mth.clamp(scaled.y * 0.5 + submersion * 0.06, -0.1, 0.08);
                this.setDeltaMovement(scaled.x, newY, scaled.z);
            } else {
                double newY = delta.y - 0.08;
                this.setDeltaMovement(delta.x * 0.98, newY * 0.98, delta.z * 0.98);
            }
            this.calculateEntityAnimation(false);
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ItemTags.CREEPER_IGNITERS)) {
            return InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround()) {
            Vec3 delta = this.getDeltaMovement();
            double horizontalDistance = delta.horizontalDistance();
            if (horizontalDistance > 1.0E-4 || Math.abs(delta.y) > 1.0E-4) {
                this.xRotO = this.getXRot();
                this.setXRot((float) (Mth.atan2(delta.y, horizontalDistance) * (180.0 / Math.PI)));
            }
        } else if (this.getXRot() != 0.0F) {
            this.xRotO = this.getXRot();
            this.setXRot(0.0F);
        }

        if (this.level().isClientSide()) {
            if (this.tickCount >= TRAIL_START_DELAY && !this.onGround()) {
                this.level().addParticle(AMParticles.FESTIVE_TNT_PARTICLETRAIL.get(), this.getX(), this.getY() + 0.25, this.getZ(), 0.0, 0.0, 0.0);
            }
        } else if (this.isAlive()) {
            if (!this.hasLanded && this.onGround()) {
                this.hasLanded = true;
                this.setDeltaMovement(Vec3.ZERO);
                this.setSwellDir(1);
            }

            if (this.isInWater()) {
                if (++this.waterTicks >= WATER_EXPLODE_DELAY) {
                    this.explodeCustom();
                }
            } else {
                this.waterTicks = 0;
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return true;
        }
        return super.isInvulnerableTo(source);
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
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.dead = true;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
        ServerLevel sl = (ServerLevel) this.level();
        TolerableCreepersIntegration.spawnParticleRing(sl, AMParticles.FESTIVE_SPORES.get(), this.position().add(0.0, 0.1, 0.0), 0.8, 20);
        this.discard();
    }
}
