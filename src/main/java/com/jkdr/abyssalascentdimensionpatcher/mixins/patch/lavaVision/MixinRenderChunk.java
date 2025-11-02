package com.jkdr.abyssalascentdimensionpatcher.mixins.patch.lavaVision;

import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Mixin to the inner class ChunkRenderDispatcher$RenderChunk
@Mixin(ChunkRenderDispatcher.RenderChunk.class)
public interface MixinRenderChunk {
    
    // Set the 'needsUpdate' field (the 'dirty' field you were trying to access) using its mapped name f_112792_
    // Note: The method name *must* start with 'set' followed by the capitalized field name (Setter).
    //@Accessor("field_175024_i")
    //void setNeedsUpdate(boolean needsUpdate);
}