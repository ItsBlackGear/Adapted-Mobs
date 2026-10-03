package com.cf28.adaptedmobs.client.level.renderer.mob.mindmould;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public final class FacadeVertexConsumer implements VertexConsumer {
    private final Function<TextureAtlasSprite, VertexConsumer> consumerFactory;
    private final Map<Direction, ModelPart.Polygon> vesselFaces = new EnumMap<>(Direction.class);

    private VertexConsumer vertexConsumer;
    private final ModelPart.Cube vessel;

    private float[] brightness;
    private int vertices;

    private Direction facadeFace;
    private ModelPart.Polygon vesselFace;

    public FacadeVertexConsumer(Function<TextureAtlasSprite, VertexConsumer> consumerFactory, ModelPart.Cube vessel) {
        this.vessel = vessel;
        this.consumerFactory = consumerFactory;
        for (ModelPart.Polygon polygon : vessel.polygons) {
            Direction direction = Direction.getNearest(
                polygon.normal.x(), polygon.normal.y(), polygon.normal.z()
            );
            this.vesselFaces.put(direction, polygon);
        }
    }

    @Override
    public void putBulkData(
        @NotNull PoseStack.Pose pose, BakedQuad quad, float @NotNull [] brightness,
        float red, float green, float blue, float alpha, int @NotNull [] lights,
        int packedOverlay, boolean readExistingColor
    ) {
        this.facadeFace = quad.getDirection();
        this.vesselFace = this.vesselFaces.get(Direction.getNearest(
            -facadeFace.getStepX(), -facadeFace.getStepY(), facadeFace.getStepZ()
        ));
        if (this.vesselFace == null) return;

        this.vertexConsumer = this.consumerFactory.apply(quad.getSprite());
        this.brightness = brightness;
        this.vertices = 0;

        VertexConsumer.super.putBulkData(
            pose, quad, brightness,
            red, green, blue, alpha,
            lights, packedOverlay,
            readExistingColor
        );
    }

    @Override
    public void addVertex(
        float x, float y, float z, int color,
        float u, float v, int packedOverlay,
        int packedLight, float normalX,
        float normalY, float normalZ
    ) {
        int vertex = vertices;
        vertices++;

        ModelPart.Vertex vesselVertex = this.getVesselVertex(facadeFace, vertex);
        vertexConsumer.addVertex(
            x, y, z, FastColor.ARGB32.color(
                FastColor.as8BitChannel(brightness[vertex]), color
            ), u, v, packedOverlay, packedLight,
            (vesselVertex.u * 2F) - 1F,
            (vesselVertex.v * 2F) - 1F,
            0.0F
        );
    }

    private ModelPart.Vertex getVesselVertex(Direction direction, int corner) {
        FaceInfo.VertexInfo info = FaceInfo.fromFacing(direction).getVertexInfo(corner);
        float targetX = Mth.lerp(1.0F - getCoordinate(info.xFace), vessel.minX, vessel.maxX);
        float targetY = Mth.lerp(1.0F - getCoordinate(info.yFace), vessel.minY, vessel.maxY);
        float targetZ = Mth.lerp(getCoordinate(info.zFace), vessel.minZ, vessel.maxZ);
        ModelPart.Vertex nearest = vesselFace.vertices[0];
        float nearestDistance = Float.POSITIVE_INFINITY;
        for (ModelPart.Vertex candidate : vesselFace.vertices) {
            float distance = Mth.lengthSquared(
                candidate.pos.x() - targetX,
                candidate.pos.y() - targetY,
                candidate.pos.z() - targetZ
            );
            if (distance < nearestDistance) {
                nearest = candidate;
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    public static float getCoordinate(int face) {
        return Direction.from3DDataValue(face).getAxisDirection()
            == Direction.AxisDirection.POSITIVE ? 1.0F : 0.0F;
    }

    @Override public @NotNull VertexConsumer addVertex(float x, float y, float z) {
        vertexConsumer.addVertex(x, y, z);
        return this;
    }

    @Override public @NotNull VertexConsumer setColor(int red, int green, int blue, int alpha) {
        vertexConsumer.setColor(red, green, blue, alpha);
        return this;
    }

    @Override public @NotNull VertexConsumer setUv(float u, float v) {
        vertexConsumer.setUv(u, v);
        return this;
    }

    @Override public @NotNull VertexConsumer setUv1(int u, int v) {
        vertexConsumer.setUv1(u, v);
        return this;
    }

    @Override public @NotNull VertexConsumer setUv2(int u, int v) {
        vertexConsumer.setUv2(u, v);
        return this;
    }

    @Override public @NotNull VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
        vertexConsumer.setNormal(normalX, normalY, normalZ);
        return this;
    }
}
