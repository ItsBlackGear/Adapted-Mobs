package com.cf28.adaptedmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class AMMindmouldParticle extends BreakingItemParticle {
    protected AMMindmouldParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, new ItemStack(Items.SLIME_BALL));
        this.pickSprite(spriteSet);
    }

    @Override public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
            SimpleParticleType type, ClientLevel level,
            double x, double y, double z, double xSpeed,
            double ySpeed, double zSpeed
        ) {
            return new AMMindmouldParticle(level, x, y, z, spriteSet);
        }
    }
}
