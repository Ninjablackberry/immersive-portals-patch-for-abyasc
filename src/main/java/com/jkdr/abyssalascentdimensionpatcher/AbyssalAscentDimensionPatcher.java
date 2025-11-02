package com.jkdr.abyssalascentdimensionpatcher;

import com.jkdr.abyssalascentdimensionpatcher.config.IronSpellbookExpandedConfig;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;

@Mod(AbyssalAscentDimensionPatcher.MOD_ID)
public class AbyssalAscentDimensionPatcher {
    public static final String MOD_ID = "abyssalascentdimensionpatcher";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceKey<Level> CAVE_DIMENSION =
    ResourceKey.create(Registries.DIMENSION, new ResourceLocation("dimension_of_caves", "cave"));

    public static final ThreadLocal<Boolean> IS_PLAYER_BREAKING = ThreadLocal.withInitial(() -> false);

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