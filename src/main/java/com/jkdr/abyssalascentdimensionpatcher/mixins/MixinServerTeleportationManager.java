package com.jkdr.abyssalascentdimensionpatcher.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = qouteall.imm_ptl.core.teleportation.ServerTeleportationManager.class, remap = false)
public abstract class MixinServerTeleportationManager {
    @Inject(
        method = "changePlayerDimension",
        at = @At("TAIL")
    )
    private void afterChangeDimensions(ServerPlayer player, ServerLevel fromWorld, ServerLevel toWorld, Vec3 newEyePos, CallbackInfo ci) {
            //Delete current player capabilites
            player.invalidateCaps();
            //Revive capabilites
            player.reviveCaps();
            
            //Create a fake dimension change event so mods including Dimension Viewer refresh correctly
            PlayerEvent.PlayerChangedDimensionEvent event =
                new PlayerEvent.PlayerChangedDimensionEvent(player, fromWorld.dimension(), toWorld.dimension());

            MinecraftForge.EVENT_BUS.post(event);
    }

}