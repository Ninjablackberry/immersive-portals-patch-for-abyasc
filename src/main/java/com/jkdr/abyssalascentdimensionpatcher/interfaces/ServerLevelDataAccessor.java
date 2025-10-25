package com.jkdr.abyssalascentdimensionpatcher.interfaces;

import com.jkdr.abyssalascentdimensionpatcher.data.dimensionRoofData;

/**
 * This interface allows other mixins to access the custom data
 * we are adding to the ServerLevel class via MixinServerLevel.
 */
public interface ServerLevelDataAccessor {
    dimensionRoofData getRoofData();
}