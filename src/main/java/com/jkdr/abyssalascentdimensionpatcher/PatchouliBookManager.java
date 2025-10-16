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

import com.jkdr.abyssalascentdimensionpatcher.AACOREConfigValues;
import com.jkdr.abyssalascentdimensionpatcher.AbyssalAscentDimensionPatcher;



public class PatchouliBookManager {
     private static final Logger LOGGER = LogUtils.getLogger();

    

    public static void givePlayerPatchouliBook(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag persistentTag = playerData.getCompound(Player.PERSISTED_NBT_TAG);

        if (!persistentTag.getBoolean(AACOREConfigValues.DISABLE_GUIDE_TAG)) {
            ResourceLocation bookId = new ResourceLocation("patchouli", "guide_book");
            Item guideBookItem = ForgeRegistries.ITEMS.getValue(bookId);
            if (guideBookItem != null && guideBookItem != Items.AIR) {
                ItemStack guideBookStack = new ItemStack(guideBookItem);

                CompoundTag bookNbt = new CompoundTag();
                bookNbt.putString("patchouli:book", "patchouli:aa_guide_book");
                guideBookStack.setTag(bookNbt);

                player.getInventory().add(guideBookStack);
                LOGGER.info("Successfully gave Patchouli guide book to {}.", player.getName().getString());

                ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + AACOREConfigValues.BOOK_TOGGLE_CMD);

                MutableComponent prefix = Component.literal(AACOREConfigValues.ABYSSAL_ASCENT_OWNER_PREFIX).withStyle(ChatFormatting.GRAY);
                MutableComponent toggleGuideButton = Component.literal("[Click Here]")
                    .withStyle(style -> style.withClickEvent(clickEvent)
                    .withColor(ChatFormatting.GREEN)
                    .withUnderlined(true));
                 
                player.sendSystemMessage(AbyssalAscentDimensionPatcher.serverTranslate("message.abyssalascentdimensionpatcher.bookenabled", prefix, AACOREConfigValues.BOOK_TOGGLE_CMD, toggleGuideButton));
            } else {
                LOGGER.warn("Could not find item 'patchouli:guide_book'. Is Patchouli installed?");
            }
        } else {
            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + AACOREConfigValues.BOOK_TOGGLE_CMD);

            MutableComponent prefix = Component.literal(AACOREConfigValues.ABYSSAL_ASCENT_OWNER_PREFIX).withStyle(ChatFormatting.GRAY);
            MutableComponent toggleGuideButton = Component.literal("[Click Here]")
                .withStyle(style -> style.withClickEvent(clickEvent)
                .withColor(ChatFormatting.GREEN)
                .withUnderlined(true));

            player.sendSystemMessage(AbyssalAscentDimensionPatcher.serverTranslate("message.abyssalascentdimensionpatcher.bookdisabled", prefix, AACOREConfigValues.BOOK_TOGGLE_CMD, toggleGuideButton));
        }
    }
}
