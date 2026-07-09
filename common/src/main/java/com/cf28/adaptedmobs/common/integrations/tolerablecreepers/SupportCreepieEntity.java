package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SupportCreepieEntity extends Creepie {
    private Variant variant = Variant.SPEED;

    public SupportCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(8);
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant v) {
        this.variant = v;
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.dead = true;

        if (variant == Variant.SPEED || variant == Variant.STRENGTH) {
            Holder<MobEffect> effect = variant == Variant.SPEED ? MobEffects.MOVEMENT_SPEED : MobEffects.DAMAGE_BOOST;
            for (Player p : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(2.5))) {
                p.addEffect(new MobEffectInstance(effect, 200, 0));
            }
            ServerLevel sl = (ServerLevel) this.level();
            SimpleParticleType particle = variant == Variant.SPEED
                    ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_RED.get();
            sl.sendParticles(particle, this.getX(), this.getY() + 0.25, this.getZ(), 20, 0.4, 0.4, 0.4, 0.05);
        } else {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
            Holder<MobEffect> debuff = variant == Variant.SLOWNESS ? MobEffects.MOVEMENT_SLOWDOWN : MobEffects.WEAKNESS;
            for (Player p : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(3.0))) {
                p.addEffect(new MobEffectInstance(debuff, 120, 0));
            }
            ServerLevel sl = (ServerLevel) this.level();
            SimpleParticleType particle = variant == Variant.SLOWNESS
                    ? AMParticles.SUPPORTED_YELLOW.get() : AMParticles.SUPPORTED_GREY.get();
            sl.sendParticles(particle, this.getX(), this.getY() + 0.25, this.getZ(), 20, 0.4, 0.4, 0.4, 0.05);
        }

        this.discard();
    }

    public enum Variant {
        SPEED, STRENGTH, SLOWNESS, WEAKNESS;

        public ResourceLocation getTexture() {
            return ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID,
                    "textures/entity/tolerable_creepers/support_creepie_" + this.name().toLowerCase() + ".png");
        }
    }
}
