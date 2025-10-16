
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

// World & Dimension
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.PlayerRespawnLogic;

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


public class AACOREConfigValues {
    public static final String BOOK_TOGGLE_CMD = "guide";
    public static final String DISCORD_CMD = "discord";
    public static final String DISCORD_INVITE = "https://discord.com/invite/Z35BDKFEXu";
    public static final String ABYSSAL_ASCENT_OWNER_PREFIX = "<Abyssal Ascent>";

    //After releasing this mod DO NOT change these values (as there really is no need and it would reset player saves)
    public static final String FIRST_JOIN_TAG = "abyssal_ascent_first_join";
    public static final String DISABLE_GUIDE_TAG = "abyssal_ascent_disable_helpbook";

     //How the game interprets dimensions so the overworld is (minecraft, overworld), undergarden is (undergarden, undergarden)
     //Basically undergarden:undergarden
    public static final ResourceKey<Level> playerSpawnDimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("undergarden","undergarden"));
}
