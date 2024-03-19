package com.cf28.adaptedmobs.common.entity.errant;

import com.cf28.adaptedmobs.client.registry.AMSoundEvents;
import com.cf28.adaptedmobs.client.renderer.layer.ErrantCapeLayer;
import com.mojang.serialization.Dynamic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Errant extends PathfinderMob {
    public float oBob;
    public float bob;
    public double xCloakO;
    public double yCloakO;
    public double zCloakO;
    public double xCloak;
    public double yCloak;
    public double zCloak;

    public Errant(EntityType<Errant> type, Level level) {
        super(type, level);
        this.maxUpStep = 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 120.0)
//            .add(Attributes.MOVEMENT_SPEED, 0.3F)
            .add(Attributes.ATTACK_DAMAGE, 7.0)
            .add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    protected Brain.Provider<Errant> brainProvider() {
        return Brain.provider(ErrantBrain.MEMORIES, ErrantBrain.SENSORS);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return ErrantBrain.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override @SuppressWarnings("unchecked")
    public Brain<Errant> getBrain() {
        return (Brain<Errant>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("piglinBrain");
        this.getBrain().tick((ServerLevel) this.level, this);
        this.level.getProfiler().pop();
        ErrantBrain.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        super.tick();
        this.moveCloak();
    }

    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;
        double d = this.getX() - this.xCloak;
        double e = this.getY() - this.yCloak;
        double f = this.getZ() - this.zCloak;
        if (d > 10.0) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (f > 10.0) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (e > 10.0) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        if (d < -10.0) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (f < -10.0) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (e < -10.0) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        this.xCloak += d * 0.25;
        this.zCloak += f * 0.25;
        this.yCloak += e * 0.25;
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        this.updateNoActionTime();
        this.oBob = this.bob;
        super.aiStep();

        float f = 0.0F;

        if (this.onGround && !this.isDeadOrDying() && !this.isSwimming()) {
            f = Math.min(0.1F, (float)this.getDeltaMovement().horizontalDistance());
        }

        this.bob += (f - this.bob) * 0.4F;
    }

    @SuppressWarnings("deprecation")
    protected void updateNoActionTime() {
        if (this.getLightLevelDependentMagicValue() > 0.5F) {
            this.noActionTime += 2;
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    @Override @Nullable
    protected SoundEvent getAmbientSound() {
        return AMSoundEvents.ERRANT_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return AMSoundEvents.ERRANT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOSTILE_DEATH;
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.HOSTILE_SMALL_FALL, SoundEvents.HOSTILE_BIG_FALL);
    }

    @Override
    public boolean shouldDropExperience() {
        return true;
    }

    @Override
    protected boolean shouldDropLoot() {
        return true;
    }

    @Override
    public ItemStack getProjectile(ItemStack weaponStack) {
        if (weaponStack.getItem() instanceof ProjectileWeaponItem projectile) {
            Predicate<ItemStack> supportedProjectiles = projectile.getSupportedHeldProjectiles();
            ItemStack stack = ProjectileWeaponItem.getHeldProjectile(this, supportedProjectiles);
            return stack.isEmpty() ? new ItemStack(Items.ARROW) : stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ResourceLocation getCloakTextureLocation() {
        return ErrantCapeLayer.GREEN_CAPE_LOCATION;
    }
}