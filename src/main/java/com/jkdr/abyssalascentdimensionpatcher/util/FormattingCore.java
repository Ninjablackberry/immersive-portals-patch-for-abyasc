package com.jkdr.abyssalascentdimensionpatcher.util;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;

public class FormattingCore {
    public static final String MOD_ID = "abyssalascentdimensionpatcher";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static MutableComponent createCommandClickableComponent(String command) {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command);

        return Component.literal(ModInternalConfig.CMD_LINK_TEXT) // Using constant
            .withStyle(style -> style.withClickEvent(clickEvent)
            .withColor(ChatFormatting.GREEN)
            .withUnderlined(true));
    }
    
    public static MutableComponent createExternalLinkClickableComponent(String url, Boolean displayRawLink) {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        String url_Text = ModInternalConfig.URL_LINK_TEXT;
        if (displayRawLink) {url_Text = url;}

        return Component.literal(url_Text) // Using constant
            .withStyle(style -> style.withClickEvent(clickEvent)
            .withColor(ChatFormatting.BLUE)
            .withUnderlined(true));
    }

    public static MutableComponent createPrefixWithFormatting() {
        return Component.literal(ModInternalConfig.ABYSSAL_ASCENT_OWNER_PREFIX).withStyle(ChatFormatting.GRAY);
    }

    // Add a new overloaded method that accepts ChatFormatting
    public static Component serverTranslate(String key, ChatFormatting format, Object... args) {
        // Get the translation string
        String raw = Language.getInstance().getOrDefault(key);
        String[] parts = raw.split("%s", -1);

        MutableComponent result = Component.literal("");

        for (int i = 0; i < parts.length; i++) {
            // Apply the desired formatting to the static text parts
            result.append(Component.literal(parts[i]).withStyle(format));
        
            // Append the argument if it exists
            if (i < args.length) {
                Object arg = args[i];
                if (arg instanceof Component c) {
                    result.append(c); // Arguments with their own style are preserved
                } else {
                    // Also apply the format to plain string arguments
                    result.append(Component.literal(arg.toString()).withStyle(format));
                }
            }
        }
        return result;
    }

    // Keep your original method for translations that don't need special color
    public static Component serverTranslate(String key, Object... args) {
        // Just call the other method with a default color like GRAY or WHITE
        return serverTranslate(key, ChatFormatting.WHITE, args);
    }
}