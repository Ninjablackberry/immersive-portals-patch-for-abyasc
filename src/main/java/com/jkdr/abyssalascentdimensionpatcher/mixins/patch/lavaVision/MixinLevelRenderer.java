package com.jkdr.abyssalascentdimensionpatcher.mixins.patch.lavaVision;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Mixin to net.minecraft.client.renderer.LevelRenderer
@Mixin(LevelRenderer.class)
public interface MixinLevelRenderer {
    
    // Get the ChunkRenderDispatcher (the 'viewArea' in source) using its mapped name f_109469_
    // Note: The AT calls it 'viewFrustum', but this is the field that holds the ChunkRenderDispatcher.
    //@Accessor("viewArea") 
    //ChunkRenderDispatcher getViewArea(); 
}