package net.teuos.skyblock.listeners;

import net.teuos.skyblock.managers.IslandDataManager;
import net.teuos.skyblock.managers.IslandLevelManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.io.IOException;

public class CobbleGen implements Listener {

    private final IslandLevelManager levelManager;
    private final IslandDataManager islandDataManager;

    public CobbleGen(IslandLevelManager levelManager, IslandDataManager islandDataManager) {
        this.levelManager = levelManager;
        this.islandDataManager = islandDataManager;
    }


    @EventHandler
    public void onBlockFormEvent(BlockFormEvent event) throws IOException {
        if (event.getNewState().getType() == Material.COBBLESTONE) {
            if (islandDataManager.getGenLevel(event.getNewState().getLocation().getWorld().getName()) >= 1) {
                event.getNewState().setType(Material.IRON_ORE);
            }
            else {
                event.getNewState().setType(Material.COAL_ORE);
            }
        }

    }
}


