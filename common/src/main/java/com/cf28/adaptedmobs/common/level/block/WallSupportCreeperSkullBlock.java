package com.cf28.adaptedmobs.common.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WallSupportCreeperSkullBlock extends AMWallSkullBlock {
    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH,
                    Block.box(3.5, 3.5, 7.5, 12.5, 12.5, 16.5),
                    Direction.SOUTH,
                    Block.box(3.5, 3.5, -0.5, 12.5, 12.5, 8.5),
                    Direction.EAST,
                    Block.box(-0.5, 3.5, 3.5, 8.5, 12.5, 12.5),
                    Direction.WEST,
                    Block.box(7.5, 3.5, 3.5, 16.5, 12.5, 12.5)
            )
    );

    public WallSupportCreeperSkullBlock(SkullBlock.Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }
}
