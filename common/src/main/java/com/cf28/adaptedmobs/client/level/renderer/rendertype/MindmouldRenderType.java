package com.cf28.adaptedmobs.client.level.renderer.rendertype;

import com.cf28.adaptedmobs.client.registries.AMShadersRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix3f;

public final class MindmouldRenderType extends RenderType {
    private static RenderStateShard.TexturingStateShard getTexturingState(FacadeState state) {
        return new RenderStateShard.TexturingStateShard(
            "adaptedmobs_facade_texturing_state",
            () -> {
                Minecraft minecraft = Minecraft.getInstance();
                RenderSystem.setShaderTexture(3, InventoryMenu.BLOCK_ATLAS);
                AMShadersRegistry.getMindmouldShader().safeGetUniform("FacadeAmount").set(state.facadeAmount);
                AMShadersRegistry.getMindmouldShader().safeGetUniform("FacadeTopLeft").set(state.u0, state.v0);
                AMShadersRegistry.getMindmouldShader().safeGetUniform("FacadeBottomRight").set(state.u1, state.v1);
                AMShadersRegistry.getMindmouldShader().safeGetUniform("FacadeOrigin").set(state.originX, state.originY, state.originZ);
                AMShadersRegistry.getMindmouldShader().safeGetUniform("FacadeInverseTransform").set(state.inverseTransform);
                if (minecraft.level != null) RenderSystem.setShaderGameTime(minecraft.level.getGameTime(),
                    minecraft.getTimer().getGameTimeDeltaPartialTick(false)
                );
            },
            () -> {}
        );
    }

    private static final RenderStateShard.ShaderStateShard MINDMOULD_FACADE_SHADER
        = new RenderStateShard.ShaderStateShard(AMShadersRegistry::getMindmouldShader);

    private MindmouldRenderType(
        String name, VertexFormat format,
        VertexFormat.Mode mode, int bufferSize,
        boolean affectsCrumbling, boolean sortOnUpload,
        Runnable setupState, Runnable clearState
    ) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    private record FacadeState(
        ResourceLocation vesselTexture, float facadeAmount,
        float u0, float v0, float u1, float v1,
        float originX, float originY, float originZ,
        Matrix3f inverseTransform
    ) {}

    public static RenderType mindmould(
        ResourceLocation vesselTexture, float facadeAmount,
        TextureAtlasSprite sprite, float originX, float originY,
        float originZ, Matrix3f inverseTransform
    ) {
        return createMindmouldRenderType(
            new FacadeState(
                vesselTexture, facadeAmount, sprite.getU0(),
                sprite.getV0(), sprite.getU1(), sprite.getV1(),
                originX, originY, originZ, new Matrix3f(inverseTransform)
            )
        );
    }

    private static RenderType createMindmouldRenderType(FacadeState state) {
        return RenderType.create(
            "adaptedmobs_rendertype_mindmould_facade",
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
            1536, false, true, RenderType.CompositeState.builder()
                .setShaderState(MINDMOULD_FACADE_SHADER).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY)
                .setTextureState(new RenderStateShard.TextureStateShard(state.vesselTexture(), false, false))
                .setTexturingState(getTexturingState(state))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setOutputState(ITEM_ENTITY_TARGET) // fuuuck fabulous graphics
                .createCompositeState(true)
        );
    }
}
