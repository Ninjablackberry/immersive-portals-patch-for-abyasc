package com.jkdr.abyssalascentdimensionpatcher.mixins;

import qouteall.imm_ptl.core.block_manipulation.BlockManipulationServer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.slf4j.Logger;
import net.minecraft.server.level.ServerLevel;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ServerPlayerGameMode.class, priority = 2000)
public abstract class AbyssalAscent_ServerPlayerGameMode_Patch {

    @Shadow
    @Final
    protected ServerPlayer player;

    private static final Logger LOGGER = LogUtils.getLogger();

    @WrapOperation(
        method = "handleBlockBreakAction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;canReachRaw(Lnet/minecraft/core/BlockPos;D)Z"
        )
    )
    private boolean wrapDistanceInHandleBlockBreakAction(
        ServerPlayer instance, BlockPos blockPos, double v, Operation<Boolean> original
    ) {
        ServerLevel redirect = BlockManipulationServer.SERVER_PLAYER_INTERACTION_REDIRECT.get();
        if (redirect != null) {
            return true;
        }
        return original.call(instance, blockPos, v);
    }
}