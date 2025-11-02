package com.jkdr.abyssalascentdimensionpatcher.util;

import com.jkdr.abyssalascentdimensionpatcher.util.ModInternalConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ServerMessages {
    public static final SoundEvent BLOCKBREAKSOUND = ForgeRegistries.SOUND_EVENTS.getValue(ModInternalConfig.blockBreakFailSoundLocation);
    private static Optional<Holder<SoundEvent>> optionalHolder = ForgeRegistries.SOUND_EVENTS.getHolder(BLOCKBREAKSOUND);

    public static void welcome(ServerPlayer player) {
        player.sendSystemMessage(FormattingCore.serverTranslate(
            "message.abyssalascentdimensionpatcher.welcome", 
            FormattingCore.createPrefixWithFormatting(),
            ModInternalConfig.DISCORD_CMD, 
            FormattingCore.createCommandClickableComponent(ModInternalConfig.DISCORD_CMD))
        );
    }

    public static void discord(ServerPlayer player) {
        player.sendSystemMessage(FormattingCore.serverTranslate(
            "message.abyssalascentdimensionpatcher.discordnotice",
            FormattingCore.createPrefixWithFormatting(),
            FormattingCore.createExternalLinkClickableComponent(ModInternalConfig.DISCORD_INVITE, true))
        );
    }

    public static void spawnStructureNew(ServerPlayer player, BlockPos pos) {
        player.sendSystemMessage(FormattingCore.serverTranslate(
            "message.abyssalascentdimensionpatcher.structurepos",
            FormattingCore.createPrefixWithFormatting(),
            pos.getX(),
            pos.getY(),
            pos.getZ()
        ));
    }

    public static void pickaxeWeak(ServerPlayer player, BlockPos sourceBlock) {
        //Play the sound at the block to everyone to give more feedback the block cannot be broken
        if (BLOCKBREAKSOUND != null) {
            player.level().playSound(
                null, // null = broadcast to all players nearby
                sourceBlock.getX() + 0.5,
                sourceBlock.getY() + 0.5,
                sourceBlock.getZ() + 0.5,
                BLOCKBREAKSOUND,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
            );
        }

        player.sendSystemMessage(FormattingCore.serverTranslate("message.abyssalascentdimensionpatcher.failedBlockBreak", ChatFormatting.RED), true);
    }

    public static void invalidSourceMine(Level level, BlockPos sourceBlock) {
        //Play the sound at the block to everyone to give more feedback the block cannot be broken
        if (BLOCKBREAKSOUND != null) {
            level.playSound(
                null, // null = broadcast to all players nearby
                sourceBlock.getX() + 0.5,
                sourceBlock.getY() + 0.5,
                sourceBlock.getZ() + 0.5,
                BLOCKBREAKSOUND,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
            );
        }
    }
}