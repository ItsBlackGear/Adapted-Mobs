package com.cf28.adaptedmobs.common.level.entity.ai.pathfinding.move_control;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BackUpMoveControl extends MoveControl {
    public BackUpMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            super.tick();
            Vec3 travelVec = this.mob.getDeltaMovement();
            this.checkAndApplyJumping(travelVec.x, travelVec.z);
            return;
        }

        if (this.operation == Operation.STRAFE) {
            float speed = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float maxSpeed = (float) this.speedModifier * speed;
            
            float fwd = this.strafeForwards;
            float right = this.strafeRight;
            float len = Mth.sqrt(fwd * fwd + right * right);
            if (len < 1.0F) len = 1.0F;

            len = maxSpeed / len;
            fwd *= len;
            right *= len;

            float sinY = Mth.sin(this.mob.getYRot() * Mth.DEG_TO_RAD);
            float cosY = Mth.cos(this.mob.getYRot() * Mth.DEG_TO_RAD);
            
            float relX = fwd * cosY - right * sinY;
            float relZ = right * cosY + fwd * sinY;

            this.mob.setSpeed(maxSpeed);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = Operation.WAIT;

            if (this.checkAndApplyJumping(relX, relZ)) {
                this.operation = Operation.JUMPING;
            }
        } else {
            super.tick();
        }
    }

    private boolean checkAndApplyJumping(double xMovement, double zMovement) {
        int dx = (int) Math.signum(xMovement);
        int dz = (int) Math.signum(zMovement);
        BlockPos origin = this.mob.blockPosition();

        if (this.shouldJumpOver(origin.offset(dx, 0, dz))
                || (dx != 0 && this.shouldJumpOver(origin.offset(dx, 0, 0)))
                || (dz != 0 && this.shouldJumpOver(origin.offset(0, 0, dz)))) {
            this.mob.getJumpControl().jump();
            return true;
        }
        
        return false;
    }

    private boolean shouldJumpOver(BlockPos pos) {
        BlockState state = this.mob.level().getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(this.mob.level(), pos);
        return !shape.isEmpty()
                && this.mob.getY() < shape.max(Direction.Axis.Y) + (double) pos.getY()
                && !state.is(BlockTags.DOORS)
                && !state.is(BlockTags.FENCES);
    }
}