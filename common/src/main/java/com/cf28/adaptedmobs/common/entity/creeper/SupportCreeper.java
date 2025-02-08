package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.creeper.ai.ApplyBuffsToTargetGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * FEATURES:
 * - they run away from players and look for other hostile mobs to follow *
 * - once following a mob, they buff themselves and the mob with potions such as speed and fire resistance *
 * - when they follow creepers, they will turn them into charged creepers
 * - if it happens to be charged, the potion effects that it provides will be more powerful *
 * - they have a rare chance of dropping a yellow Mystery Egg that hatches a tame baby Support Creeper *
 * - the baby support creeper will start helping you right away and give you several helpful potion effects *
 * - [additional note] apparently, owners aren't able to hit tamed creepers... *
 * ADDITIONAL CHANGES:
 * - runs quickly like an ocelot away from players *
 * - looks for hostile mobs to follow and buff with strength I and speed I *
 * - when below 50% health and there is no mob nearby to buff, it will explode like a normal creeper if you get too close *
 * - when charged, the buffs it gives are tier II and its explosion when at low health is larger and deals more damage *
 * - the charge applied by support creeper will go away after 5 minutes
 * LOOT:
 * - gunpowder [similar to creepers]
 * - yellow mystery egg [very rare]
 */
public class SupportCreeper extends TamableCreeper {
    private static final EntityDataAccessor<Optional<UUID>> SUPPORTED_ENTITY_UUID = SynchedEntityData.defineId(SupportCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DATA_ID_VARIANT = SynchedEntityData.defineId(SupportCreeper.class, EntityDataSerializers.INT);

    private static final Set<String> PEEPER_NAMES = Set.of("Pee Dog", "PeeingDog", "Peeper", "pee dog", "peeingdog", "peeper");

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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUPPORTED_ENTITY_UUID, Optional.empty());
        this.entityData.define(DATA_ID_VARIANT, Variant.NORMAL.ordinal());
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

    @Nullable
    public UUID getSupportedUUID() {
        return this.entityData.get(SUPPORTED_ENTITY_UUID).orElse(null);
    }

    public void setSupportedUUID(@Nullable UUID uuid) {
        this.entityData.set(SUPPORTED_ENTITY_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Variant", this.getVariant().name);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
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
    public boolean canTarget() {
        return false;
    }

    @Override
    public ClothType getClothType() {
        return ClothType.SUPPORT;
    }

    public void setVariant(Variant variant) {
        this.entityData.set(DATA_ID_VARIANT, variant.ordinal());
    }

    public Variant getVariant() {
        return Variant.byId(this.entityData.get(DATA_ID_VARIANT));
    }

    public enum Variant implements StringRepresentable {
        NORMAL("normal"),
        PEEPER("peepper");

        private final String name;

        Variant(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
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
    }
}