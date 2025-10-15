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

import org.spongepowered.asm.mixin.injection.Redirect;
import org.objectweb.asm.Opcodes;

@Mixin(value = ServerPlayerGameMode.class, priority = 2000)
public abstract class AbyssalAscent_ServerPlayerGameMode_Patch {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Shadow
    private ServerLevel level;

    //Gets the ACTUAL world provided by immptlcore
    private ServerLevel ip_getActualWorld() {
        //Get the saved server level when the player did an action from another dimension, if it is null it means the player did a regular action and immersive portals will let the vanilla / other mods handle it
        ServerLevel redirect = BlockManipulationServer.SERVER_PLAYER_INTERACTION_REDIRECT.get();
        if (redirect != null) {
            //Take over and send the correct dimension
            return redirect;
        }
        //Continue as normal
        return level;
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    //Wrap operation does not change the method but waits until "canReachRaw" is called and changes the value depending on if it is through a portal.
    @WrapOperation(
        method = "handleBlockBreakAction",
        at = @At(
            value = "INVOKE", //Immersive portals tries to access the wrong method and is not designed to access "canReachRaw" so we have to call it manually. This is not immersive portal's fault instead its just a mod incompatibility.
            target = "Lnet/minecraft/server/level/ServerPlayer;canReachRaw(Lnet/minecraft/core/BlockPos;D)Z"
        )
    )
    private boolean wrapDistanceInHandleBlockBreakAction(
        ServerPlayer instance, BlockPos blockPos, double v, Operation<Boolean> original
    ) {
        ServerLevel redirect = BlockManipulationServer.SERVER_PLAYER_INTERACTION_REDIRECT.get(); //Gets the value of player interaction through portal if value is null it means the interaction is normal and if its not null it is through a portal.
        if (redirect != null) {
            return true; //Is through a portal, do not check in the original method.
        }
        return original.call(instance, blockPos, v); //Is normal, continue with operation.
    }

    //Get this method added by a mod which caused incompatibility
    @Redirect(
        method = {
            "removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
        },
        at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/server/level/ServerPlayerGameMode;level:Lnet/minecraft/server/level/ServerLevel;"
        )
    )
    private ServerLevel redirectGetLevel(
        ServerPlayerGameMode serverPlayerGameMode
    ) {
        return ip_getActualWorld(); //On the value retrieved, return the correct dimension now allowing the proper block to be processed into breaking.
    }
}
