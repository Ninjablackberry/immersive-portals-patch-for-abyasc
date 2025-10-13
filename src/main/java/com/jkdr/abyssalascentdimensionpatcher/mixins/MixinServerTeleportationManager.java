package com.jkdr.abyssalascentdimensionpatcher.mixins;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import qouteall.imm_ptl.core.teleportation.ServerTeleportationManager;

@Mixin(value = qouteall.imm_ptl.core.teleportation.ServerTeleportationManager.class, remap = false)
public abstract class MixinServerTeleportationManager {
      private static final Logger LOGGER = LogUtils.getLogger();

      //Inject into immersive portals "changePlayerDimension"
      //Fabric version does not properly subscribe to dimension change events and does not refresh capabilities correctly
    @Inject(
        method = "changePlayerDimension",
        at = @At("TAIL")
    )
    private void afterChangeDimensions(ServerPlayer player, ServerLevel fromWorld, ServerLevel toWorld, Vec3 newEyePos, CallbackInfo ci) {
            player.invalidateCaps(); //Remove the current capabilities from the previous dimensions.
            player.reviveCaps(); //Get the players current capabilities and load them.

          //Create a fake dimension change event so mods including Dimension Viewer refresh correctly
            PlayerEvent.PlayerChangedDimensionEvent event =
                new PlayerEvent.PlayerChangedDimensionEvent(player, fromWorld.dimension(), toWorld.dimension());

          //Tell forge to send out subscribed updates from mods which rely on this event.
            MinecraftForge.EVENT_BUS.post(event);
    }

}
