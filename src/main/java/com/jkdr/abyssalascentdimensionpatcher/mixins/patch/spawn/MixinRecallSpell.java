package com.jkdr.abyssalascentdimensionpatcher.mixins.patch.spawn;

import com.jkdr.abyssalascentdimensionpatcher.eventHooks.playerSpawnEvents;
import com.jkdr.abyssalascentdimensionpatcher.util.ModInternalConfig;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.spells.ender.RecallSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = RecallSpell.class, priority = 2000)
public abstract class MixinRecallSpell {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "findSpawnPosition", at = @At("RETURN"), cancellable = true, remap = false)
    private static void onFindSpawnPositionReturn(ServerLevel level, ServerPlayer player, CallbackInfoReturnable<Optional<Vec3>> cir) {
        Optional<Vec3> original = cir.getReturnValue();

        //Check if Irons Spells did not find a spawn
        if (original.isEmpty()) {
            //We do a proper check, getting the players respawn dimension
            if (player.getRespawnDimension() != null) {
                cir.setReturnValue(Optional.of(getRespawnPosition(player)));
                cir.cancel();
            }
        }
    }

    private static Vec3 getRespawnPosition(ServerPlayer player) {
        if (player.getRespawnPosition() == null) {

            MinecraftServer server = player.getServer();
            if (server == null) {
                // fallback to origin if server not loaded
                return Vec3.ZERO;
            }

            ServerLevel undergardenLevel = server.getLevel(ModInternalConfig.playerSpawnDimension);
            if (undergardenLevel == null) {
                return Vec3.ZERO;
            }

            BlockPos rawPos = playerSpawnEvents.getSpawnCoordinates(undergardenLevel);
            BlockPos adjustedPos = rawPos.offset(-5, 1, 3);

            return Vec3.atCenterOf(adjustedPos);
        }

        return Vec3.atCenterOf(player.getRespawnPosition());
    }
}
