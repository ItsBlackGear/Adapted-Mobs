package com.cf28.adaptedmobs.common.blockentities;

import com.cf28.adaptedmobs.common.registry.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AMSkullBlockEntity extends SkullBlockEntity {
    public AMSkullBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return AMBlockEntityTypes.SKULL.get();
    }
}