package com.cf28.adaptedmobs.common.level.block_entity;

import com.cf28.adaptedmobs.common.level.block.HarpyEggBlock;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.registries.AMBlockEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HarpyEggBlockEntity extends BlockEntity {
    private static final int BASE_HATCH_TICKS = 18000;
    private static final int BOOSTED_HATCH_TICKS = 12000;
    private static final int WARMED_RATE = 3;
    private static final int CHECK_INTERVAL = 20;

    private int hatchProgress;

    public HarpyEggBlockEntity(BlockPos pos, BlockState state) {
        super(AMBlockEntityTypes.HARPY_EGG.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, HarpyEggBlockEntity egg) {
        if (level.getGameTime() % CHECK_INTERVAL != 0) {
            return;
        }

        int rate = rateAt(level, pos);
        if (rate <= 0) {
            return;
        }

        egg.hatchProgress += rate * CHECK_INTERVAL;
        egg.setChanged();

        int total = HarpyEggBlock.hatchBoost(level, pos) ? BOOSTED_HATCH_TICKS : BASE_HATCH_TICKS;
        int stage = state.getValue(HarpyEggBlock.HATCH);
        if (egg.hatchProgress < total * (stage + 1) / (HarpyEggBlock.MAX_HATCH_LEVEL + 1)) {
            return;
        }

        if (stage < HarpyEggBlock.MAX_HATCH_LEVEL) {
            level.playSound(null, pos, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
            level.setBlock(pos, state.setValue(HarpyEggBlock.HATCH, stage + 1), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        } else {
            hatch(level, pos);
        }
    }

    private static void hatch(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        level.destroyBlock(pos, false);

        Harpy harpy = AMEntityTypes.HARPY.get().create(level);
        if (harpy != null) {
            Vec3 vec3 = pos.getCenter();
            harpy.setBaby(true);
            harpy.moveTo(vec3.x(), vec3.y(), vec3.z(), Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
            level.addFreshEntity(harpy);
        }
    }

    private static int rateAt(Level level, BlockPos pos) {
        AABB above = new AABB(pos.above()).inflate(0.1D);

        List<Harpy> harpies = level.getEntitiesOfClass(Harpy.class, above, harpy -> !harpy.isBaby());
        if (!harpies.isEmpty()) {
            return 0;
        }

        return level.getEntitiesOfClass(Player.class, above).isEmpty() ? 1 : WARMED_RATE;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("HatchProgress", this.hatchProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.hatchProgress = tag.getInt("HatchProgress");
    }
}
