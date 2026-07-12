package com.cf28.adaptedmobs.client.level.model;

import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.core.Direction;

import java.util.EnumSet;

public final class ModelUtil {
    private static final float FRINGE_OFFSET = 0.005F;

    public static CubeListBuilder addFringeBox(CubeListBuilder builder, float x, float y, float z, float sizeX, float sizeY, float sizeZ, boolean mirror) {
        if (sizeX == 0.0F) {
            builder.mirror(mirror).addBox(x - FRINGE_OFFSET, y, z, sizeX, sizeY, sizeZ, EnumSet.of(Direction.EAST));
            builder.mirror(mirror).addBox(x + FRINGE_OFFSET, y, z, sizeX, sizeY, sizeZ, EnumSet.of(Direction.WEST));
        } else if (sizeY == 0.0F) {
            builder.mirror(mirror).addBox(x, y - FRINGE_OFFSET, z, sizeX, sizeY, sizeZ, EnumSet.of(Direction.UP));
            builder.mirror(mirror).addBox(x, y + FRINGE_OFFSET, z, sizeX, sizeY, sizeZ, EnumSet.of(Direction.DOWN));
        } else {
            builder.mirror(mirror).addBox(x, y, z - FRINGE_OFFSET, sizeX, sizeY, sizeZ, EnumSet.of(Direction.NORTH));
            builder.mirror(mirror).addBox(x, y, z + FRINGE_OFFSET, sizeX, sizeY, sizeZ, EnumSet.of(Direction.SOUTH));
        }
        return builder;
    }
}
