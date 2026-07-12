package com.cf28.adaptedmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class AMFlowerParticle extends TextureSheetParticle {
    protected AMFlowerParticle(ClientLevel clientLevel, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, SpriteSet spriteSet, float sizeMultiplier) {
        super(clientLevel, x, y, z);
        this.gravity = 0.1F;
        this.friction = 0.6F;
        this.xd = xVelocity;
        this.yd = yVelocity;
        this.zd = zVelocity;
        this.quadSize = 0.2F * sizeMultiplier * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
        this.lifetime = (int) (20.0D / (this.random.nextFloat() * 0.8D + 0.2D)) + 2;
        this.roll = this.oRoll = this.random.nextFloat() * (float) Math.PI * 2.0F;
        this.pickSprite(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.oRoll = this.roll;
            this.roll += (float) (Math.PI * Math.min(0.5, this.yd) * 2.0F);

            this.yd -= 0.04 * this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1;
                this.zd *= 1.1;
            }

            this.xd *= this.friction;
            this.yd *= this.friction;
            this.zd *= this.friction;
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final float sizeMultiplier;

        public Provider(SpriteSet spriteSet) {
            this(spriteSet, 1.0F);
        }

        public Provider(SpriteSet spriteSet, float sizeMultiplier) {
            this.sprites = spriteSet;
            this.sizeMultiplier = sizeMultiplier;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity) {
            return new AMFlowerParticle(level, x, y, z, xVelocity, yVelocity, zVelocity, this.sprites, this.sizeMultiplier);
        }
    }
}
