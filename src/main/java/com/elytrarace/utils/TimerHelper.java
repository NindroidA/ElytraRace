
/*
 * Copyright (c) 2025 Kartik Fulara
 * 
 * This file is part of ElytraRace.
 * 
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.utils;

import com.elytrarace.ElytraRacePlugin;
import com.elytrarace.data.PlayerRaceData;
import com.elytrarace.data.RingDefinition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;


/**
 * Timer helper — shows action bar timer and center countdown titles.
 * - ActionBar updates every 1s (you selected A)
 * - Center countdown uses 0.5s steps (10 ticks)
 */
public class TimerHelper {

    private final ElytraRacePlugin plugin;

    public TimerHelper(ElytraRacePlugin plugin) {
        this.plugin = plugin;
    }

    public String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public String formatTimeDetailed(double seconds) {
        long minutes = (long) (seconds / 60);
        double remaining = seconds % 60;
        return String.format("%02d:%05.2f", minutes, remaining);
    }

    public void updatePlayerTime(Player player, double currentTime) {
        PlayerRaceData data = plugin.getRaceManager().getRacePlayers().get(player.getUniqueId());
        if (data == null) return;

        int ringsTotal = plugin.getConfigManager().getRingDefinitions().size();
        int ringsPassed = data.getRingsCount();

        String timeStr = formatTimeDetailed(currentTime);
        String progressStr = ringsPassed + "/" + ringsTotal;

        // GTA-style navigation indicator
        RingDefinition nextRing = plugin.getRaceManager().getNextRingForPlayer(player.getUniqueId());
        String navStr = "";
        if (nextRing != null) {
            Location center = nextRing.getCenter();
            if (player.getWorld().equals(center.getWorld())) {
                double dist = player.getLocation().distance(center);
                String arrow = getDirectionArrow(player, center);
                navStr = " §7| §b" + arrow + " §f" + (int) dist + "m";
            }
        }

        player.sendActionBar("§6⏱ §e" + timeStr + " §7| §aRings: §e" + progressStr + navStr);
    }

    /**
     * Get an 8-direction arrow pointing from the player toward the target.
     * Computes the relative angle between player yaw and target bearing.
     */
    private String getDirectionArrow(Player player, Location target) {
        Location loc = player.getLocation();
        double dx = target.getX() - loc.getX();
        double dz = target.getZ() - loc.getZ();

        // Bearing to target (0 = south, clockwise)
        double targetAngle = Math.toDegrees(Math.atan2(-dx, dz));
        // Player yaw (0 = south, clockwise)
        double playerYaw = loc.getYaw() % 360;
        if (playerYaw < 0) playerYaw += 360;

        // Relative angle: how far the target is from where the player is looking
        double relative = targetAngle - playerYaw;
        if (relative < 0) relative += 360;
        if (relative > 360) relative -= 360;

        // 8-direction arrows (each 45 degrees)
        if (relative >= 337.5 || relative < 22.5) return "↑";       // Ahead
        if (relative < 67.5) return "↗";    // Front-right
        if (relative < 112.5) return "→";   // Right
        if (relative < 157.5) return "↘";   // Back-right
        if (relative < 202.5) return "↓";   // Behind
        if (relative < 247.5) return "↙";   // Back-left
        if (relative < 292.5) return "←";   // Left
        return "↖";                          // Front-left
    }

    /**
     * Show the center countdown step:
     * steps: 3 -> 2 -> 1 -> READY -> GO
     * color: yellow (user requested)
     * duration per step: 0.5s (10 ticks)
     */
    public void showCenterCountdownStep(Player player, int stepIndex) {
        // stepIndex: 0 => "3", 1 => "2", 2 => "1", 3 => "READY", 4 => "GO"
        String title;
        String subtitle = "";
        switch (stepIndex) {
            case 0: title = "§e3"; subtitle = "§7Get ready..."; break;
            case 1: title = "§e2"; subtitle = "§7Get ready..."; break;
            case 2: title = "§e1"; subtitle = "§7Get ready..."; break;
            case 3: title = "§eREADY"; subtitle = "§7Almost..."; break;
            case 4: title = "§a§lGO!"; subtitle = "§7Fly!"; break;
            default: title = "§e"; break;
        }
        player.sendTitle(title, subtitle, 0, 10, 2);
    }

    public void broadcastCenterCountdownStep(int stepIndex) {
        for (UUID uuid : plugin.getRaceManager().getRacePlayers().keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                showCenterCountdownStep(p, stepIndex);
            }
        }
    }
}
