package com.cf28.adaptedmobs.client.level.renderer.effect;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

// https://github.com/Monad-Modding/No-Mans-Land/blob/1.21.1/src/main/java/com/farcr/nomansland/client/renderer/effect/GreyscaleEffectRenderer.java
// WHICH I WROTE but the history doesn't show >:(
public class MindmouldEffectRenderer {
    public static MindmouldEffectRenderer INSTANCE = new MindmouldEffectRenderer();
    public static MindmouldEffectRenderer getInstance() { return INSTANCE; }

    public static ResourceLocation MINDMOULD_SHADER = AdaptedMobs.resource("shaders/post/mindmould.json");

    private boolean failed = false;
    private float width = 0.0f;
    private float height = 0.0f;

    void resizePostChain(Window window) {
        postChain.resize(window.getWidth(), window.getHeight());
        width = window.getWidth();
        height = window.getHeight();
    }

    public void resizeOrCreate() {
        Window window = Minecraft.getInstance().getWindow();
        if (postChain == null) {
            try {
                setupPostChain();
                resizePostChain(window);
            } catch (IOException e) {
                if (!failed) AdaptedMobs.LOGGER.warn("Failed to load shader: {}", MINDMOULD_SHADER, e);
                failed = true;
            }
        } else if (window.getWidth() != width || window.getHeight() != height)
            resizePostChain(window);
    }

    public PostChain postChain;
    public void setupPostChain() throws IOException {
        Minecraft minecraft = Minecraft.getInstance();
        postChain = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(),
            minecraft.getMainRenderTarget(), MINDMOULD_SHADER);
        postChain.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
    }

    public void render(Minecraft minecraft, float partialTicks, boolean isMindmould) {
        if (minecraft.level == null) return;

        this.resizeOrCreate();
        if (postChain == null) return;

        float partialTick = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
        postChain.setUniform("GameTime", ((float)(minecraft.level.getGameTime() % 24000L) + partialTick) / 24000.0F);
        postChain.setUniform("DesaturateAmount", isMindmould ? 1.f : 0.f);
        postChain.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
        postChain.process(partialTicks);
    }
}