package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SupportCreepieEntity extends Creepie {
    private static final double BUFF_RANGE_SQR = 2.25;

    private Variant variant = Variant.SPEED;

    public SupportCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(24);
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant v) {
        this.variant = v;
        if (v == Variant.SPEED || v == Variant.STRENGTH) {
            this.setFuseTime(100);
            this.ignite();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (variant != Variant.SPEED && variant != Variant.STRENGTH) {
            return;
        }

        Entity owner = this.getOwner();
        if (owner == null) {
            this.explodeCustom();
        } else if (this.distanceToSqr(owner) <= BUFF_RANGE_SQR) {
            this.explodeCustom();
        } else if (this.getNavigation().isDone()) {
            this.getNavigation().moveTo(owner, 1.2);
        }
    }

    @Override
    public void setAge(int age) {
        if (!this.level().isClientSide() && age >= 0) {
            this.convertTo(AMEntityTypes.SUPPORT_CREEPER.get(), false);
            return;
        }
        super.setAge(age);
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.dead = true;
        ServerLevel sl = (ServerLevel) this.level();

        if (variant == Variant.SPEED || variant == Variant.STRENGTH) {
            Holder<MobEffect> effect = variant == Variant.SPEED ? MobEffects.MOVEMENT_SPEED : MobEffects.DAMAGE_BOOST;
            for (Player p : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(2.5))) {
                p.addEffect(new MobEffectInstance(effect, 200, 0));
            }
            SimpleParticleType particle = variant == Variant.SPEED
                    ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_RED.get();
            TolerableCreepersIntegration.spawnParticleRing(sl, particle, this.position().add(0.0, 0.1, 0.0), 0.6, 16);
        } else {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
            Holder<MobEffect> debuff = variant == Variant.SLOWNESS ? MobEffects.MOVEMENT_SLOWDOWN : MobEffects.WEAKNESS;
            for (Player p : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(3.0))) {
                p.addEffect(new MobEffectInstance(debuff, 120, 0));
            }
            SimpleParticleType particle = variant == Variant.SLOWNESS
                    ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_GREY.get();
            TolerableCreepersIntegration.spawnParticleRing(sl, particle, this.position().add(0.0, 0.1, 0.0), 0.6, 16);
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
