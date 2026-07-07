package com.cf28.adaptedmobs.common.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RocketCreeperSkullBlock extends AMSkullBlock {
    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
    
    public RocketCreeperSkullBlock(Type type, Properties properties) {
        super(type, properties);
    }
    
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}