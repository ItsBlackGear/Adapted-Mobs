package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.integrations.LambDynLightsCompat;
import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Nullable;

public class EntombedNodeEvaluator extends WalkNodeEvaluator {
    @Override
    protected Node getStartNode(BlockPos pos) {
        Node node = super.getStartNode(pos);
        float malus = this.getLightMalus(node.x, node.y, node.z);
        if (malus > 0.0F) {
            node.costMalus = Math.max(node.costMalus, malus);
        }
        return node;
    }

    @Nullable
    @Override
    protected Node findAcceptedNode(int x, int y, int z, int verticalDeltaLimit, double nodeFloorLevel, Direction direction, PathType pathType) {
        Node node = super.findAcceptedNode(x, y, z, verticalDeltaLimit, nodeFloorLevel, direction, pathType);
        if (node != null) {
            float malus = this.getLightMalus(node.x, node.y, node.z);
            if (malus < 0.0F) {
                node.costMalus = -1.0F;
            } else if (malus > 0.0F) {
                node.costMalus = Math.max(node.costMalus, malus);
            }
        }
        return node;
    }

    private float getLightMalus(int x, int y, int z) {
        if (this.mob instanceof Entombed entombed) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockPos posAbove = pos.above();
            int light = Math.max(entombed.getLightLevelAt(pos), entombed.getLightLevelAt(posAbove));

            BlockPos mobPos = entombed.blockPosition();
            BlockPos mobEyePos = BlockPos.containing(entombed.getX(), entombed.getEyeY(), entombed.getZ());
            int mobCurrentLight = Math.max(entombed.getLightLevelAt(mobPos), entombed.getLightLevelAt(mobEyePos));

            boolean mobInBurningLight = mobCurrentLight >= Entombed.BURNING_LIGHT || entombed.isOnFire();

            if (light >= Entombed.BURNING_LIGHT) {
                if (!mobInBurningLight) {
                    return -1.0F;
                }
                return 16.0F;
            }

            if (light > Entombed.MAX_COMFORT_LIGHT) {
                if (mobCurrentLight <= Entombed.MAX_COMFORT_LIGHT) {
                    return 16.0F;
                }
                return 4.0F;
            }

            if (LambDynLightsCompat.isLoaded()) {
                LivingEntity target = entombed.getTarget();
                if (target != null && entombed.isPositionInTargetLight(pos, target)) {
                    return 8.0F;
                }
                if (entombed.isPositionInPlayerLight(pos)) {
                    return 8.0F;
                }
            }
        }
        return 0.0F;
    }
}
