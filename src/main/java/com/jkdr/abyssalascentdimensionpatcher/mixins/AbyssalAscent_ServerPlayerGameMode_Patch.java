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

import com.jkdr.abyssalascentdimensionpatcher.data.dimensionRoofData;
// Import the new interface
import com.jkdr.abyssalascentdimensionpatcher.interfaces.ServerLevelDataAccessor; 
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.core.BlockPos;

import com.jkdr.abyssalascentdimensionpatcher.util.ServerMessages;

@Mixin(value = ServerPlayerGameMode.class, priority = 2000)
public abstract class AbyssalAscent_ServerPlayerGameMode_Patch {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Shadow
    private ServerLevel level;

    private ServerLevel ip_getActualWorld() {
        ServerLevel redirect = BlockManipulationServer.SERVER_PLAYER_INTERACTION_REDIRECT.get();
        if (redirect != null) {
            return redirect;
        }
        return level;
    }

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void checkRoofBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerLevel level = ip_getActualWorld();
        
        // FIX: Cast 'level' to the interface to access the mixin-added method
        dimensionRoofData roofData = ((ServerLevelDataAccessor) level).getRoofData();

        boolean allowed = roofData.ValidateAbyAscBlockBreak(pos, player);

        if (!allowed) {
            ServerMessages.pickaxeWeak(player, pos);

            cir.setReturnValue(false); // cancels the method, returns false
        }

    }

    private static final Logger LOGGER = LogUtils.getLogger();

    //THIS FUNCTION DOES WORK AS INTENDED BUT THERE IS ANOTHER CHECK PREVENTING THE BLOCK FROM BREAKING.
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
        return ip_getActualWorld();
    }
}
