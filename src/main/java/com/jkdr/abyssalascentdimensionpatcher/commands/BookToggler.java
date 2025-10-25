package com.jkdr.abyssalascentdimensionpatcher.commands;

import com.jkdr.abyssalascentdimensionpatcher.AbyssalAscentDimensionPatcher;
import com.jkdr.abyssalascentdimensionpatcher.util.ModInternalConfig;
import com.jkdr.abyssalascentdimensionpatcher.util.ServerMessages;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AbyssalAscentDimensionPatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BookToggler {
	@SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal(ModInternalConfig.BOOK_TOGGLE_CMD)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    toggleGuideTag(player);
                    return 1;
                })
        );
    }

    //Method for flipping the player tag (the book enabled condition)
    private static void toggleGuideTag(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

        boolean isDisabled = persistent.getBoolean(ModInternalConfig.DISABLE_GUIDE_TAG);
        persistent.putBoolean(ModInternalConfig.DISABLE_GUIDE_TAG, !isDisabled);

        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persistent);

        // Send feedback message based on the NEW state
        if (!isDisabled) { // It is now disabled
            ServerMessages.bookNowDisabled(player);
        } else { // It is now enabled
            ServerMessages.bookNowEnabled(player);
        }
    }
}