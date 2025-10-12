package com.jkdr.abyssalascentdimensionpatcher.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.phys.Vec3;
import com.jkdr.abyssalascentdimensionpatcher.AbyssalAscentDimensionPatcher;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.ForgeEventFactory;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.IMagicManager;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import qouteall.imm_ptl.core.portal.Portal;
import java.util.UUID;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mixin(value = qouteall.imm_ptl.core.teleportation.ServerTeleportationManager.class, remap = false)
public abstract class MixinServerTeleportationManager {
      private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(
        method = "changePlayerDimension",
        at = @At("TAIL")
    )
    private void afterChangeDimensions(ServerPlayer player, ServerLevel fromWorld, ServerLevel toWorld, Vec3 newEyePos, CallbackInfo ci) {
            player.invalidateCaps();
            player.reviveCaps();

    }

}