package com.cf28.adaptedmobs.common.blockentity;

import com.cf28.adaptedmobs.common.registry.AMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlatformSkullBlockEntity extends SkullBlockEntity {
    public PlatformSkullBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return AMBlockEntities.SKULL.get();
    }
}
