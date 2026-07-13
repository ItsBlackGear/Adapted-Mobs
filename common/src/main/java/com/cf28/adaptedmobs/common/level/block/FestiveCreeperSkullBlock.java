package com.cf28.adaptedmobs.common.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FestiveCreeperSkullBlock extends AMSkullBlock {
    protected static final VoxelShape SHAPE = Block.box(3.0, 0.0, 4.0, 13.0, 8.0, 12.0);
    protected static final VoxelShape SHAPE_SIDEWAYS = Block.box(4.0, 0.0, 3.0, 12.0, 8.0, 13.0);

    public FestiveCreeperSkullBlock(Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int rotation = state.getValue(SkullBlock.ROTATION);
        return ((rotation + 2) / 4) % 2 == 1 ? SHAPE_SIDEWAYS : SHAPE;
    }
}
