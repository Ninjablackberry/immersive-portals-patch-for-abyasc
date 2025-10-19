package com.jkdr.abyssalascentdimensionpatcher.eventHooks;

import com.jkdr.abyssalascentdimensionpatcher.AbyssalAscentDimensionPatcher;
import com.jkdr.abyssalascentdimensionpatcher.data.SpawnPosData;
import com.jkdr.abyssalascentdimensionpatcher.util.ModInternalConfig;
import com.jkdr.abyssalascentdimensionpatcher.util.PatchouliBookManager;
import com.jkdr.abyssalascentdimensionpatcher.util.ServerMessages;
import com.jkdr.abyssalascentdimensionpatcher.util.StructureSpawning;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.fml.common.Mod;
import com.google.common.collect.ImmutableSet;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = AbyssalAscentDimensionPatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class playerSpawnEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static Set<Block> VALID_GROUND_BLOCKS = null;

    private static final ResourceKey<Biome> FORCESPAWNINBIOME = ResourceKey.create(
            Registries.BIOME, new ResourceLocation("undergarden", "gronglegrowth")
    );

    private static Set<Block> getValidGroundBlocks() {
        // Check if the set hasn't been created yet
        if (VALID_GROUND_BLOCKS == null) {
            // If not, build it now. At this point in the game, the registries are guaranteed to be full.
            VALID_GROUND_BLOCKS = ImmutableSet.of(
                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("undergarden", "deepturf_block")),
                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("undergarden", "frozen_deepturf_block")),
                ForgeRegistries.BLOCKS.getValue(new ResourceLocation("undergarden", "deepsoil"))
            ).stream()
             .filter(Objects::nonNull).filter(block -> block != Blocks.AIR) // Still good practice to keep this filter
             .collect(ImmutableSet.toImmutableSet());
            LOGGER.info("Built the valid ground blocks set. Found {} blocks.", VALID_GROUND_BLOCKS.size());
        }
        return VALID_GROUND_BLOCKS;
    }

    @SubscribeEvent
    public static void onPlayerFirstJoin(PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerLevel undergardenLevel = server.getLevel(ModInternalConfig.playerSpawnDimension);
        if (undergardenLevel == null) {
            LOGGER.error("Undergarden dimension not found! Cannot set spawn.");
            return;
        }

        spawnStructure(player, undergardenLevel);

        // Persistent data
        var persistent = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        if (!persistent.getBoolean(ModInternalConfig.FIRST_JOIN_TAG)) {
            persistent.putBoolean(ModInternalConfig.FIRST_JOIN_TAG, true);
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persistent);
            ServerMessages.welcome(player);
        }

        PatchouliBookManager.givePlayerPatchouliBook(player);

        // Only teleport if Overworld and first time
        if (player.getRespawnPosition() == null) {
            BlockPos rawPos = getSpawnCoordinates(undergardenLevel);
            BlockPos adjustedPos = rawPos.offset(-5, 1, 3);

            player.teleportTo(
                    undergardenLevel,
                    adjustedPos.getX() + 0.5,
                    adjustedPos.getY(),
                    adjustedPos.getZ() + 0.5,
                    player.getYRot(),
                    player.getXRot()
            );
            player.setRespawnPosition(ModInternalConfig.playerSpawnDimension, adjustedPos, 0, false, false);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.isEndConquered()) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        PatchouliBookManager.givePlayerPatchouliBook(player);

        if (player.getRespawnPosition() != null || player.level().dimension() != Level.OVERWORLD) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerLevel undergardenLevel = server.getLevel(ModInternalConfig.playerSpawnDimension);
        if (undergardenLevel == null) return;

        if (player.level() == undergardenLevel) return;

        BlockPos rawPos = getSpawnCoordinates(undergardenLevel);
        BlockPos adjustedPos = rawPos.offset(-5, 1, 3);

        player.teleportTo(
                undergardenLevel,
                adjustedPos.getX() + 0.5,
                adjustedPos.getY(),
                adjustedPos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot()
        );
    }

    private static void spawnStructure(ServerPlayer player, ServerLevel level) {
        SpawnPosData spawnData = SpawnPosData.get(level);
        if (spawnData.getSpawnPos() != null) return;

        LOGGER.info("Building spawn structure, didn't exist before");
        BlockPos spawnBlockData = getSpawnCoordinates(level);
        BlockPos offsetStructure = spawnBlockData.offset(-7, -3, -1);

        LOGGER.info("Structure offset: {}, ORIGINAL {}", offsetStructure, spawnBlockData);
        StructureSpawning.createSpawnStructure(level, offsetStructure);
    }

    public static BlockPos getSpawnCoordinates(ServerLevel level) {
        SpawnPosData spawnData = SpawnPosData.get(level);
        BlockPos customSpawnPos = spawnData.getSpawnPos();

        if (customSpawnPos == null) {
            

            //Create new spawnpoint if it couldnt be found.
            BlockPos bedCompatibleSpawn = level.getSharedSpawnPos();
            
            Predicate<Holder<Biome>> biomePredicate = (biomeHolder) -> biomeHolder.is(FORCESPAWNINBIOME);
            BiomeSource biomeSource = level.getChunkSource().getGenerator().getBiomeSource();
            RandomState randomState = level.getChunkSource().randomState();

            Climate.Sampler sampler = randomState.sampler();
            RandomSource random = level.getRandom();
            int searchRadiusInQuarts = 3200 / 4;

            // Search for the closest biome using the correct method on the BiomeSource.
            Pair<BlockPos, Holder<Biome>> biomeResult = biomeSource.findBiomeHorizontal(
                bedCompatibleSpawn.getX() / 4,
                bedCompatibleSpawn.getY(),
                bedCompatibleSpawn.getZ() / 4,
                searchRadiusInQuarts,
                biomePredicate,
                random, // The required RandomSource
                sampler // The required Climate.Sampler
             );

            if (biomeResult != null) {
                bedCompatibleSpawn = biomeResult.getFirst();
            }

            BlockPos finalSpawnPos = findSafeSpotNearby(level, bedCompatibleSpawn, 16);
            spawnData.setSpawnPos(finalSpawnPos);

            return finalSpawnPos; 
        }

        return customSpawnPos;
    }

    public static BlockPos findSafeSpotNearby(ServerLevel level, BlockPos initialPos, int radiusChunks) {
        int baseX = initialPos.getX();
        int baseZ = initialPos.getZ();

        //Loop in the selected radius of chunks.
        for (int dx = -radiusChunks; dx <= radiusChunks; dx++) {
            for (int dz = -radiusChunks; dz <= radiusChunks; dz++) {
                int chunkX = (baseX >> 4) + dx;
                int chunkZ = (baseZ >> 4) + dz;

                //Load the chunk so block data can be accessed
                ChunkAccess chunk = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);
                if (chunk == null) continue;

                int x = (chunkX << 4) + 8;
                int z = (chunkZ << 4) + 8;

                BlockPos candidate = findValidSpawn(level, new BlockPos(x, level.getMaxBuildHeight() - 15, z));
                if (candidate != null) return candidate;
            }
        }

        LOGGER.warn("No safe spawn found nearby, falling back to initialPos, NOT RECOMMENDED (CAN SPAWN IN WALLS)");
        return initialPos;
    }

    public static BlockPos findValidSpawn(ServerLevel level, BlockPos pos) {
        int x = pos.getX(), z = pos.getZ();
        int airLength = 0;
        BlockPos lastRecordedAir = null;
        //Get a rough method to check if the spawn is valid
        for (int y = pos.getY(); y > level.getMinBuildHeight() + 30; y -= 4) {
            BlockPos current = new BlockPos(x, y, z);

            //If the block is not air we reset the count (need atleast 3 times in a row)
            if (!level.getBlockState(current).isAir()) {
                airLength = 0;
            }

            airLength++;
            
            if (airLength < 3) {continue;}
            //If the check passed 3 times in a row then we continue

            //Now we check if the block is not solid and if it isnt we add it to recorded air.
            if (!level.getBlockState(pos).isSolid()) {
                lastRecordedAir = current;
                continue;
            }

            break;
            
        }

        //Switch to a more precise method
        if (lastRecordedAir != null) {
            int maxSearchDown = 0;
            //Search from the last recorded air position to Y level 30 (undergarden level)
            for (int y = lastRecordedAir.getY(); y > level.getMinBuildHeight() + 30; y--) {
                BlockPos current = new BlockPos(x, y, z);
                BlockPos below = current.below();

                //Find the current block and check if its air (if so keep looping)
                if (!level.getBlockState(current).isSolid()) {continue;}
                //Add to the maximun searching depth (after finding a block so we dont keep doing checks which could slow down the check)
                maxSearchDown++;

                Block currentBlock = level.getBlockState(current).getBlock();
                //Gets the valid block list and checks if the block can actually be spawned on
                boolean isSpawnableBlock = getValidGroundBlocks().contains(currentBlock);
                if (isSpawnableBlock) {return current;}
                if (maxSearchDown >= 3) {break;}
            }
        }
        return null;
    }
}
