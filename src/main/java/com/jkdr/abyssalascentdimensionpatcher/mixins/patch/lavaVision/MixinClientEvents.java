package com.jkdr.abyssalascentdimensionpatcher.mixins.patch.lavaVision;

import com.github.alexthe666.alexsmobs.client.event.ClientEvents;
import com.jkdr.abyssalascentdimensionpatcher.mixins.patch.lavaVision.MixinLevelRenderer;
import com.jkdr.abyssalascentdimensionpatcher.mixins.patch.lavaVision.MixinRenderChunk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;

import com.github.alexthe666.alexsmobs.client.event.ClientEvents;

@Mixin(value = ClientEvents.class, remap = false)
public abstract class MixinClientEvents {
    /*

    @Inject(
        method = "updateAllChunks",
        at = @At("HEAD")
    )
    //i just added this cause i kept crashing on der's server and drank a potion
    private static void onUpdateAllChunks(CallbackInfo ci) { // Make sure the method signature matches the target
        Minecraft minecraft = Minecraft.getInstance();
        
        // 1. Get the private viewArea field using the LevelRendererAccessor
        ChunkRenderDispatcher viewArea = ((LevelRendererAccessor)minecraft.levelRenderer).getViewArea();

        if (viewArea != null) {
            
            // Note: The array of chunks is a public field on the ChunkRenderDispatcher, so we access it directly.
            // The field for the chunk array is typically named 'chunks'.
            ChunkRenderDispatcher.RenderChunk[] chunks = viewArea.chunks;

            int length = chunks.length;
            for (int i = 0; i < length; i++) {
                
                // 2. Add the crucial null check
                ChunkRenderDispatcher.RenderChunk chunk = chunks[i];

                if (chunk != null) {
                    
                    // 3. Set the 'dirty' status (mapped to needsUpdate) using the RenderChunkAccessor
                    ((RenderChunkAccessor)chunk).setNeedsUpdate(true);
                }
            }
        }

        ci.cancel();
    }
    */
}