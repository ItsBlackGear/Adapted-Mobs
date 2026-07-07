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

public class WallRocketCreeperSkullBlock extends AMWallSkullBlock {
    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(
        ImmutableMap.of(
            Direction.NORTH,
            Block.box(4.0, 4.0, 8.0, 12.0, 14.0, 16.0),
            Direction.SOUTH,
            Block.box(4.0, 4.0, 0.0, 12.0, 14.0, 8.0),
            Direction.EAST,
            Block.box(0.0, 4.0, 4.0, 8.0, 14.0, 12.0),
            Direction.WEST,
            Block.box(8.0, 4.0, 4.0, 16.0, 14.0, 12.0)
        )
    );

    public WallRocketCreeperSkullBlock(SkullBlock.Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }
}