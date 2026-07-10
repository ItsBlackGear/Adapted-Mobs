package com.cf28.adaptedmobs.common.level.entity.mob.creeper;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.level.entity.ai.goal.ApplyBuffsToTargetGoal;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class SupportCreeper extends TamableCreeper {
    private static final EntityDataAccessor<Optional<UUID>> SUPPORTED_ENTITY_UUID = SynchedEntityData.defineId(SupportCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DATA_ID_VARIANT = SynchedEntityData.defineId(SupportCreeper.class, EntityDataSerializers.INT);

    private static final Set<String> PEEPER_NAMES = Set.of("Pee Dog", "PeeingDog", "Peeper", "pee dog", "peeingdog", "peeper");
    private static final ResourceKey<LootTable> AM_EXPLODE_LOOT_TABLE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "entities/support_creeper_explode"));

    public SupportCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ApplyBuffsToTargetGoal(this, 16.0, 1.25));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.0F, 1.25F, target -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target) && !this.isTame()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SUPPORTED_ENTITY_UUID, Optional.empty());
        builder.define(DATA_ID_VARIANT, Variant.NORMAL.ordinal());
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);

        if (name != null) {
            boolean isPeeper = PEEPER_NAMES.contains(name.getString());
            if (this.getVariant() == Variant.NORMAL && isPeeper) {
                this.setVariant(Variant.PEEPER);
            } else if (this.getVariant() == Variant.PEEPER && !isPeeper) {
                this.setVariant(Variant.NORMAL);
            }
        }
    }

    @Override
    public void setTame(boolean tame) {
        super.setTame(tame);
        if (tame) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3F);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
        }
    }

    @Override
    public void tick() {
        this.setupAnimations();
        super.tick();
    }

    @Override
    protected ItemStack getSkull() {
        return new ItemStack(this.getVariant() == Variant.PEEPER ? AMBlocks.PEEPER_CREEPER_HEAD.getFirst().get() : AMBlocks.SUPPORT_CREEPER_HEAD.getFirst().get());
    }

    @Nullable
    public UUID getSupportedUUID() {
        return this.entityData.get(SUPPORTED_ENTITY_UUID).orElse(null);
    }

    public void setSupportedUUID(@Nullable UUID uuid) {
        this.entityData.set(SUPPORTED_ENTITY_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Variant", this.getVariant().name);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant", 8)) {
            this.setVariant(Variant.byName(tag.getString("Variant")));
        }
    }

    @Override
    public boolean shouldSwell() {
        return (this.getSupportedUUID() == null && this.getHealth() <= this.getMaxHealth() / 2) || this.isIgnited();
    }

    @Override
    protected void explodeCreeper() {
        if (this.shouldSwell() && !this.isTame() && TolerableCreepersCompat.isLoaded() && AdaptedMobs.CONFIG.preventSupportCreeperBlockDamage.get()) {
            if (!this.level().isClientSide()) {
                float explosionMultiplier = this.isPowered() ? 2.0F : 1.0F;
                this.explodeWithoutBlockDamage((float) this.explosionRadius * explosionMultiplier);
                this.discard();
                this.postExplosion();
            }
        } else {
            super.explodeCreeper();
        }
    }

    @Override
    protected void postExplosion() {
        super.postExplosion();
        if (!this.level().isClientSide() && TolerableCreepersCompat.isLoaded()) {
            Entity spores = TolerableCreepersIntegration.createSupportSporesFromCreeper(this.level(), this);
            this.level().addFreshEntity(spores);
        }
    }

    @Override
    protected ResourceKey<LootTable> getExplosionLootTable() {
        return TolerableCreepersCompat.isLoaded() ? AM_EXPLODE_LOOT_TABLE : null;
    }

    @Override
    public boolean canTarget() {
        return false;
    }

    @Override
    public ClothType getClothType() {
        return ClothType.SUPPORT;
    }

    public Variant getVariant() {
        return Variant.byId(this.entityData.get(DATA_ID_VARIANT));
    }

    public void setVariant(Variant variant) {
        this.entityData.set(DATA_ID_VARIANT, variant.ordinal());
    }

    public enum Variant implements StringRepresentable {
        NORMAL("normal"),
        PEEPER("peepper");

        private final String name;

        Variant(String name) {
            this.name = name;
        }

        public static Variant byId(int id) {
            Variant[] variants = values();
            return (id >= 0 && id < variants.length) ? variants[id] : variants[0];
        }

        public static Variant byName(String name) {
            for (Variant variant : values()) {
                if (variant.name.equals(name)) {
                    return variant;
                }
            }

            return NORMAL;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}