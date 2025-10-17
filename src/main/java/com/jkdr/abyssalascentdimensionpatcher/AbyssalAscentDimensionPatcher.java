package com.jkdr.abyssalascentdimensionpatcher;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AbyssalAscentDimensionPatcher.MOD_ID)
public class AbyssalAscentDimensionPatcher {
    public static final String MOD_ID = "abyssalascentdimensionpatcher";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AbyssalAscentDimensionPatcher() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
