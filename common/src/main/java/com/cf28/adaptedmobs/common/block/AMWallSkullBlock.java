package com.cf28.adaptedmobs.common.block;

import com.cf28.adaptedmobs.common.blockentities.AMSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AMWallSkullBlock extends WallSkullBlock {
    public AMWallSkullBlock(SkullBlock.Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AMSkullBlockEntity(pos, state);
    }
}