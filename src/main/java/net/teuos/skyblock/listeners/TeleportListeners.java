package net.teuos.skyblock.listeners;

import net.teuos.skyblock.managers.IslandDataManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class TeleportListeners implements Listener {

    private final IslandDataManager islandDataManager;

    public TeleportListeners(IslandDataManager islandDataManager) {
        this.islandDataManager = islandDataManager;

    }


    @EventHandler
    public void onRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.getLastDeathLocation() == null) {
            return;
        }
        World deathWorld = player.getLastDeathLocation().getWorld();

        if (deathWorld == null) {
            return;
        }

        if (islandDataManager.islandExists(deathWorld.getName())) {
            event.setRespawnLocation(deathWorld.getSpawnLocation());
        }
    }

}
