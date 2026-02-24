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
import com.elytrarace.data.RingDefinition;
import com.elytrarace.data.RingDefinition.Orientation;
import com.elytrarace.data.RingDefinition.RingType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import java.util.*;

public class ConfigManager {

    private final ElytraRacePlugin plugin;

    public ConfigManager(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        setupDefaults();
    }

    private void setupDefaults() {
        plugin.getConfig().addDefault("race.min-players", 2);
        plugin.getConfig().addDefault("race.max-players", 5);
        plugin.getConfig().addDefault("race.countdown-seconds", 5);
        plugin.getConfig().addDefault("race.ready-timeout-seconds", 120);
        plugin.getConfig().addDefault("race.max-time-minutes", 30);
        
        plugin.getConfig().addDefault("race.required-rockets", 64);
        plugin.getConfig().addDefault("race.max-rocket-uses", 3);
        plugin.getConfig().addDefault("race.auto-finish-time", 180);
        
        // NEW: Feature 3 - Region import settings
        plugin.getConfig().addDefault("region-import.enabled", true);
        plugin.getConfig().addDefault("region-import.prefix", "ring");
        plugin.getConfig().addDefault("region-import.auto-detect", true);
        
        // NEW: Feature 9 - Anti-cheat boundary
        plugin.getConfig().addDefault("anti-cheat.boundary-distance", 50);
        plugin.getConfig().addDefault("anti-cheat.teleport-on-exceed", true);
        plugin.getConfig().addDefault("anti-cheat.warnings-before-teleport", 3);
        
        // NEW: Feature 7 - Auto-spectator
        plugin.getConfig().addDefault("spectator.auto-enable", true);
        plugin.getConfig().addDefault("spectator.return-to-lobby", true);
        plugin.getConfig().addDefault("spectator.delay-seconds", 3);
        
        // NEW: Feature 4 - Starting platform
        plugin.getConfig().addDefault("starting-platform.enabled", true);
        plugin.getConfig().addDefault("starting-platform.material", "GLASS");
        plugin.getConfig().addDefault("starting-platform.size", 3);
        plugin.getConfig().addDefault("starting-platform.height-offset", -1);
        
        // NEW: Feature 8 - Ring preview
        plugin.getConfig().addDefault("ring-preview.enabled", true);
        plugin.getConfig().addDefault("ring-preview.particle", "VILLAGER_HAPPY");
        plugin.getConfig().addDefault("ring-preview.particle-count", 20);

        // NEW: v1.4.0 - Ring system settings
        plugin.getConfig().addDefault("rings.default-orientation", "VERTICAL_NS");
        plugin.getConfig().addDefault("rings.enforce-order", true);
        plugin.getConfig().addDefault("rings.default-radius", 5.0);

        plugin.getConfig().addDefault("messages.prefix", "&6[ElytraRace] &f");
        plugin.getConfig().addDefault("messages.race-started", "&aThe race has started! Fly through all the rings!");
        plugin.getConfig().addDefault("messages.race-finished", "&aYou finished in &e{time}&a seconds!");
        plugin.getConfig().addDefault("messages.ring-passed", "&aRing &e{ring}&a passed! (&e{current}&a/&e{total}&a)");
        plugin.getConfig().addDefault("messages.ready-up", "&aYou are ready! Waiting for other players...");
        plugin.getConfig().addDefault("messages.player-ready", "&e{player}&a is ready! (&e{ready}&a/&e{total}&a)");
        plugin.getConfig().addDefault("messages.countdown", "&eRace starting in &c{seconds}&e seconds!");
        plugin.getConfig().addDefault("messages.not-enough-players", "&cNot enough players to start! Need at least {min} players.");
        plugin.getConfig().addDefault("messages.already-ready", "&cYou are already ready!");
        plugin.getConfig().addDefault("messages.lobby-full", "&cThe lobby is full!");
        plugin.getConfig().addDefault("messages.no-permission", "&cYou don't have permission to use this command!");
        
        // NEW: Additional messages
        plugin.getConfig().addDefault("messages.insufficient-rockets", "&cYou need {required} rockets to ready up! You have {current}.");
        plugin.getConfig().addDefault("messages.inventory-not-empty", "&cYour inventory must be empty (except armor) to race!");
        plugin.getConfig().addDefault("messages.no-elytra", "&cYou must have an elytra equipped to race!");
        plugin.getConfig().addDefault("messages.boundary-warning", "&c⚠ WARNING: You're going off-course! ({warnings}/3)");
        plugin.getConfig().addDefault("messages.teleported-to-checkpoint", "&cYou went too far off-course! Teleported to last checkpoint.");
        plugin.getConfig().addDefault("messages.auto-finish", "&eTime's up! Race automatically finished.");
        plugin.getConfig().addDefault("messages.force-joined", "&aYou have been force-joined to the race by an admin!");
        plugin.getConfig().addDefault("messages.test-mode-enabled", "&e⚠ Test mode enabled - stats will not be saved.");
        plugin.getConfig().addDefault("messages.dependencies-missing", "&c⚠ WorldEdit/WorldGuard not found. Region import disabled.");

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getMinPlayers() {
        return plugin.getConfig().getInt("race.min-players", 2);
    }

    public int getMaxPlayers() {
        return plugin.getConfig().getInt("race.max-players", 5);
    }

    public int getCountdownSeconds() {
        return plugin.getConfig().getInt("race.countdown-seconds", 5);
    }

    public int getReadyTimeoutSeconds() {
        return plugin.getConfig().getInt("race.ready-timeout-seconds", 120);
    }

    public int getMaxTimeMinutes() {
        return plugin.getConfig().getInt("race.max-time-minutes", 30);
    }
    
    public int getRequiredRockets() {
        return plugin.getConfig().getInt("race.required-rockets", 64);
    }

    public int getMaxRocketUses() {
        return plugin.getConfig().getInt("race.max-rocket-uses", 3);
    }

    public int getAutoFinishTime() {
        return plugin.getConfig().getInt("race.auto-finish-time", 180);
    }
    
    // NEW: Feature 3
    public boolean isRegionImportEnabled() {
        return plugin.getConfig().getBoolean("region-import.enabled", true);
    }
    
    public String getRegionPrefix() {
        return plugin.getConfig().getString("region-import.prefix", "ring");
    }
    
    // NEW: Feature 9
    public int getBoundaryDistance() {
        return plugin.getConfig().getInt("anti-cheat.boundary-distance", 50);
    }
    
    public boolean isTeleportOnExceed() {
        return plugin.getConfig().getBoolean("anti-cheat.teleport-on-exceed", true);
    }
    
    public int getWarningsBeforeTeleport() {
        return plugin.getConfig().getInt("anti-cheat.warnings-before-teleport", 3);
    }
    
    // NEW: Feature 7
    public boolean isAutoSpectatorEnabled() {
        return plugin.getConfig().getBoolean("spectator.auto-enable", true);
    }
    
    public boolean isReturnToLobby() {
        return plugin.getConfig().getBoolean("spectator.return-to-lobby", true);
    }
    
    public int getSpectatorDelay() {
        return plugin.getConfig().getInt("spectator.delay-seconds", 3);
    }
    
    // NEW: Feature 4
    public boolean isStartingPlatformEnabled() {
        return plugin.getConfig().getBoolean("starting-platform.enabled", true);
    }
    
    public String getPlatformMaterial() {
        return plugin.getConfig().getString("starting-platform.material", "GLASS");
    }
    
    public int getPlatformSize() {
        return plugin.getConfig().getInt("starting-platform.size", 3);
    }
    
    public int getPlatformHeightOffset() {
        return plugin.getConfig().getInt("starting-platform.height-offset", -1);
    }
    
    // NEW: Feature 8
    public boolean isRingPreviewEnabled() {
        return plugin.getConfig().getBoolean("ring-preview.enabled", true);
    }
    
    public String getPreviewParticle() {
        return plugin.getConfig().getString("ring-preview.particle", "VILLAGER_HAPPY");
    }
    
    public int getPreviewParticleCount() {
        return plugin.getConfig().getInt("ring-preview.particle-count", 20);
    }

    // NEW: v1.4.0 - Ring system getters
    public String getDefaultOrientation() {
        return plugin.getConfig().getString("rings.default-orientation", "VERTICAL_NS");
    }

    public boolean isRingOrderEnforced() {
        return plugin.getConfig().getBoolean("rings.enforce-order", true);
    }

    public double getDefaultRingRadius() {
        return plugin.getConfig().getDouble("rings.default-radius", 5.0);
    }

    public Location getLobbyLocation() {
        ConfigurationSection lobby = plugin.getConfig().getConfigurationSection("lobby");
        if (lobby == null) return null;

        String world = lobby.getString("world");
        double x = lobby.getDouble("x");
        double y = lobby.getDouble("y");
        double z = lobby.getDouble("z");
        float yaw = (float) lobby.getDouble("yaw", 0.0);
        float pitch = (float) lobby.getDouble("pitch", 0.0);

        return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    public void setLobbyLocation(Location loc) {
        plugin.getConfig().set("lobby.world", loc.getWorld().getName());
        plugin.getConfig().set("lobby.x", loc.getX());
        plugin.getConfig().set("lobby.y", loc.getY());
        plugin.getConfig().set("lobby.z", loc.getZ());
        plugin.getConfig().set("lobby.yaw", loc.getYaw());
        plugin.getConfig().set("lobby.pitch", loc.getPitch());
        plugin.saveConfig();
    }

    public Map<String, Location> getRingLocations() {
        Map<String, Location> rings = new LinkedHashMap<>();
        ConfigurationSection ringsSection = plugin.getConfig().getConfigurationSection("rings");

        if (ringsSection == null) return rings;

        for (String ringName : ringsSection.getKeys(false)) {
            ConfigurationSection ring = ringsSection.getConfigurationSection(ringName);
            if (ring == null) continue;

            String world = ring.getString("world");
            double x = ring.getDouble("x");
            double y = ring.getDouble("y");
            double z = ring.getDouble("z");

            rings.put(ringName, new Location(plugin.getServer().getWorld(world), x, y, z));
        }

        return rings;
    }

    public void addRing(String ringName, Location loc) {
        plugin.getConfig().set("rings." + ringName + ".world", loc.getWorld().getName());
        plugin.getConfig().set("rings." + ringName + ".x", loc.getX());
        plugin.getConfig().set("rings." + ringName + ".y", loc.getY());
        plugin.getConfig().set("rings." + ringName + ".z", loc.getZ());
        plugin.saveConfig();
    }

    public void removeRing(String ringName) {
        plugin.getConfig().set("rings." + ringName, null);
        plugin.saveConfig();
    }
    
    public void clearAllRings() {
        plugin.getConfig().set("rings", null);
        plugin.saveConfig();
    }

    /**
     * Save a POINT-type ring (player standing location, no WorldEdit selection).
     */
    public void addRingPoint(String ringName, Location loc, int order, Orientation orientation, double radius) {
        String path = "rings." + ringName;
        plugin.getConfig().set(path + ".type", "POINT");
        plugin.getConfig().set(path + ".world", loc.getWorld().getName());
        plugin.getConfig().set(path + ".x", loc.getX());
        plugin.getConfig().set(path + ".y", loc.getY());
        plugin.getConfig().set(path + ".z", loc.getZ());
        plugin.getConfig().set(path + ".order", order);
        plugin.getConfig().set(path + ".orientation", orientation.name());
        plugin.getConfig().set(path + ".radius", radius);
        plugin.saveConfig();
    }

    /**
     * Save a REGION-type ring (from WorldEdit cuboid selection).
     */
    public void addRingRegion(String ringName, Location min, Location max, int order, Orientation orientation, double radius) {
        String path = "rings." + ringName;
        plugin.getConfig().set(path + ".type", "REGION");
        plugin.getConfig().set(path + ".world", min.getWorld().getName());
        plugin.getConfig().set(path + ".min.x", min.getX());
        plugin.getConfig().set(path + ".min.y", min.getY());
        plugin.getConfig().set(path + ".min.z", min.getZ());
        plugin.getConfig().set(path + ".max.x", max.getX());
        plugin.getConfig().set(path + ".max.y", max.getY());
        plugin.getConfig().set(path + ".max.z", max.getZ());
        plugin.getConfig().set(path + ".order", order);
        plugin.getConfig().set(path + ".orientation", orientation.name());
        plugin.getConfig().set(path + ".radius", radius);
        plugin.saveConfig();
    }

    /**
     * Load all rings as RingDefinitions. Backward-compatible with legacy configs
     * that only store x/y/z/world (treats them as POINT with defaults).
     */
    public Map<String, RingDefinition> getRingDefinitions() {
        Map<String, RingDefinition> rings = new LinkedHashMap<>();
        ConfigurationSection ringsSection = plugin.getConfig().getConfigurationSection("rings");

        if (ringsSection == null) return rings;

        // Skip non-ring keys (settings like default-orientation, enforce-order, default-radius)
        double defaultRadius = getDefaultRingRadius();
        String defaultOrientStr = getDefaultOrientation();
        Orientation defaultOrientation;
        try {
            defaultOrientation = Orientation.valueOf(defaultOrientStr);
        } catch (IllegalArgumentException e) {
            defaultOrientation = Orientation.VERTICAL_NS;
        }

        int autoOrder = 1;
        for (String ringName : ringsSection.getKeys(false)) {
            // Skip config settings stored under rings.*
            if (ringName.equals("default-orientation") || ringName.equals("enforce-order") || ringName.equals("default-radius")) {
                continue;
            }

            ConfigurationSection ring = ringsSection.getConfigurationSection(ringName);
            if (ring == null) continue;

            String worldName = ring.getString("world");
            if (worldName == null) continue;
            World world = plugin.getServer().getWorld(worldName);
            if (world == null) continue;

            // Parse type (default POINT for legacy)
            String typeStr = ring.getString("type", "POINT");
            RingType type;
            try {
                type = RingType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                type = RingType.POINT;
            }

            // Parse orientation
            String orientStr = ring.getString("orientation", defaultOrientStr);
            Orientation orientation;
            try {
                orientation = Orientation.valueOf(orientStr);
            } catch (IllegalArgumentException e) {
                orientation = defaultOrientation;
            }

            int order = ring.getInt("order", autoOrder);
            double radius = ring.getDouble("radius", defaultRadius);

            if (type == RingType.REGION && ring.getConfigurationSection("min") != null) {
                ConfigurationSection minSec = ring.getConfigurationSection("min");
                ConfigurationSection maxSec = ring.getConfigurationSection("max");
                if (minSec != null && maxSec != null) {
                    Location min = new Location(world, minSec.getDouble("x"), minSec.getDouble("y"), minSec.getDouble("z"));
                    Location max = new Location(world, maxSec.getDouble("x"), maxSec.getDouble("y"), maxSec.getDouble("z"));
                    rings.put(ringName, new RingDefinition(ringName, order, orientation, radius, min, max));
                }
            } else {
                // POINT type or legacy format
                double x = ring.getDouble("x");
                double y = ring.getDouble("y");
                double z = ring.getDouble("z");
                Location center = new Location(world, x, y, z);
                rings.put(ringName, new RingDefinition(ringName, order, orientation, radius, center));
            }

            autoOrder++;
        }

        return rings;
    }

    /**
     * Get the next available ring order number.
     */
    public int getNextRingOrder() {
        Map<String, RingDefinition> existing = getRingDefinitions();
        int max = 0;
        for (RingDefinition def : existing.values()) {
            if (def.getOrder() > max) {
                max = def.getOrder();
            }
        }
        return max + 1;
    }

    public String getMessage(String key) {
        return plugin.getConfig().getString("messages." + key, "&cMessage not found: " + key)
                .replace("&", "§");
    }

    public String getPrefix() {
        return getMessage("prefix");
    }
}