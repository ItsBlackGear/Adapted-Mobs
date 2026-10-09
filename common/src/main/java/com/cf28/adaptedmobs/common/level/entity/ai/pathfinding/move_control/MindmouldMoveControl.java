package com.cf28.adaptedmobs.common.level.entity.ai.pathfinding.move_control;

import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;

public class MindmouldMoveControl extends MoveControl {

    private float yRotTo;
    private int jumpDelay;
    private final Mindmould mindmould;

    public MindmouldMoveControl(Mindmould mindmould) {
        super(mindmould);
        this.mindmould = mindmould;
        this.yRotTo = mindmould.yRotO;
    }

    @Override public void setWantedPosition(double x, double y, double z, double speed) {
        super.setWantedPosition(x, y, z, speed);
        Vec3 relativePosition = this.mindmould.position().vectorTo(new Vec3(x, y, z));
        this.yRotTo = (float) (Mth.atan2(relativePosition.z, relativePosition.x) * 180.0F / (float) Math.PI) - 90.0F;
    }

    @Override
    public void tick() {
//        this.mob.setYRot();
//        AdaptedMobs.LOGGER.info("{}, ", this.yRot);
        this.mindmould.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresentOrElse(
            target -> this.mindmould.lookAt(target, 10.0F, 10.0F),
            () -> this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRotTo, 10.0F))
        );

        this.mob.yHeadRot = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();
//        if (this.operation != MoveControl.Operation.MOVE_TO)
//            this.mob.setZza(0.0F);
//        else {
//            this.operation = MoveControl.Operation.WAIT;
        this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

        if (this.mob.onGround()) {
            if (this.jumpDelay-- <= 0) {
                this.jumpDelay = this.mindmould.getJumpDelay() / 3;
//                if (this.isAggressive) {
//                    this.jumpDelay /= 3;
//                }

                this.mindmould.getJumpControl().jump();
                this.mindmould.playSound(
                    SoundEvents.SLIME_JUMP,
                    this.mindmould.getSoundVolume(),
                    this.mindmould.isBaby() ? 1.5f : 1.0f
                );
            } else {
                this.mindmould.xxa = 0.0F;
                this.mindmould.zza = 0.0F;
                this.mob.setSpeed(0.0F);
            }
        }
//        }
    }
}
