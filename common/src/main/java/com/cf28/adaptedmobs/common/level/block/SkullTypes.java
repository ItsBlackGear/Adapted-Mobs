package com.cf28.adaptedmobs.common.level.block;

import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;

public enum SkullTypes implements SkullBlock.Type {
    FESTIVE_CREEPER("festive_creeper"),
    SUPPORT_CREEPER("support_creeper"),
    ROCKET_CREEPER("rocket_creeper"),
    PEEPER_CREEPER("peeper_creeper");
    
    private final String name;
    
    SkullTypes(String name) {
        this.name = name;
    }
    
    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}