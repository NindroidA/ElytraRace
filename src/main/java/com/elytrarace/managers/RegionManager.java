/*
 * Copyright (c) 2025 Kartik Fulara
 *
 * This file is part of ElytraRace.
 *
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.managers;

import com.elytrarace.ElytraRacePlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class RegionManager {

    private final ElytraRacePlugin plugin;
    private final Map<RegionType, CachedRegion> regionCache = new EnumMap<>(RegionType.class);

    public enum RegionType {
        START,
        FINISH
    }

    public RegionManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        loadRegionCache();
    }

    /**
     * Load all regions from config into cache.
     */
    private void loadRegionCache() {
        regionCache.clear();
        for (RegionType type : RegionType.values()) {
            ConfigurationSection sec = plugin.getConfig().getConfigurationSection("regions." + type.name().toLowerCase());
            if (sec == null) continue;

            String worldName = sec.getString("world");
            if (worldName == null) continue;

            World world = plugin.getServer().getWorld(worldName);
            if (world == null) continue;

            regionCache.put(type, new CachedRegion(
                world,
                sec.getDouble("min.x"), sec.getDouble("min.y"), sec.getDouble("min.z"),
                sec.getDouble("max.x"), sec.getDouble("max.y"), sec.getDouble("max.z")
            ));
        }
    }

    /**
     * Check if player is inside a cuboid region (uses cached bounds).
     */
    public boolean isInsideRegion(Player player, RegionType type) {
        CachedRegion region = regionCache.get(type);
        if (region == null) return false;

        Location loc = player.getLocation();
        if (!loc.getWorld().equals(region.world)) return false;

        return loc.getX() >= region.minX && loc.getX() <= region.maxX &&
               loc.getY() >= region.minY && loc.getY() <= region.maxY &&
               loc.getZ() >= region.minZ && loc.getZ() <= region.maxZ;
    }

    /**
     * Get the center location of a region.
     */
    public Location getRegionCenter(RegionType type) {
        CachedRegion region = regionCache.get(type);
        if (region == null) return null;

        return new Location(region.world,
            (region.minX + region.maxX) / 2.0,
            (region.minY + region.maxY) / 2.0,
            (region.minZ + region.maxZ) / 2.0);
    }

    /**
     * Saves region using two corner locations and refreshes cache.
     */
    public void saveRegionFromSelection(Location min, Location max, RegionType type) {
        String key = "regions." + type.name().toLowerCase();
        plugin.getConfig().set(key + ".world", min.getWorld().getName());
        plugin.getConfig().set(key + ".min.x", Math.min(min.getX(), max.getX()));
        plugin.getConfig().set(key + ".min.y", Math.min(min.getY(), max.getY()));
        plugin.getConfig().set(key + ".min.z", Math.min(min.getZ(), max.getZ()));
        plugin.getConfig().set(key + ".max.x", Math.max(min.getX(), max.getX()));
        plugin.getConfig().set(key + ".max.y", Math.max(min.getY(), max.getY()));
        plugin.getConfig().set(key + ".max.z", Math.max(min.getZ(), max.getZ()));
        plugin.saveConfig();
        loadRegionCache();
    }

    /**
     * Checks if region exists in config.
     */
    public boolean regionExists(RegionType type) {
        return regionCache.containsKey(type);
    }

    private static class CachedRegion {
        final World world;
        final double minX, minY, minZ;
        final double maxX, maxY, maxZ;

        CachedRegion(World world, double minX, double minY, double minZ,
                     double maxX, double maxY, double maxZ) {
            this.world = world;
            this.minX = minX; this.minY = minY; this.minZ = minZ;
            this.maxX = maxX; this.maxY = maxY; this.maxZ = maxZ;
        }
    }
}
