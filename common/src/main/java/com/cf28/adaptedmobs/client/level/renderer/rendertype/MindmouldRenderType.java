package com.cf28.adaptedmobs.client.level.renderer.rendertype;

import com.cf28.adaptedmobs.client.registries.AMShadersRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.function.BiFunction;

public final class MindmouldRenderType extends RenderType {
    private static RenderStateShard.TexturingStateShard getTexturingState(float facadeAmount) {
        return new RenderStateShard.TexturingStateShard(
            "adaptedmobs_facade_texturing_state",
            () -> {
                RenderSystem.setShaderTexture(3, InventoryMenu.BLOCK_ATLAS);
                AMShadersRegistry.getMindmouldShader().safeGetUniform("FacadeAmount").set(facadeAmount);
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

    public static RenderType mindmould(ResourceLocation vesselTexture, float facadeAmount) {
        return MINDMOULD_FACADE.apply(vesselTexture, facadeAmount);
    }

    private static final BiFunction<ResourceLocation, Float, RenderType> MINDMOULD_FACADE
        = Util.memoize(MindmouldRenderType::createMindmould);
    private static RenderType createMindmould(ResourceLocation vesselTexture, float facadeAmount) {
        return RenderType.create(
            "adaptedmobs_rendertype_mindmould_facade",
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
            1536, false, true, RenderType.CompositeState.builder()
                .setShaderState(MINDMOULD_FACADE_SHADER)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setTextureState(new RenderStateShard.TextureStateShard(vesselTexture, false, false))
                .setTexturingState(getTexturingState(facadeAmount))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setOutputState(TRANSLUCENT_TARGET)
                .createCompositeState(true)
        );
    }
}
