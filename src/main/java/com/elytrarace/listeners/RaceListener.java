/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.listeners;

import com.elytrarace.ElytraRacePlugin;
import com.elytrarace.data.RingDefinition;
import com.elytrarace.managers.RegionManager;
import com.elytrarace.managers.RaceManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * HEAVILY OPTIMIZED RaceListener with Feature 9: Boundary checking
 */
public class RaceListener implements Listener {

    private final ElytraRacePlugin plugin;
    private final RaceManager raceManager;
    private final RegionManager regionManager;

    // OPTIMIZATION: Cache region states to avoid repeated region checks
    private final Map<UUID, Boolean> inStartCache = new HashMap<>();
    private final Map<UUID, Long> lastRegionCheck = new HashMap<>();
    
    // NEW: Feature 9 - Boundary check timing
    private final Map<UUID, Long> lastBoundaryCheck = new HashMap<>();
    
    // Ring definitions cache (v1.4.0: uses RingDefinition with type/orientation/bounds)
    private final Map<String, RingDefinition> ringDefCache = new HashMap<>();

    // Check regions only every 250ms (5 ticks) instead of every tick
    private static final long REGION_CHECK_INTERVAL = 250;

    // NEW: Feature 9 - Boundary check every 500ms
    private static final long BOUNDARY_CHECK_INTERVAL = 500;

    public RaceListener(ElytraRacePlugin plugin) {
        this.plugin = plugin;
        this.raceManager = plugin.getRaceManager();
        this.regionManager = plugin.getRegionManager();
        cacheRingData();
    }

    /**
     * Pre-cache ring definitions to avoid repeated config lookups.
     * v1.4.0: Now uses RingDefinition with type-aware detection.
     */
    private void cacheRingData() {
        ringDefCache.putAll(plugin.getConfigManager().getRingDefinitions());
    }

    /**
     * Refresh ring cache (call this after adding/removing rings)
     */
    public void refreshRingCache() {
        ringDefCache.clear();
        cacheRingData();
    }

    /**
     * Get the cached ring definitions (for use by other systems).
     */
    public Map<String, RingDefinition> getCachedRings() {
        return Collections.unmodifiableMap(ringDefCache);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        // OPTIMIZATION 1: Ignore head movements (only check if player actually moved blocks)
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null || (from.getBlockX() == to.getBlockX() && 
                           from.getBlockY() == to.getBlockY() && 
                           from.getBlockZ() == to.getBlockZ())) {
            return; // Player only turned their head, skip all checks
        }

        // OPTIMIZATION 2: Cache-based region checking (only check every 250ms)
        long now = System.currentTimeMillis();
        Long lastCheck = lastRegionCheck.get(uuid);
        if (lastCheck == null || (now - lastCheck) > REGION_CHECK_INTERVAL) {
            lastRegionCheck.put(uuid, now);
            checkRegions(player, uuid);
        }

        // OPTIMIZATION 3: Check rings if player is racing OR in test mode
        if ((raceManager.isRacing() || raceManager.isInTestMode(uuid))
                && raceManager.getRacePlayers().containsKey(uuid)) {
            checkRings(player, uuid, to);
        }
        
        // NEW: Feature 9 - Boundary checking (only every 500ms)
        Long lastBoundaryCheckTime = lastBoundaryCheck.get(uuid);
        if ((lastBoundaryCheckTime == null || (now - lastBoundaryCheckTime) > BOUNDARY_CHECK_INTERVAL)
                && raceManager.isRacing() 
                && raceManager.getRacePlayers().containsKey(uuid)) {
            lastBoundaryCheck.put(uuid, now);
            raceManager.checkPlayerBoundary(player);
        }
    }

    /**
     * OPTIMIZED: Region checking with caching
     */
    private void checkRegions(Player player, UUID uuid) {
        boolean inStart = regionManager.isInsideRegion(player, RegionManager.RegionType.START);
        boolean inFinish = regionManager.isInsideRegion(player, RegionManager.RegionType.FINISH);
        
        Boolean wasInStart = inStartCache.get(uuid);

        // Handle start region transitions
        if (inStart && (wasInStart == null || !wasInStart)) {
            raceManager.playerEnteredStart(player);
            inStartCache.put(uuid, true);
        } else if (!inStart && wasInStart != null && wasInStart) {
            raceManager.playerLeftStart(player);
            inStartCache.put(uuid, false);
        }

        // Handle finish region
        if (inFinish && raceManager.isRacing()) {
            raceManager.tryFinish(player);
        }
    }

    /**
     * Ring detection using RingDefinition.contains() which handles
     * both POINT (sphere) and REGION (cuboid) detection automatically.
     */
    private void checkRings(Player player, UUID uuid, Location playerLoc) {
        var playerData = raceManager.getRacePlayers().get(uuid);
        if (playerData == null || playerData.isFinished()) {
            return;
        }

        for (Map.Entry<String, RingDefinition> entry : ringDefCache.entrySet()) {
            RingDefinition ring = entry.getValue();
            if (ring.contains(playerLoc)) {
                raceManager.passRing(player, entry.getKey());
            }
        }
    }

    /**
     * Track firework rocket usage for disqualification
     */
    @EventHandler
    public void onRocketLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        
        Player player = (Player) event.getEntity().getShooter();
        
        // Check if player is gliding (using elytra)
        if (!player.isGliding()) return;
        
        // Check if it's a firework rocket
        if (event.getEntity().getType() == EntityType.FIREWORK_ROCKET) {
            var data = raceManager.getRacePlayers().get(player.getUniqueId());
            if (data != null && !data.isFinished()) {
                boolean allowed = data.useRocket();
                if (!allowed) {
                    event.setCancelled(true);
                    plugin.getRaceManager().getSoundManager().playDisqualification(player);
                    player.sendMessage(plugin.getConfigManager().getPrefix() +
                        "§c§lDISQUALIFIED: §fExceeded rocket limit!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        // Clean up caches
        inStartCache.remove(uuid);
        lastRegionCheck.remove(uuid);
        lastBoundaryCheck.remove(uuid); // NEW
        
        // Handle race disconnect
        if (raceManager.isPlayerInStart(player)) {
            raceManager.playerLeftStart(player);
        }
        
        // Mark as DNF if racing
        if (raceManager.isRacing()) {
            var data = raceManager.getRacePlayers().get(uuid);
            if (data != null && !data.isFinished()) {
                data.disqualify("Disconnected mid-race");
                raceManager.broadcastToAll("§c" + player.getName() + " disconnected (DNF)");
            }
        }
    }

}