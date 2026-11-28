package com.cf28.adaptedmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AMSporeSmokeParticle extends TextureSheetParticle {
    private static final float MAX_ALPHA = 0.18F;
    private static final float FADE_IN = 0.2F;

    private final SpriteSet sprites;

    protected AMSporeSmokeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.friction = 0.96F;
        this.gravity = -0.005F;
        this.hasPhysics = false;
        this.xd = xSpeed + (this.random.nextDouble() - 0.5) * 0.02;
        this.yd = ySpeed + this.random.nextDouble() * 0.01;
        this.zd = zSpeed + (this.random.nextDouble() - 0.5) * 0.02;
        this.quadSize *= 4.0F + this.random.nextFloat() * 2.0F;
        this.lifetime = 30 + this.random.nextInt(20);
        this.roll = this.oRoll = this.random.nextFloat() * Mth.TWO_PI;

        float shade = 0.9F + this.random.nextFloat() * 0.1F;
        this.setColor(0.98F * shade, 0.82F * shade, 0.32F * shade);
        this.alpha = 0.0F;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.removed) return;

        this.setSpriteFromAge(this.sprites);
        float progress = (float) this.age / this.lifetime;
        float fade = progress < FADE_IN ? progress / FADE_IN : 1.0F - (progress - FADE_IN) / (1.0F - FADE_IN);
        this.alpha = MAX_ALPHA * Mth.clamp(fade, 0.0F, 1.0F);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AMSporeSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}
