package com.cf28.adaptedmobs.common.level.block;

import com.cf28.adaptedmobs.common.level.block_entity.AMSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AMWallSkullBlock extends WallSkullBlock {
    public AMWallSkullBlock(SkullBlock.Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AMSkullBlockEntity(pos, state);
    }
}