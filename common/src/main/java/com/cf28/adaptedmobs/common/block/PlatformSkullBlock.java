package com.cf28.adaptedmobs.common.block;

import com.cf28.adaptedmobs.common.blockentity.PlatformSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlatformSkullBlock extends SkullBlock {
    public PlatformSkullBlock(Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlatformSkullBlockEntity(pos, state);
    }
}