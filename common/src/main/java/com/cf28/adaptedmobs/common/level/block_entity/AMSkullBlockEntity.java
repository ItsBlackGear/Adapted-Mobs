package com.cf28.adaptedmobs.common.level.block_entity;

import com.cf28.adaptedmobs.common.registries.AMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AMSkullBlockEntity extends SkullBlockEntity {
    public AMSkullBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return AMBlockEntityTypes.SKULL.get();
    }
}