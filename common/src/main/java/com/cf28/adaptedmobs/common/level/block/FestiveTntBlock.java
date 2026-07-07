package com.cf28.adaptedmobs.common.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FestiveTntBlock extends Block {
    public FestiveTntBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
    }
}