package com.cf28.adaptedmobs.client.level;

import com.cf28.adaptedmobs.common.level.entity.mob.Stranger;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StrangerSpawner {
    private static final int CHECK_INTERVAL = 600;
    private static final int PLACEMENT_ATTEMPTS = 16;
    private static final double MIN_DISTANCE = 48.0;
    private static final double MAX_DISTANCE = 72.0;

    public static void tick(ClientLevel level) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.level() != level) return;

        if (level.getEntity(Stranger.CLIENT_ID) instanceof Stranger stranger) {
            stranger.tickFor(player);
            return;
        }

        if (level.dimension() != Level.OVERWORLD || player.isSpectator() || level.getGameTime() % CHECK_INTERVAL != 0)
            return;
        if (level.random.nextDouble() >= AdaptedMobs.CONFIG.strangerChance.get()) return;

        trySpawn(level, player);
    }

    private static void trySpawn(ClientLevel level, LocalPlayer player) {
        EntityType<Stranger> type = AMEntityTypes.STRANGER.get();
        RandomSource random = level.random;
        Vec3 eyePosition = player.getEyePosition();

        for (int attempt = 0; attempt < PLACEMENT_ATTEMPTS; attempt++) {
            float yaw = (player.getYRot() + 100.0F + random.nextFloat() * 160.0F) * Mth.DEG_TO_RAD;
            double distance = Mth.nextDouble(random, MIN_DISTANCE, MAX_DISTANCE);
            int x = Mth.floor(player.getX() - Mth.sin(yaw) * distance);
            int z = Mth.floor(player.getZ() + Mth.cos(yaw) * distance);

            if (!level.isLoaded(new BlockPos(x, player.getBlockY(), z))) continue;

            BlockPos pos = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), z);
            BlockPos ground = pos.below();
            BlockState groundState = level.getBlockState(ground);
            if (!groundState.getFluidState().isEmpty() || groundState.is(BlockTags.LEAVES) || !groundState.isFaceSturdy(level, ground, Direction.UP))
                continue;

            double spawnX = x + 0.5;
            double spawnZ = z + 0.5;
            if (!level.noCollision(type.getSpawnAABB(spawnX, pos.getY(), spawnZ))) continue;

            Vec3 strangerEyes = new Vec3(spawnX, pos.getY() + type.getDimensions().eyeHeight(), spawnZ);
            ClipContext clip = new ClipContext(eyePosition, strangerEyes, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player);
            if (level.clip(clip).getType() != HitResult.Type.MISS) continue;

            Stranger stranger = type.create(level);
            if (stranger == null) return;

            stranger.moveTo(spawnX, pos.getY(), spawnZ, 0.0F, 0.0F);
            stranger.lookAt(EntityAnchorArgument.Anchor.EYES, eyePosition);
            level.addEntity(stranger);
            return;
        }
    }
}
