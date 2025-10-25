package com.jkdr.abyssalascentdimensionpatcher;

import com.jkdr.abyssalascentdimensionpatcher.config.IronSpellbookExpandedConfig;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@Mod(AbyssalAscentDimensionPatcher.MOD_ID)
public class AbyssalAscentDimensionPatcher {
    public static final String MOD_ID = "abyssalascentdimensionpatcher";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AbyssalAscentDimensionPatcher() {
        ModLoadingContext.get().registerConfig(
            ModConfig.Type.COMMON, // The config type (COMMON, CLIENT, or SERVER)
            IronSpellbookExpandedConfig.SPEC,       // Your ForgeConfigSpec from the other class
            "irons-spells-config-expansion.toml"    // The name of the config file
        );

        if (FMLEnvironment.dist.isClient()) {
            return; // Stop loading the mod right here
        }
        MinecraftForge.EVENT_BUS.register(this);
        

    }
}