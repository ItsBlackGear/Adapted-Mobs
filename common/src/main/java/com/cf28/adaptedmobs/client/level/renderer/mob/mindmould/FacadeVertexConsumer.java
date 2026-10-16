package com.cf28.adaptedmobs.client.level.renderer.mob.mindmould;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public final class FacadeVertexConsumer implements VertexConsumer {

    private final Function<TextureAtlasSprite, VertexConsumer> consumerFactory;
    private final Map<Direction, ModelPart.Vertex[]> vesselVertices = new EnumMap<>(Direction.class);
    private final Map<Direction, Vector3d[]> shadedFaces = new EnumMap<>(Direction.class);
    private final Map<Direction, Vector3d[]> unshadedFaces = new EnumMap<>(Direction.class);
    private final ModelBlockRenderer.AmbientOcclusionFace vanillaAo;

    private VertexConsumer vertexConsumer;
    private final BlockAndTintGetter level;
    private final BlockState blockState;

    private final Vec3 cameraPosition;

    private final BitSet faceFlags = new BitSet();
    private final Vector3f worldSize = new Vector3f();

    private final float[] brightness = new float[4];
    private final int[] rotatedLights = new int[4];
    private int vertices;

    private ModelPart.Vertex[] currentVesselVertices;

    public FacadeVertexConsumer(
        Function<TextureAtlasSprite, VertexConsumer> consumerFactory,
        ModelPart.Cube vessel, BlockAndTintGetter level, BlockState blockState,
        boolean ambientOcclusion, Vec3 cameraPosition
    ) {
        this.consumerFactory = consumerFactory;
        this.level = level;

        this.blockState = blockState;
        this.vanillaAo = ambientOcclusion ? new ModelBlockRenderer.AmbientOcclusionFace() : null;

        this.cameraPosition = cameraPosition;
        for (ModelPart.Polygon polygon : vessel.polygons) {
            Direction direction = Direction.getNearest(
                -polygon.normal.x(), -polygon.normal.y(), polygon.normal.z()
            );
            ModelPart.Vertex[] corners = new ModelPart.Vertex[4];
            for (int corner = 0; corner < corners.length; corner++)
                corners[corner] = getVesselVertex(vessel, polygon, direction, corner);

            this.vesselVertices.put(direction, corners);
        }
    }

    @Override
    public void putBulkData(
        @NotNull PoseStack.Pose pose, BakedQuad quad, float @NotNull [] brightness,
        float red, float green, float blue, float alpha, int @NotNull [] lights,
        int packedOverlay, boolean readExistingColor
    ) {
        Direction facadeFace = quad.getDirection();
        this.currentVesselVertices = vesselVertices.get(facadeFace);
        if (this.currentVesselVertices == null) return;

        this.vertexConsumer = this.consumerFactory.apply(quad.getSprite());
        Vector3f normal = facadeFace.step();
        pose.transformNormal(normal, normal);

        Matrix4f transform = pose.pose();
        Vector3f center = transform.transformPosition(0.5F, 0.5F, 0.5F, new Vector3f());
        Vector3f minimum = new Vector3f();

        transform.transformAab(0, 0, 0, 1, 1, 1, minimum, worldSize);
        worldSize.sub(minimum);

        Vec3 facadeOrigin = cameraPosition.add(center.x - 0.5, minimum.y, center.z - 0.5);

        FaceInfo face = FaceInfo.fromFacing(facadeFace);
        Vector3f vertex = new Vector3f();
        Vector3d lighting = new Vector3d();
        for (int i = 0; i < this.brightness.length; i++) {
            FaceInfo.VertexInfo corner = face.getVertexInfo(i);
            transform.transformPosition(
                getCoordinate(corner.xFace),
                getCoordinate(corner.yFace),
                getCoordinate(corner.zFace), vertex
            );

            vertex.set(
                Mth.clamp(0.5F + vertex.x - center.x, 0.0F, 1.0F),
                Mth.clamp(vertex.y - minimum.y, 0.0F, 1.0F),
                Mth.clamp(0.5F + vertex.z - center.z, 0.0F, 1.0F)
            );

            this.interpolateLighting(facadeOrigin, vertex, normal, quad.isShade(), lighting);
            this.brightness[i] = (float) lighting.x;
            rotatedLights[i] = (int) Math.round(lighting.y) | ((int) Math.round(lighting.z) << 16);
        }
        this.vertices = 0;

        VertexConsumer.super.putBulkData(
            pose, quad, this.brightness,
            red, green, blue, alpha,
            rotatedLights, packedOverlay,
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

        ModelPart.Vertex vesselVertex = currentVesselVertices[vertex];
        vertexConsumer.addVertex(
            x, y, z, FastColor.ARGB32.color(
                FastColor.as8BitChannel(brightness[vertex]), color
            ), u, v, packedOverlay, packedLight,
            (vesselVertex.u * 2F) - 1F,
            (vesselVertex.v * 2F) - 1F,
            0.0F
        );
    }

    private Vector3d[] interpolateFaceLighting(Vec3 origin, Direction direction, boolean shade) {
        boolean boundaryFace = direction.getAxis().choose(worldSize.x, worldSize.y, worldSize.z) >= 1.0F - Mth.EPSILON;
        faceFlags.set(0, boundaryFace);

        BlockPos lowerOrigin = BlockPos.containing(origin);
        Vec3 fraction = origin.subtract(lowerOrigin.getX(), lowerOrigin.getY(), lowerOrigin.getZ());

        Vector3d[] lighting = new Vector3d[vanillaAo == null ? 1 : 4];
        for (int corner = 0; corner < lighting.length; corner++)
            lighting[corner] = new Vector3d();

        Vector3d sample = new Vector3d();
        float flatBrightness = vanillaAo == null ? level.getShade(direction, shade) : 0.0F;
        for (BlockPos position : BlockPos.betweenClosed(lowerOrigin, lowerOrigin.offset(1, 1, 1))) {
            double weight = Mth.lerp(position.getX() - lowerOrigin.getX(), 1.0 - fraction.x, fraction.x)
                * Mth.lerp(position.getY() - lowerOrigin.getY(), 1.0 - fraction.y, fraction.y)
                * Mth.lerp(position.getZ() - lowerOrigin.getZ(), 1.0 - fraction.z, fraction.z);
            if (weight == 0.0) continue;

            int flatLight = 0;
            if (vanillaAo != null) {
                vanillaAo.calculate(level, blockState,
                    position, direction, null,
                    faceFlags, shade
                );
            }else {
                BlockPos lightPosition = boundaryFace ? position.relative(direction) : position;
                flatLight = LevelRenderer.getLightColor(level, blockState, lightPosition);
            }

            for (int corner = 0; corner < lighting.length; corner++) {
                float brightness = vanillaAo != null ? vanillaAo.brightness[corner] : flatBrightness;
                int packedLight = vanillaAo != null ? vanillaAo.lightmap[corner] : flatLight;
                int blockLight = packedLight & 0xFFFF;
                int skyLight = (packedLight >>> 16) & 0xFFFF;

                lighting[corner].add(sample.set(brightness, blockLight, skyLight).mul(weight));
            }
        }
        return lighting;
    }

    private void interpolateLighting(Vec3 origin, Vector3f coordinates, Vector3f normal, boolean shade, Vector3d lighting) {
        lighting.zero();
        Map<Direction, Vector3d[]> cache = shade ? shadedFaces : unshadedFaces;
        for (Direction.Axis axis : Direction.Axis.VALUES) {
            float normalComponent = (float) axis.choose(normal.x, normal.y, normal.z);
            if (normalComponent == 0.0F) continue;
            Direction direction = Direction.fromAxisAndDirection(
                axis, (normalComponent < 0.0F) ? Direction.AxisDirection.NEGATIVE
                    : Direction.AxisDirection.POSITIVE
            );

            Vector3d[] face = cache.computeIfAbsent(direction, faceDirection -> interpolateFaceLighting(origin, faceDirection, shade));
            double weight = Mth.square(normalComponent);
            if (vanillaAo == null) {
                lighting.add(face[0].x * weight, face[0].y * weight, face[0].z * weight);
                continue;
            }

            FaceInfo.VertexInfo diagonal = FaceInfo.fromFacing(direction).getVertexInfo(2);
            double horizontal = axis == Direction.Axis.X ? coordinates.z : coordinates.x;
            double vertical = axis == Direction.Axis.Y ? coordinates.z : coordinates.y;
            horizontal = Mth.lerp(getCoordinate(axis == Direction.Axis.X ? diagonal.zFace : diagonal.xFace), 1.0 - horizontal, horizontal);
            vertical = Mth.lerp(getCoordinate(axis == Direction.Axis.Y ? diagonal.zFace : diagonal.yFace), 1.0 - vertical, vertical);

            lighting.add(
                Mth.lerp2(horizontal, vertical, face[0].x, face[3].x, face[1].x, face[2].x) * weight,
                Mth.lerp2(horizontal, vertical, face[0].y, face[3].y, face[1].y, face[2].y) * weight,
                Mth.lerp2(horizontal, vertical, face[0].z, face[3].z, face[1].z, face[2].z) * weight
            );
        }
    }

    private static ModelPart.Vertex getVesselVertex(ModelPart.Cube vessel, ModelPart.Polygon face, Direction direction, int corner) {
        FaceInfo.VertexInfo info = FaceInfo.fromFacing(direction).getVertexInfo(corner);
        float targetX = Mth.lerp(1.0F - getCoordinate(info.xFace), vessel.minX, vessel.maxX);
        float targetY = Mth.lerp(1.0F - getCoordinate(info.yFace), vessel.minY, vessel.maxY);
        float targetZ = Mth.lerp(getCoordinate(info.zFace), vessel.minZ, vessel.maxZ);

        return Collections.min(Arrays.asList(face.vertices), Comparator.comparingDouble(
            candidate -> candidate.pos.distanceSquared(targetX, targetY, targetZ)
        ));
    }

    public static float getCoordinate(int face) {
        return Mth.clamp(Direction.from3DDataValue(face).getAxisDirection().getStep(), 0, 1);
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
