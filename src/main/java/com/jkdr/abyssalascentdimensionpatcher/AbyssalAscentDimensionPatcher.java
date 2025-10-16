package com.jkdr.abyssalascentdimensionpatcher;

import com.jkdr.abyssalascentdimensionpatcher.mixins.MixinPlayerRespawnLogic;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.locale.Language;

// World & Dimension
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;

// Player & Entity
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

// Items & NBT Data
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

// Chat Components & Formatting (for messages)
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraftforge.event.RegisterCommandsEvent;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.level.Level;

import com.jkdr.abyssalascentdimensionpatcher.PatchouliBookManager;
import com.jkdr.abyssalascentdimensionpatcher.AACOREConfigValues;


@Mod(AbyssalAscentDimensionPatcher.MOD_ID)
public class AbyssalAscentDimensionPatcher {
    public static final String MOD_ID = "abyssalascentdimensionpatcher";
     private static final Logger LOGGER = LogUtils.getLogger();

    
    

    public AbyssalAscentDimensionPatcher() {
        MinecraftForge.EVENT_BUS.register(this);
        
    }

    private static void sendWelcomeMessage(ServerPlayer player) {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + AACOREConfigValues.DISCORD_CMD);

        MutableComponent prefix = Component.literal(AACOREConfigValues.ABYSSAL_ASCENT_OWNER_PREFIX).withStyle(ChatFormatting.GRAY);
        MutableComponent discordButton = Component.literal("[Click Here]")
            .withStyle(style -> style.withClickEvent(clickEvent)
            .withColor(ChatFormatting.BLUE)
            .withUnderlined(true));

        player.sendSystemMessage(AbyssalAscentDimensionPatcher.serverTranslate("message.abyssalascentdimensionpatcher.welcome", prefix, AACOREConfigValues.DISCORD_CMD, discordButton));
    }

    @SubscribeEvent
    public void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        CompoundTag persistent = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

        MinecraftServer server = player.getServer();
        if (server == null) return;

        


        if (persistent.getBoolean(AACOREConfigValues.FIRST_JOIN_TAG)) {
            //We have already init the player so dont worry.
            return;
        }

        //First time player is joining the world since we added this update so we will send the message either way
        sendWelcomeMessage(player);
        
        //We should check if the player has already a set spawnpoint, if they do we don't need to worry about setting their spawnpoint (from a bed etc)
        //we should check the position AND level beacuse in the weird situation where the player is located out of the undergarden and does not have their spawn set the player will be teleported back to world spawn.
        //This is purely just for compatibility for other ABYASC versions.
        boolean currentSpawnpointSet = (player.getRespawnPosition() != null) && (player.getRespawnDimension() == Level.OVERWORLD);
        if (currentSpawnpointSet) {
            return;
        }
        persistent.putBoolean(AACOREConfigValues.FIRST_JOIN_TAG, true);
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persistent);

        ServerLevel undergardenLevel = server.getLevel(AACOREConfigValues.playerSpawnDimension);
        if (undergardenLevel == null) {
            LOGGER.error("The Undergarden dimension was not found! Cannot set spawn. Is The Undergarden mod installed?");
            return;
        }

        //We don't bother to check since we know this advancement will always exist
        ResourceLocation netherAdvancement = new ResourceLocation("minecraft", "nether/root");
        Advancement advancement = player.getServer().getAdvancements().getAdvancement(netherAdvancement);
        AdvancementProgress advancementProgress = player.getAdvancements().getOrStartProgress(advancement);

        // Teleport the player to the Undergarden's spawn point.
        BlockPos nonFudgedSpawn = findSafeSpawnLocation(undergardenLevel, undergardenLevel.getSharedSpawnPos(), 90);
        BlockPos spawnPos = MixinPlayerRespawnLogic.invokeGetOverworldRespawnPos(undergardenLevel, nonFudgedSpawn.getX(), nonFudgedSpawn.getZ());
        PatchouliBookManager.givePlayerPatchouliBook(player);

        //We ONLY teleport the player if they have never entered the nether before (justafiable since the previous if statement checks if the current spawnpoint is set and player is in the overworld)
        if (!advancementProgress.isDone()) { player.teleportTo(undergardenLevel, nonFudgedSpawn.getX() + 0.5, nonFudgedSpawn.getY(), nonFudgedSpawn.getZ() + 0.5, player.getYRot(), player.getXRot()); }
        //Setting the "forced" parameter to false ensures the spawnpoint does not get override a bed (the 4th argument)
        player.setRespawnPosition(AACOREConfigValues.playerSpawnDimension, spawnPos, 0, false, false);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        BlockPos respawnPos = player.getRespawnPosition();

        boolean isBedSpawn = (respawnPos != null);
        LOGGER.info("respawn pos: {}", respawnPos);

        PatchouliBookManager.givePlayerPatchouliBook(player);

        if (isBedSpawn) {
            return;
        }

        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerLevel undergardenLevel = server.getLevel(AACOREConfigValues.playerSpawnDimension);

        if (undergardenLevel == null) {
            LOGGER.error("The Undergarden dimension was not found! Cannot handle respawn.");
            return;
        }

        //Just incase the player is in the undergarden we dont want to do another meaningless call
        if (player.level().dimension().equals(AACOREConfigValues.playerSpawnDimension)) {
            return;
        }
        
        BlockPos nonFudgedSpawn = findSafeSpawnLocation(undergardenLevel, undergardenLevel.getSharedSpawnPos(), 90);
        BlockPos spawnPos = MixinPlayerRespawnLogic.invokeGetOverworldRespawnPos(undergardenLevel, nonFudgedSpawn.getX(), nonFudgedSpawn.getZ());
        player.teleportTo(undergardenLevel, nonFudgedSpawn.getX() + 0.5, nonFudgedSpawn.getY(), nonFudgedSpawn.getZ() + 0.5, player.getYRot(), player.getXRot());
        
    }

    private static BlockPos findSafeSpawnLocation(ServerLevel level, BlockPos startPos, int topY) {

        for (int y = topY; y > level.getMinBuildHeight(); --y) {
            BlockPos currentPos = new BlockPos(startPos.getX(), y, startPos.getZ());
            if (!level.getBlockState(currentPos).isAir()) {
                BlockPos spawnPos = currentPos.above();
                if (level.getBlockState(spawnPos).isAir() && level.getBlockState(spawnPos.above()).isAir()) {
                    return spawnPos;
                }
            }
        }
        // Fallback if no safe spot is found, though unlikely
        return null;
    }

    public static Component serverTranslate(String key, Object... args) {
        // Get the translation string
        String raw = Language.getInstance().getOrDefault(key);

        // Split the string by %s so we can inject components
        String[] parts = raw.split("%s", -1);

        // Start building the result component
        MutableComponent result = Component.literal(parts[0]);

        for (int i = 0; i < args.length; i++) {
            // Append the argument (if it's a Component, append directly; else wrap as literal)
            Object arg = args[i];
            if (arg instanceof Component c) {
                result.append(c);
            } else {
                result.append(Component.literal(arg.toString()));
            }

            // Append the next part of the translation string (if exists)
            if (i + 1 < parts.length) {
                result.append(Component.literal(parts[i + 1]));
            }
        }

        return result;
    }
}
