package com.jkdr.abyssalascentdimensionpatcher;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(AbyssalAscentDimensionPatcher.MOD_ID)
public class AbyssalAscentDimensionPatcher {
    public static final String MOD_ID = "abyssalascentdimensionpatcher";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AbyssalAscentDimensionPatcher() {
        if (FMLEnvironment.dist.isClient()) {
            return; // Stop loading the mod right here
        }
        MinecraftForge.EVENT_BUS.register(this);
    }
}