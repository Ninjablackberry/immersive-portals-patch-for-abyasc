package com.jkdr.abyssalascentdimensionpatcher.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Vec3i;

import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class StructureSpawning {

    /**
     * Spawns a structure at the given position in the world, removing water blocks first.
     *
     * @param level The ServerLevel to spawn the structure in.
     * @param pos   The BlockPos where the structure should be placed.
     * @return true if the structure was successfully placed, false otherwise.
     */
    public static boolean createSpawnStructure(ServerLevel level, BlockPos pos) {

        ResourceLocation structureRL = new ResourceLocation("abyssalascentdimensionpatcher", "abyssal_spawn_structure");
        StructureTemplateManager templateManager = level.getStructureManager();
        Optional<StructureTemplate> optionalTemplate = templateManager.get(structureRL);

        if (optionalTemplate.isPresent()) {
            StructureTemplate template = optionalTemplate.get();

            // Configure placement settings
            StructurePlaceSettings placementSettings = new StructurePlaceSettings()
                    .setRotation(Rotation.NONE)
                    .setMirror(Mirror.NONE)
                    .setIgnoreEntities(false);

            Vec3i templateSize = template.getSize(); // size of the structure

            for (int x = 0; x < templateSize.getX(); x++) {
                for (int y = 0; y < templateSize.getY(); y++) {
                    for (int z = 0; z < templateSize.getZ(); z++) {
                        BlockPos checkPos = pos.offset(x, y, z);
                        if (level.getBlockState(checkPos).getBlock() == Blocks.WATER) {
                            level.setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }


            // Place the structure
            return template.placeInWorld(level, pos, pos, placementSettings, level.getRandom(), 2);

        } else {
            // Could not find structure
            return false;
        }
    }
}
