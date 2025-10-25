package com.jkdr.abyssalascentdimensionpatcher.mixins;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = qouteall.imm_ptl.core.teleportation.ClientTeleportationManager.class, remap = false)
public abstract class MixinClientTeleportationManager {


    @Inject(
        method = "changePlayerDimension",
        at = @At("TAIL")
    )
    private static void onClientChangeDimension(
        LocalPlayer player,         // Note: This is LocalPlayer, not ServerPlayer
        ClientLevel fromWorld, 
        ClientLevel toWorld, 
        Vec3 newEyePos, 
        CallbackInfo ci) { // Make sure the method signature matches the target
        
        // This is the part you replicate from the server
        PlayerEvent.PlayerChangedDimensionEvent event =
            new PlayerEvent.PlayerChangedDimensionEvent(
                player, 
                fromWorld.dimension(), 
                toWorld.dimension()
            );

        MinecraftForge.EVENT_BUS.post(event);
    }
}