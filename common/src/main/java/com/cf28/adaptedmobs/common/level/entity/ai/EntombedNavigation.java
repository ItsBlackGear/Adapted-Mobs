package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.integrations.LambDynLightsCompat;
import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;

public class EntombedNavigation extends GroundPathNavigation {
    private final Entombed entombed;

    public EntombedNavigation(final Entombed entombed, final Level level) {
        super(entombed, level);
        this.entombed = entombed;
    }

    @Override
    protected void trimPath() {
        super.trimPath();

        final LivingEntity target = this.entombed.getTarget();
        final BlockPos blockPos = BlockPos.containing(this.mob.getX(), this.mob.getY() + (double) 0.5F, this.mob.getZ());

        // trim the path if we're not in a lit spot
        if (this.entombed.isPosLit(blockPos)) {
            return;
        }

        if (LambDynLightsCompat.isLoaded()) {
            if ((target != null && this.entombed.isPositionInTargetLight(blockPos, target)) || this.entombed.isPositionInPlayerLight(blockPos)) {
                return;
            }
        }

        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < this.path.getNodeCount(); ++i) {
            final Node node = this.path.getNode(i);

            if (this.entombed.isPosLit(mutableBlockPos.set(node.x, node.y, node.z))) {
                this.path.truncateNodes(i);
                return;
            }

            if (LambDynLightsCompat.isLoaded()) {
                if ((target != null && this.entombed.isPositionInTargetLight(mutableBlockPos, target)) || this.entombed.isPositionInPlayerLight(mutableBlockPos)) {
                    this.path.truncateNodes(i);
                    return;
                }
            }
        }
    }

    @Override
    protected PathFinder createPathFinder(final int maxVisitedNodes) {
        this.nodeEvaluator = new EntombedNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }
}
