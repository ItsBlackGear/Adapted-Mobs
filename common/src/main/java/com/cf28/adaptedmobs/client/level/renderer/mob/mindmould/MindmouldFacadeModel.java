package com.cf28.adaptedmobs.client.level.renderer.mob.mindmould;

import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.Stream;

public final class MindmouldFacadeModel {
    private static final FaceBakery FACE_BAKERY = new FaceBakery();
    private static final Vector3f CUBE_MIN = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f CUBE_MAX = new Vector3f(16.0F, 16.0F, 16.0F);

    private MindmouldFacadeModel() {}

    public static BakedModel getFacadeModel(BakedModel model, BlockState state, long seed, FacadeCorner corner) {
        RandomSource random = RandomSource.create(seed);
        List<BakedQuad> unculledQuads = model.getQuads(state, null, random);
        Map<Direction, List<BakedQuad>> facadeQuads = new EnumMap<>(Direction.class);

        for (Direction direction : Direction.values()) {
            random.setSeed(seed);
            Iterator<BakedQuad> sourceQuads = Stream.concat(
                model.getQuads(state, direction, random).stream(),
                unculledQuads.stream().filter(quad -> quad.getDirection() == direction)
            ).iterator();

            float largestArea = -1.0F;
            List<BakedQuad> largestQuads = new ArrayList<>();

            int horizontalIndex = direction.getAxis() == Direction.Axis.X ? 1 : 0;
            int verticalIndex = direction.getAxis() == Direction.Axis.Z ? 1 : 2;
            while (sourceQuads.hasNext()) {
                BakedQuad quad = sourceQuads.next();
                float area = getArea(quad, horizontalIndex, verticalIndex);
                boolean tied = Mth.equal(area, largestArea);
                if (tied || (area > largestArea)) {
                    if (!tied) largestQuads.clear();
                    largestArea = Math.max(largestArea, area);
                    largestQuads.add(quad);
                }
            }

            int anchorOffset = FaceBakery.UV_INDEX;
            if (corner != null) {
                int xFace = (corner == FacadeCorner.NORTH_EAST
                    || corner == FacadeCorner.SOUTH_EAST)
                    ? FaceInfo.Constants.MAX_X
                    : FaceInfo.Constants.MIN_X;

                int zFace = (corner == FacadeCorner.SOUTH_WEST
                    || corner == FacadeCorner.SOUTH_EAST)
                    ? FaceInfo.Constants.MAX_Z
                    : FaceInfo.Constants.MIN_Z;

                for (int i = 0; i < FaceBakery.VERTEX_COUNT; i++) {
                    FaceInfo.VertexInfo vertexInfo = FaceInfo.fromFacing(direction).getVertexInfo(i);
                    if ((direction.getAxis() == Direction.Axis.X || vertexInfo.xFace == xFace)
                        && (direction.getAxis() == Direction.Axis.Y || vertexInfo.yFace == FaceInfo.Constants.MAX_Y)
                        && (direction.getAxis() == Direction.Axis.Z || vertexInfo.zFace == zFace)
                    ) {
                        anchorOffset = (i * FaceBakery.VERTEX_INT_SIZE) + FaceBakery.UV_INDEX;
                        break;
                    }
                }
            }

            List<BakedQuad> quads = new ArrayList<>(Math.max(1, largestQuads.size()));
            for (int i = 0; i < Math.max(1, largestQuads.size()); i++) {
                BakedQuad source = largestQuads.isEmpty() ? null : largestQuads.get(i);
                BlockElementFace face = new BlockElementFace(
                    direction, source == null ? BlockElementFace.NO_TINT : source.getTintIndex(), "",
                    new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0)
                );

                BakedQuad quad = FACE_BAKERY.bakeQuad(
                    CUBE_MIN, CUBE_MAX, face, source == null
                        ? model.getParticleIcon() : source.getSprite(),
                    direction, BlockModelRotation.X0_Y0, null,
                    source == null || source.isShade()
                );

                int[] vertices = quad.getVertices();
                if (source != null && getArea(source, horizontalIndex, verticalIndex) >= Mth.EPSILON) {
                    int[] sourceVertices = source.getVertices().clone();
                    FACE_BAKERY.recalculateWinding(sourceVertices, direction);

                    for (int j = 0; j < FaceBakery.VERTEX_COUNT; j++) {
                        int offset = (j * FaceBakery.VERTEX_INT_SIZE) + FaceBakery.UV_INDEX;
                        System.arraycopy(sourceVertices, offset, vertices, offset, 2);
                    }
                }

                if (corner != null) {
                    TextureAtlasSprite sprite = quad.getSprite();
                    float anchorU = Math.round(sprite.getUOffset(Float.intBitsToFloat(vertices[anchorOffset])));
                    float anchorV = Math.round(sprite.getVOffset(Float.intBitsToFloat(vertices[anchorOffset + 1])));

                    for (int j = 0; j < FaceBakery.VERTEX_COUNT; j++) {
                        int offset = (j * FaceBakery.VERTEX_INT_SIZE) + FaceBakery.UV_INDEX;

                        float u = sprite.getUOffset(Float.intBitsToFloat(vertices[offset]));
                        float v = sprite.getVOffset(Float.intBitsToFloat(vertices[offset + 1]));

                        vertices[offset] = Float.floatToRawIntBits(sprite.getU(anchorU + (u - anchorU) * 0.5F));
                        vertices[offset + 1] = Float.floatToRawIntBits(sprite.getV(anchorV + (v - anchorV) * 0.5F));
                    }
                }
                quads.add(quad);
            }
            facadeQuads.put(direction, quads);
        }
        return new SimpleBakedModel(
            List.of(), facadeQuads, model.useAmbientOcclusion(),
            model.usesBlockLight(), model.isGui3d(), model.getParticleIcon(),
            model.getTransforms(), model.getOverrides()
        );
    }

    private static float getArea(BakedQuad quad, int firstAxis, int secondAxis) {
        int[] vertices = quad.getVertices();
        float minFirst = Float.intBitsToFloat(vertices[firstAxis]);
        float maxFirst = minFirst;

        float minSecond = Float.intBitsToFloat(vertices[secondAxis]);
        float maxSecond = minSecond;

        for (int i = 1; i < FaceBakery.VERTEX_COUNT; i++) {
            int offset = (i * FaceBakery.VERTEX_INT_SIZE);
            float first = Float.intBitsToFloat(vertices[offset + firstAxis]);
            float second = Float.intBitsToFloat(vertices[offset + secondAxis]);

            minFirst = Math.min(minFirst, first);
            maxFirst = Math.max(maxFirst, first);

            minSecond = Math.min(minSecond, second);
            maxSecond = Math.max(maxSecond, second);
        }
        return (maxFirst - minFirst) * (maxSecond - minSecond);
    }

    public enum FacadeCorner {
        NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST;
        private static final FacadeCorner[] VALUES = values();
        public static FacadeCorner getFromHash(int hash) { return VALUES[Math.floorMod(hash, VALUES.length)]; }
    }
}
