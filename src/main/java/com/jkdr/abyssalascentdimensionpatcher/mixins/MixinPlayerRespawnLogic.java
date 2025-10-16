package com.jkdr.abyssalascentdimensionpatcher.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.PlayerRespawnLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerRespawnLogic.class)
public interface MixinPlayerRespawnLogic {

    //Make this "protected" method accessable to us
    //This method holds all of the logic of converting a single block spawn into the vanilla "scattered" functionality
    @Invoker("getOverworldRespawnPos")
    static BlockPos invokeGetOverworldRespawnPos(ServerLevel level, int x, int z) {
        throw new AssertionError();
    }
}
