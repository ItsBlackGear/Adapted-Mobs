package com.cf28.adaptedmobs.common.level.entity.mob.creeper;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RocketCreeper extends TamableCreeper {
    private static final EntityDataAccessor<Boolean> IS_ROCKET = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceKey<LootTable> AM_EXPLODE_LOOT_TABLE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "entities/rocket_creeper_explode"));
    private int timeBeforeJumping;

    public RocketCreeper(EntityType<? extends TamableCreeper> entityType, Level level) {
        super(entityType, level);
        this.setRocket(false);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 10;
    }

    @Override
    protected void postExplosion() {
        super.postExplosion();
        this.setRocket(false);
        if (!this.level().isClientSide() && TolerableCreepersCompat.isLoaded()) {
            Entity spores = TolerableCreepersIntegration.createRocketSporesFromCreeper(this.level(), this);
            this.level().addFreshEntity(spores);
        }
    }

    @Override
    protected ResourceKey<LootTable> getExplosionLootTable() {
        return TolerableCreepersCompat.isLoaded() ? AM_EXPLODE_LOOT_TABLE : null;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (this.isRocket()) {
            this.setSwellDir(-1);
            this.explodeCreeper();
        }

        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    public boolean hasEnoughVerticalSpace() {
        BlockPos pos = this.blockPosition();
        while (pos.getY() < this.level().getHeight()) {
            BlockState state = this.level().getBlockState(pos);
            if (!state.canBeReplaced())
                return false;

            pos = pos.above();
        }

        return true;
    }

    @Override
    public void tick() {
        this.setupAnimations();
        super.tick();

        this.launchTowardsTarget();
        if (this.level().isClientSide && this.isRocket()) {
            this.spawnSmokeParticles();
        }

        if (this.isInWaterOrBubble() && this.getState().is(CreeperState.ATTACKING)) {
            this.setState(CreeperState.IDLING);
        }
    }

    private void launchTowardsTarget() {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (this.distanceToSqr(target) > 25) {
                this.setSwellDir(-1);
            }
        }

        if (this.getSwellDir() > 0) {
            this.timeBeforeJumping++;
        } else {
            this.timeBeforeJumping = 0;
        }

        if (this.shouldRocket() && target != null) {
            this.setState(CreeperState.ATTACKING);
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 0.5F);
            this.setDeltaMovement((target.getX() - this.getX()) / 6.0D, 1.2D, (target.getZ() - this.getZ()) / 6.0D);

            this.setRocket(true);
            this.fallDistance = 0.0F;
        }
    }

    private boolean shouldRocket() {
        return this.timeBeforeJumping > 15
                && this.isAlive()
                && this.getSwellDir() > 0
                && this.onGround()
                && this.hasEnoughVerticalSpace();
    }

    private void spawnSmokeParticles() {
        this.level().addParticle(
                ParticleTypes.SMOKE,
                this.getX(),
                this.getY(),
                this.getZ(),
                this.random.nextGaussian() * 0.02,
                this.random.nextGaussian() * 0.02,
                this.random.nextGaussian() * 0.02
        );
    }

    @Override
    protected ItemStack getSkull() {
        return new ItemStack(AMBlocks.ROCKET_CREEPER_HEAD.getFirst().get());
    }

    @Override
    public float getExplosionDamageMultiplier() {
        return 1.5F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);

        if (recentlyHit && Math.max(this.random.nextFloat() - (float) getLooting(level, source.getEntity()) * 0.01F, 0.0F) < 0.2F) {
            ItemStack stack = new ItemStack(Items.FIREWORK_STAR);
            FireworkExplosion explosion = new FireworkExplosion(FireworkExplosion.Shape.CREEPER, IntList.of(DyeColor.LIGHT_BLUE.getFireworkColor()), IntList.of(), false, false);
            stack.set(DataComponents.FIREWORK_EXPLOSION, explosion);
            this.spawnAtLocation(stack);
        }
    }

    private int getLooting(ServerLevel level, Entity entity) {
        Optional<Registry<Enchantment>> registry = level.registryAccess().registry(Registries.ENCHANTMENT);
        if (registry.isPresent()) {
            Registry<Enchantment> lookup = registry.get();
            Enchantment enchantment = lookup.get(Enchantments.LOOTING);
            return entity instanceof Player player ? EnchantmentHelper.getEnchantmentLevel(lookup.wrapAsHolder(enchantment), player) : 0;
        }

        return 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_ROCKET, false);
    }

    @Override
    public boolean canFollow() {
        return !this.isRocket();
    }

    public boolean isRocket() {
        return this.entityData.get(IS_ROCKET);
    }

    public void setRocket(boolean rocket) {
        this.entityData.set(IS_ROCKET, rocket);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Rocket", this.isRocket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setRocket(tag.getBoolean("Rocket"));
    }

    @Override
    public ClothType getClothType() {
        return ClothType.ROCKET;
    }
}