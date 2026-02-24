/*
 * Copyright (c) 2025 Kartik Fulara
 *
 * This file is part of ElytraRace.
 *
 * ElytraRace is licensed under the MIT License.
 * See LICENSE file in the project root for full details.
 */

package com.elytrarace.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

/**
 * Centralized sound effects for race events (v1.4.2).
 */
public class SoundManager {

    // Ring pass — satisfying ding
    public void playRingPass(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.4f);
    }

    // Wrong ring — error buzz
    public void playWrongRing(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
    }

    // Countdown tick (3, 2, 1)
    public void playCountdownTick(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
    }

    // Countdown GO
    public void playCountdownGo(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
    }

    // Race finish — triumphant fanfare
    public void playRaceFinish(Player player) {
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }

    // All rings complete — ready for finish
    public void playAllRingsComplete(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
    }

    // Player ready up
    public void playReadyUp(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.5f);
    }

    // Disqualification
    public void playDisqualification(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.5f, 1.0f);
    }

    // Broadcast a sound to multiple players by UUID
    public void broadcastSound(Collection<UUID> uuids, SoundEffect effect) {
        for (UUID uuid : uuids) {
            Player p = org.bukkit.Bukkit.getPlayer(uuid);
            if (p != null) {
                switch (effect) {
                    case COUNTDOWN_TICK -> playCountdownTick(p);
                    case COUNTDOWN_GO -> playCountdownGo(p);
                    case RACE_FINISH -> playRaceFinish(p);
                }
            }
        }
    }

    public enum SoundEffect {
        COUNTDOWN_TICK,
        COUNTDOWN_GO,
        RACE_FINISH
    }
}
