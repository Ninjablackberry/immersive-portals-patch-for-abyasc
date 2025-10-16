package com.jkdr.abyssalascentdimensionpatcher.commands;

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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import com.jkdr.abyssalascentdimensionpatcher.PatchouliBookManager;
import com.jkdr.abyssalascentdimensionpatcher.AACOREConfigValues;
import com.jkdr.abyssalascentdimensionpatcher.AbyssalAscentDimensionPatcher;


@Mod.EventBusSubscriber(modid = AbyssalAscentDimensionPatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DiscordLink {
	@SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal(AACOREConfigValues.DISCORD_CMD)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, AACOREConfigValues.DISCORD_INVITE);

                    MutableComponent prefix = Component.literal(AACOREConfigValues.ABYSSAL_ASCENT_OWNER_PREFIX).withStyle(ChatFormatting.GRAY);
                    MutableComponent discordInviteLink = Component.literal(AACOREConfigValues.DISCORD_INVITE)
                        .withStyle(style -> style.withClickEvent(clickEvent)
                        .withColor(ChatFormatting.BLUE)
                        .withUnderlined(true));

                    player.sendSystemMessage(AbyssalAscentDimensionPatcher.serverTranslate("message.abyssalascentdimensionpatcher.discordnotice", prefix, discordInviteLink));
                    return 1;
                })
        );
    }
}
